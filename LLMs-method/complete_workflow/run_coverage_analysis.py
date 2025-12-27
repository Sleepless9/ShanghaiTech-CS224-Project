#!/usr/bin/env python3
"""
使用 Defects4J 自带的 coverage 工具检测测试代码的覆盖率

工作流程:
1. 读取 successful_tests.txt (100个成功编译的测试)
2. 对每个测试:
   - 在 WSL 中 checkout buggy version 到临时目录
   - 复制测试文件到工作目录
   - 编译代码
   - 运行 coverage 分析
   - 收集结果
3. 生成汇总报告 (CSV + JSON)
"""

import os
import json
import subprocess
import sys
import csv
import time
import xml.etree.ElementTree as ET
from pathlib import Path
from datetime import datetime

# 配置路径
SCRIPT_DIR = Path(__file__).parent
GENERATED_TESTS_DIR = SCRIPT_DIR / "generated_tests"
SUCCESSFUL_TESTS_FILE = GENERATED_TESTS_DIR / "successful_tests.txt"
RESULTS_DIR = SCRIPT_DIR / "coverage_results"
RESULTS_DIR.mkdir(exist_ok=True)

# WSL 路径配置
DEFECTS4J_HOME = "/home/shenxx/defects4j"
WORKSPACE_BASE = "/tmp/d4j_workspaces"

def windows_to_wsl_path(win_path):
    """将 Windows 路径转换为 WSL 路径"""
    win_path = str(win_path)
    # Z:\ -> /mnt/z/
    if win_path[1:3] == ':\\':
        drive = win_path[0].lower()
        rest = win_path[3:].replace('\\', '/')
        return f"/mnt/{drive}/{rest}"
    return win_path.replace('\\', '/')

def run_wsl_command(cmd, print_output=True):
    """在 WSL 中执行命令"""
    full_cmd = ["wsl", "bash", "-c", cmd]
    if print_output:
        print(f"[CMD] {cmd}")
    
    result = subprocess.run(
        full_cmd,
        capture_output=True,
        text=True,
        encoding='utf-8',
        errors='replace'
    )
    
    if print_output and result.stdout:
        print(result.stdout)
    if print_output and result.stderr:
        print(f"[STDERR] {result.stderr}", file=sys.stderr)
    
    return result

def get_test_class_name(test_file_path):
    """从测试文件中提取测试类名"""
    with open(test_file_path, 'r', encoding='utf-8') as f:
        for line in f:
            if line.strip().startswith('public class '):
                # public class XxxTest {
                class_name = line.split('class')[1].split('{')[0].strip()
                return class_name
    return None

def parse_coverage_xml(xml_file):
    """解析 coverage.xml 文件提取覆盖率数据"""
    try:
        tree = ET.parse(xml_file)
        root = tree.getroot()
        
        # 从根节点获取总体覆盖率
        line_rate = float(root.get('line-rate', 0))
        branch_rate = float(root.get('branch-rate', 0))
        lines_covered = int(root.get('lines-covered', 0))
        lines_valid = int(root.get('lines-valid', 0))
        branches_covered = int(root.get('branches-covered', 0))
        branches_valid = int(root.get('branches-valid', 0))
        
        return {
            'line_coverage': round(line_rate * 100, 2),
            'branch_coverage': round(branch_rate * 100, 2),
            'lines_covered': lines_covered,
            'lines_valid': lines_valid,
            'branches_covered': branches_covered,
            'branches_valid': branches_valid
        }
    except Exception as e:
        print(f"[ERROR] 解析 coverage.xml 失败: {e}")
        return None

