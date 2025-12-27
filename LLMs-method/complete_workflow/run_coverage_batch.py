#!/usr/bin/env python3
"""
批量覆盖率分析 - 支持断点续传和进度保存
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

PROGRESS_FILE = RESULTS_DIR / "progress.json"
DEFECTS4J_HOME = "/home/shenxx/defects4j"
WORKSPACE_BASE = "/tmp/d4j_workspaces"

def windows_to_wsl_path(win_path):
    """将 Windows 路径转换为 WSL 路径"""
    win_path = str(win_path)
    if win_path[1:3] == ':\\':
        drive = win_path[0].lower()
        rest = win_path[3:].replace('\\', '/')
        return f"/mnt/{drive}/{rest}"
    return win_path.replace('\\', '/')

def run_wsl_command(cmd, print_output=False):
    """在 WSL 中执行命令"""
    full_cmd = ["wsl", "bash", "-c", cmd]
    if print_output:
        print(f"  [CMD] {cmd[:100]}...")
    
    result = subprocess.run(
        full_cmd,
        capture_output=True,
        text=True,
        encoding='utf-8',
        errors='replace'
    )
    
    return result

def get_test_class_name(test_file_path):
    """从测试文件中提取测试类名"""
    with open(test_file_path, 'r', encoding='utf-8') as f:
        for line in f:
            if line.strip().startswith('public class '):
                class_name = line.split('class')[1].split('{')[0].strip()
                return class_name
    return None

def parse_coverage_xml(xml_file):
    """解析 coverage.xml 文件提取覆盖率数据"""
    try:
        tree = ET.parse(xml_file)
        root = tree.getroot()
        
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
        return None

def analyze_single_test(project, bug_id, model):
    """分析单个测试 - 简化版,减少输出"""
    
    # 1. 定位测试文件
    test_dir = GENERATED_TESTS_DIR / project / str(bug_id) / model
    test_files = list(test_dir.glob("*Test.java"))
    
    if not test_files:
        return {"error": "测试文件未找到", "coverage_success": False}
    
    test_file = test_files[0]
    test_class_name = get_test_class_name(test_file)
    
    if not test_class_name:
        return {"error": "无法提取测试类名", "coverage_success": False}
    
    # 2. 创建工作目录
    workspace = f"{WORKSPACE_BASE}/{project}_{bug_id}_{model}"
    run_wsl_command(f"rm -rf {workspace}", print_output=False)
    
    # 3. Checkout
    checkout_cmd = f"cd {DEFECTS4J_HOME} && ./framework/bin/defects4j checkout -p {project} -v {bug_id}b -w {workspace} 2>&1 | grep -E '(OK|FAIL|Error)'"
    result = run_wsl_command(checkout_cmd)
    
    if result.returncode != 0:
        return {"error": "Checkout失败", "coverage_success": False}
    
    # 4. 获取测试目录并复制文件
    export_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j export -p dir.src.tests"
    result = run_wsl_command(export_cmd, print_output=False)
    
    if result.returncode != 0:
        return {"error": "获取测试目录失败", "coverage_success": False}
    
    test_src_dir = result.stdout.strip()
    wsl_test_file = windows_to_wsl_path(test_file)
    copy_cmd = f"mkdir -p {workspace}/{test_src_dir} && cp {wsl_test_file} {workspace}/{test_src_dir}/"
    
    if run_wsl_command(copy_cmd).returncode != 0:
        return {"error": "复制测试文件失败", "coverage_success": False}
    
    # 5. 编译
    compile_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j compile 2>&1 | tail -1"
    if run_wsl_command(compile_cmd).returncode != 0:
        return {"error": "编译失败", "coverage_success": False}
    
    # 6. 运行测试
    test_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j test 2>&1 | grep -E '(Failing|OK)'"
    test_result = run_wsl_command(test_cmd)
    test_passed = test_result.returncode == 0
    
    # 7. 运行覆盖率
    coverage_cmd = f"cd {workspace} && {DEFECTS4J_HOME}/framework/bin/defects4j coverage 2>&1 | grep -E '(coverage|OK)'"
    coverage_result = run_wsl_command(coverage_cmd)
    
    # 8. 收集结果
    result_dir = RESULTS_DIR / project / str(bug_id) / model
    result_dir.mkdir(parents=True, exist_ok=True)
    
    wsl_result_dir = windows_to_wsl_path(result_dir)
    copy_result_cmd = f"cp {workspace}/coverage.xml {wsl_result_dir}/ 2>/dev/null"
    run_wsl_command(copy_result_cmd, print_output=False)
    
    coverage_xml = result_dir / "coverage.xml"
    coverage_data = None
    if coverage_xml.exists():
        coverage_data = parse_coverage_xml(coverage_xml)
    
    # 9. 返回结果
    metadata = {
        "project": project,
        "bug_id": bug_id,
        "model": model,
        "test_class": test_class_name,
        "test_file": str(test_file.name),
        "checkout_success": True,
        "compile_success": True,
        "test_passed": test_passed,
        "coverage_success": coverage_result.returncode == 0 and coverage_data is not None,
        "coverage_data": coverage_data,
        "timestamp": datetime.now().isoformat()
    }
    
    # 保存单个结果
    metadata_file = result_dir / "metadata.json"
    with open(metadata_file, 'w', encoding='utf-8') as f:
        json.dump(metadata, f, indent=2, ensure_ascii=False)
    
    return metadata

def load_progress():
    """加载进度"""
    if PROGRESS_FILE.exists():
        with open(PROGRESS_FILE, 'r', encoding='utf-8') as f:
            return json.load(f)
    return {"completed": [], "failed": [], "last_index": 0}

def save_progress(progress):
    """保存进度"""
    with open(PROGRESS_FILE, 'w', encoding='utf-8') as f:
        json.dump(progress, f, indent=2, ensure_ascii=False)

def main():
    """主函数"""
    start_time = time.time()
    
    print("="*80)
    print("Defects4J 批量覆盖率分析 (支持断点续传)")
    print("="*80)
    
    # 读取测试列表
    if not SUCCESSFUL_TESTS_FILE.exists():
        print(f"[ERROR] 找不到: {SUCCESSFUL_TESTS_FILE}")
        sys.exit(1)
    
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
    
    print(f"\n[INFO] 共 {len(tests)} 个测试")
    
    # 加载进度
    progress = load_progress()
    start_index = progress.get("last_index", 0)
    
    if start_index > 0:
        print(f"[INFO] 检测到之前的进度: 已完成 {start_index}/{len(tests)}")
        response = input("是否继续上次进度? (y/N): ").strip().lower()
        if response != 'y':
            start_index = 0
            progress = {"completed": [], "failed": [], "last_index": 0}
    
    print(f"\n开始处理: 从第 {start_index + 1} 个开始")
    print("="*80)
    
    # 处理测试
    results = []
    
    for i in range(start_index, len(tests)):
        test = tests[i]
        
        print(f"\n[{i+1}/{len(tests)}] {test['project']}-{test['bug_id']}-{test['model']}", end=" ... ")
        sys.stdout.flush()
        
        try:
            result = analyze_single_test(test['project'], test['bug_id'], test['model'])
            
            if result.get('coverage_success'):
                print("✓ OK", end="")
                cov = result.get('coverage_data', {})
                if cov:
                    print(f" (行:{cov['line_coverage']:.1f}% 分支:{cov['branch_coverage']:.1f}%)")
                else:
                    print()
                progress["completed"].append(f"{test['project']}-{test['bug_id']}-{test['model']}")
            else:
                print(f"✗ FAIL ({result.get('error', 'Unknown')})")
                progress["failed"].append(f"{test['project']}-{test['bug_id']}-{test['model']}")
            
            results.append(result)
            
        except KeyboardInterrupt:
            print("\n\n[INTERRUPTED] 用户中断,保存进度...")
            progress["last_index"] = i
            save_progress(progress)
            print(f"进度已保存,下次运行将从第 {i+1} 个继续")
            sys.exit(0)
            
        except Exception as e:
            print(f"✗ ERROR: {e}")
            progress["failed"].append(f"{test['project']}-{test['bug_id']}-{test['model']}")
            results.append({"error": str(e), "coverage_success": False})
        
        # 每10个保存一次进度
        if (i + 1) % 10 == 0:
            progress["last_index"] = i + 1
            save_progress(progress)
            elapsed = time.time() - start_time
            avg_time = elapsed / (i - start_index + 1)
            remaining = avg_time * (len(tests) - i - 1)
            print(f"  [进度已保存] 已耗时:{elapsed/60:.1f}分钟, 预计剩余:{remaining/60:.1f}分钟")
    
    # 完成
    progress["last_index"] = len(tests)
    save_progress(progress)
    
    elapsed_time = time.time() - start_time
    
    # 生成 CSV
    csv_file = RESULTS_DIR / "coverage_summary.csv"
    with open(csv_file, 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow([
            'Project', 'BugID', 'Model', 
            'CoverageSuccess', 'TestPassed',
            'LineCoverage(%)', 'BranchCoverage(%)',
            'LinesCovered', 'LinesValid', 'BranchesCovered', 'BranchesValid'
        ])
        
        for r in results:
            if not r:
                continue
            cov = r.get('coverage_data', {}) or {}
            writer.writerow([
                r.get('project', ''),
                r.get('bug_id', ''),
                r.get('model', ''),
                'Yes' if r.get('coverage_success') else 'No',
                'Yes' if r.get('test_passed') else 'No',
                cov.get('line_coverage', ''),
                cov.get('branch_coverage', ''),
                cov.get('lines_covered', ''),
                cov.get('lines_valid', ''),
                cov.get('branches_covered', ''),
                cov.get('branches_valid', '')
            ])
    
    # 统计
    successful = sum(1 for r in results if r.get('coverage_success'))
    failed = len(results) - successful
    
    print(f"\n\n{'='*80}")
    print(f"全部完成!")
    print(f"{'='*80}")
    print(f"总耗时: {elapsed_time/60:.1f} 分钟")
    print(f"成功: {successful}/{len(results)} ({successful/len(results)*100:.1f}%)")
    print(f"失败: {failed}/{len(results)}")
    print(f"\n结果文件:")
    print(f"  - CSV汇总: {csv_file}")
    print(f"  - 详细结果: {RESULTS_DIR}/{{Project}}/{{BugID}}/{{Model}}/")
    print(f"{'='*80}")

if __name__ == "__main__":
    main()