def analyze_coverage_for_test(project, bug_id, model):
    """为单个测试运行覆盖率分析"""
    print(f"\n{'='*80}")
    print(f"分析: {project}-{bug_id}-{model}")
    print(f"{'='*80}")
    
    # 1. 定位测试文件
    test_dir = GENERATED_TESTS_DIR / project / str(bug_id) / model
    test_files = list(test_dir.glob("*Test.java"))
    
    if not test_files:
        print(f"[ERROR] 未找到测试文件: {test_dir}")
        return None
    
    test_file = test_files[0]
    test_class_name = get_test_class_name(test_file)
    
    if not test_class_name:
        print(f"[ERROR] 无法提取测试类名: {test_file}")
        return None
    
    print(f"[INFO] 测试文件: {test_file.name}")
    print(f"[INFO] 测试类名: {test_class_name}")
    
    # 2. 创建工作目录
    workspace = f"{WORKSPACE_BASE}/{project}_{bug_id}_{model}"
    
    # 清理旧工作目录
    cleanup_cmd = f"rm -rf {workspace}"
    run_wsl_command(cleanup_cmd, print_output=False)
    
    # 3. Checkout buggy version
    print(f"\n[STEP 1] Checkout {project}-{bug_id}b ...")
    checkout_cmd = f"cd {DEFECTS4J_HOME} && ./framework/bin/defects4j checkout -p {project} -v {bug_id}b -w {workspace}"
    result = run_wsl_command(checkout_cmd)
    
    if result.returncode != 0:
        print(f"[ERROR] Checkout 失败")
        return None
    
    # 4. 复制测试文件
    print(f"\n[STEP 2] 复制测试文件 ...")
    
    # 先获取测试目录
    export_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j export -p dir.src.tests"
    result = run_wsl_command(export_cmd, print_output=False)
    
    if result.returncode != 0:
        print(f"[ERROR] 无法获取测试目录")
        return None
    
    test_src_dir = result.stdout.strip()
    print(f"[INFO] 测试源目录: {test_src_dir}")
    
    # 复制测试文件
    wsl_test_file = windows_to_wsl_path(test_file)
    copy_cmd = f"mkdir -p {workspace}/{test_src_dir} && cp {wsl_test_file} {workspace}/{test_src_dir}/"
    result = run_wsl_command(copy_cmd)
    
    if result.returncode != 0:
        print(f"[ERROR] 复制测试文件失败")
        return None
    
    # 5. 编译
    print(f"\n[STEP 3] 编译项目 ...")
    compile_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j compile"
    result = run_wsl_command(compile_cmd)
    
    if result.returncode != 0:
        print(f"[ERROR] 编译失败")
        return None
    
    # 6. 运行测试 (可选，检查测试是否能执行)
    print(f"\n[STEP 4] 运行测试 ...")
    test_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j test -t {test_class_name}"
    test_result = run_wsl_command(test_cmd)
    
    # 7. 运行覆盖率分析
    print(f"\n[STEP 5] 运行覆盖率分析 ...")
    coverage_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j coverage -t {test_class_name}"
    coverage_result = run_wsl_command(coverage_cmd)
    
    if coverage_result.returncode != 0:
        print(f"[WARNING] Coverage 命令返回非零状态")
    
    # 8. 收集覆盖率结果
    print(f"\n[STEP 6] 收集覆盖率结果 ...")
    
    # 复制覆盖率结果到 Windows
    result_dir = RESULTS_DIR / project / str(bug_id) / model
    result_dir.mkdir(parents=True, exist_ok=True)
    
    wsl_result_dir = windows_to_wsl_path(result_dir)
    wsl_workspace = workspace
    
    # 复制 coverage.xml
    copy_result_cmd = f"cp {wsl_workspace}/coverage.xml {wsl_result_dir}/ 2>/dev/null || echo 'No coverage.xml found'"
    copy_result = run_wsl_command(copy_result_cmd, print_output=False)
    
    # 检查是否成功复制
    coverage_xml = result_dir / "coverage.xml"
    coverage_data = None
    if coverage_xml.exists():
        print(f"[OK] 覆盖率文件已保存")
        coverage_data = parse_coverage_xml(coverage_xml)
    else:
        print(f"[WARNING] 未找到 coverage.xml")
    
    # 9. 保存元数据
    metadata = {
        "project": project,
        "bug_id": bug_id,
        "model": model,
        "test_class": test_class_name,
        "test_file": str(test_file.name),
        "workspace": workspace,
        "checkout_success": True,
        "compile_success": result.returncode == 0,
        "test_passed": test_result.returncode == 0,
        "coverage_success": coverage_result.returncode == 0,
        "coverage_data": coverage_data,
        "timestamp": datetime.now().isoformat()
    }
    
    metadata_file = result_dir / "metadata.json"
    with open(metadata_file, 'w', encoding='utf-8') as f:
        json.dump(metadata, f, indent=2, ensure_ascii=False)
    
    print(f"\n[SUCCESS] 结果保存到: {result_dir}")
    
    return metadata

def main():
    """主函数"""
    start_time = time.time()
    
    print("="*80)
    print("Defects4J 覆盖率分析工具 - 批量处理模式")
    print("="*80)
    
    # 检查 successful_tests.txt
    if not SUCCESSFUL_TESTS_FILE.exists():
        print(f"[ERROR] 找不到: {SUCCESSFUL_TESTS_FILE}")
        sys.exit(1)
    
    # 读取成功编译的测试
    tests = []
    with open(SUCCESSFUL_TESTS_FILE, 'r') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith('#'):
                continue
            parts = line.split(',')
            if len(parts) == 3:
                tests.append({
                    'project': parts[0],
                    'bug_id': parts[1],
                    'model': parts[2]
                })
    
    print(f"\n[INFO] 从 successful_tests.txt 读取到 {len(tests)} 个测试")
    
    # 统计信息
    project_counts = {}
    for test in tests:
        proj = test['project']
        project_counts[proj] = project_counts.get(proj, 0) + 1
    
    print(f"\n项目分布:")
    for proj, count in sorted(project_counts.items()):
        print(f"  - {proj}: {count} 个测试")
    
    # 询问用户
    print(f"\n{'='*80}")
    print(f"准备运行所有 {len(tests)} 个测试的覆盖率分析")
    print(f"预计耗时: 约 {len(tests) * 2} 分钟 (每个测试约2分钟)")
    print(f"{'='*80}")
    
    response = input("\n确认运行? (y/N): ").strip().lower()
    
    if response != 'y':
        print("已取消")
        sys.exit(0)
    
    # 运行分析
    results = []
    successful = 0
    failed = 0
    
    for i, test in enumerate(tests, 1):
        print(f"\n\n{'='*80}")
        print(f"进度: [{i}/{len(tests)}] ({(i/len(tests)*100):.1f}%)")
        print(f"累计: 成功 {successful}, 失败 {failed}")
        print(f"{'='*80}")
        
        try:
            result = analyze_coverage_for_test(
                test['project'],
                test['bug_id'],
                test['model']
            )
            
            if result and result.get('coverage_success'):
                successful += 1
            else:
                failed += 1
            
            results.append(result)
            
        except KeyboardInterrupt:
            print("\n\n[INTERRUPTED] 用户中断")
            break
        except Exception as e:
            print(f"[ERROR] 分析失败: {e}")
            import traceback
            traceback.print_exc()
            failed += 1
            results.append({
                "project": test['project'],
                "bug_id": test['bug_id'],
                "model": test['model'],
                "error": str(e),
                "coverage_success": False
            })
    
    # 计算总耗时
    elapsed_time = time.time() - start_time
    
    # 保存详细结果 (JSON)
    detail_file = RESULTS_DIR / "coverage_analysis_detailed.json"
    with open(detail_file, 'w', encoding='utf-8') as f:
        json.dump({
            "summary": {
                "total_tests": len(tests),
                "analyzed": len(results),
                "successful": successful,
                "failed": failed,
                "elapsed_time_seconds": round(elapsed_time, 2),
                "timestamp": datetime.now().isoformat()
            },
            "results": results
        }, f, indent=2, ensure_ascii=False)
    
    # 生成 CSV 报告
    csv_file = RESULTS_DIR / "coverage_analysis_summary.csv"
    with open(csv_file, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow([
            'Project', 'BugID', 'Model', 
            'Checkout', 'Compile', 'TestPassed', 'CoverageSuccess',
            'LineCoverage(%)', 'BranchCoverage(%)',
            'LinesCovered', 'LinesValid', 'BranchesCovered', 'BranchesValid',
            'TestClass'
        ])
        
        for r in results:
            if not r:
                continue
            
            cov = r.get('coverage_data', {}) or {}
            writer.writerow([
                r.get('project', ''),
                r.get('bug_id', ''),
                r.get('model', ''),
                'Yes' if r.get('checkout_success') else 'No',
                'Yes' if r.get('compile_success') else 'No',
                'Yes' if r.get('test_passed') else 'No',
                'Yes' if r.get('coverage_success') else 'No',
                cov.get('line_coverage', ''),
                cov.get('branch_coverage', ''),
                cov.get('lines_covered', ''),
                cov.get('lines_valid', ''),
                cov.get('branches_covered', ''),
                cov.get('branches_valid', ''),
                r.get('test_class', '')
            ])
    
    # 生成统计报告
    stats_file = RESULTS_DIR / "coverage_analysis_statistics.txt"
    with open(stats_file, 'w', encoding='utf-8') as f:
        f.write("="*80 + "\n")
        f.write("Defects4J 覆盖率分析 - 统计报告\n")
        f.write("="*80 + "\n\n")
        
        f.write(f"生成时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write(f"总耗时: {elapsed_time/60:.1f} 分钟\n\n")
        
        f.write(f"总测试数: {len(tests)}\n")
        f.write(f"已分析: {len(results)}\n")
        f.write(f"成功: {successful}\n")
        f.write(f"失败: {failed}\n")
        f.write(f"成功率: {successful/len(results)*100 if results else 0:.1f}%\n\n")
        
        # 按项目统计
        f.write("="*80 + "\n")
        f.write("按项目统计:\n")
        f.write("="*80 + "\n\n")
        
        for project in sorted(set(r['project'] for r in results if r)):
            proj_results = [r for r in results if r and r.get('project') == project]
            proj_success = sum(1 for r in proj_results if r.get('coverage_success'))
            
            f.write(f"\n{project}:\n")
            f.write(f"  总数: {len(proj_results)}\n")
            f.write(f"  成功: {proj_success}\n")
            f.write(f"  失败: {len(proj_results) - proj_success}\n")
            
            # 计算平均覆盖率
            valid_coverages = [
                r.get('coverage_data', {}) 
                for r in proj_results 
                if r.get('coverage_data')
            ]
            
            if valid_coverages:
                avg_line = sum(c.get('line_coverage', 0) for c in valid_coverages) / len(valid_coverages)
                avg_branch = sum(c.get('branch_coverage', 0) for c in valid_coverages) / len(valid_coverages)
                f.write(f"  平均行覆盖率: {avg_line:.2f}%\n")
                f.write(f"  平均分支覆盖率: {avg_branch:.2f}%\n")
        
        # 按模型统计
        f.write("\n" + "="*80 + "\n")
        f.write("按模型统计:\n")
        f.write("="*80 + "\n\n")
        
        for model in ['deepseek', 'qwen']:
            model_results = [r for r in results if r and r.get('model') == model]
            model_success = sum(1 for r in model_results if r.get('coverage_success'))
            
            f.write(f"\n{model}:\n")
            f.write(f"  总数: {len(model_results)}\n")
            f.write(f"  成功: {model_success}\n")
            f.write(f"  失败: {len(model_results) - model_success}\n")
            
            valid_coverages = [
                r.get('coverage_data', {}) 
                for r in model_results 
                if r.get('coverage_data')
            ]
            
            if valid_coverages:
                avg_line = sum(c.get('line_coverage', 0) for c in valid_coverages) / len(valid_coverages)
                avg_branch = sum(c.get('branch_coverage', 0) for c in valid_coverages) / len(valid_coverages)
                f.write(f"  平均行覆盖率: {avg_line:.2f}%\n")
                f.write(f"  平均分支覆盖率: {avg_branch:.2f}%\n")
    
    # 打印最终报告
    print(f"\n\n{'='*80}")
    print(f"所有分析完成!")
    print(f"{'='*80}")
    print(f"总耗时: {elapsed_time/60:.1f} 分钟")
    print(f"成功: {successful}/{len(results)}")
    print(f"失败: {failed}/{len(results)}")
    print(f"\n结果文件:")
    print(f"  - CSV汇总: {csv_file}")
    print(f"  - JSON详情: {detail_file}")
    print(f"  - 统计报告: {stats_file}")
    print(f"  - 覆盖率文件: {RESULTS_DIR}/{{Project}}/{{BugID}}/{{Model}}/coverage.xml")
    print(f"{'='*80}")
    
    # 打印统计报告内容
    print(f"\n{stats_file.read_text(encoding='utf-8')}")

if __name__ == "__main__":
    main()
