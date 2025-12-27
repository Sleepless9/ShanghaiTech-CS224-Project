#!/usr/bin/env python3
"""
分析编译失败和覆盖率结果
"""

import json
import csv
from pathlib import Path
from collections import defaultdict

SCRIPT_DIR = Path(__file__).parent
COMPILATION_FILE = SCRIPT_DIR / "generated_tests" / "compilation_results_simple.json"
COVERAGE_FILE = SCRIPT_DIR / "coverage_results" / "coverage_summary.csv"

def analyze_compilation_failures():
    """分析编译失败的详细原因"""
    
    with open(COMPILATION_FILE, 'r', encoding='utf-8') as f:
        compilation_results = json.load(f)
    
    failures = [r for r in compilation_results if not r['compilation_success']]
    
    print("="*80)
    print("编译失败案例详细分析")
    print("="*80)
    
    # 按项目分组
    by_project = defaultdict(list)
    for f in failures:
        by_project[f['project']].append(f)
    
    print(f"\n总计: {len(failures)} 个编译失败")
    print(f"\n按项目分布:")
    for proj in sorted(by_project.keys()):
        print(f"  {proj}: {len(by_project[proj])} 个")
    
    # 详细分析每个项目
    for project in sorted(by_project.keys()):
        print(f"\n{'='*80}")
        print(f"{project} 项目编译失败分析")
        print(f"{'='*80}")
        
        failures_list = by_project[project]
        
        # 按模型分组
        by_model = defaultdict(list)
        for f in failures_list:
            by_model[f['model']].append(f)
        
        print(f"\n总计: {len(failures_list)} 个失败")
        print(f"  - deepseek: {len(by_model['deepseek'])} 个")
        print(f"  - qwen: {len(by_model['qwen'])} 个")
        
        # 查看错误类型
        error_types = defaultdict(int)
        for f in failures_list:
            if f.get('error'):
                # 简化错误信息
                error = f['error']
                if 'cannot find symbol' in error:
                    error_types['符号找不到 (缺少类/方法/变量)'] += 1
                elif 'package' in error and 'does not exist' in error:
                    error_types['包不存在'] += 1
                elif 'error:' in error.lower():
                    # 提取第一个错误
                    first_error = error.split('error:')[1].split('\n')[0].strip()
                    error_types[first_error[:50]] += 1
                else:
                    error_types['其他错误'] += 1
            else:
                error_types['未记录错误'] += 1
        
        if error_types:
            print(f"\n错误类型分布:")
            for error_type, count in sorted(error_types.items(), key=lambda x: -x[1]):
                print(f"  - {error_type}: {count} 次")
        
        # 列出所有失败的 bug ID
        bug_ids = sorted(set(f['bug_id'] for f in failures_list))
        print(f"\n失败的 Bug IDs: {', '.join(map(str, bug_ids))}")
        
        # 检查是否两个模型都失败
        both_failed = []
        only_deepseek = []
        only_qwen = []
        
        for bug_id in bug_ids:
            deepseek_failed = any(f['bug_id'] == bug_id and f['model'] == 'deepseek' for f in failures_list)
            qwen_failed = any(f['bug_id'] == bug_id and f['model'] == 'qwen' for f in failures_list)
            
            if deepseek_failed and qwen_failed:
                both_failed.append(bug_id)
            elif deepseek_failed:
                only_deepseek.append(bug_id)
            elif qwen_failed:
                only_qwen.append(bug_id)
        
        if both_failed:
            print(f"\n两个模型都失败: {', '.join(map(str, both_failed))} ({len(both_failed)} 个)")
        if only_deepseek:
            print(f"仅 deepseek 失败: {', '.join(map(str, only_deepseek))} ({len(only_deepseek)} 个)")
        if only_qwen:
            print(f"仅 qwen 失败: {', '.join(map(str, only_qwen))} ({len(only_qwen)} 个)")

def analyze_coverage_results():
    """分析覆盖率结果"""
    
    print(f"\n\n{'='*80}")
    print("覆盖率分析结果")
    print(f"{'='*80}")
    
    # 读取 CSV
    results = []
    with open(COVERAGE_FILE, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            # 清理字段名和值
            clean_row = {}
            for k, v in row.items():
                clean_key = k.strip()
                clean_value = v.strip() if v else ''
                clean_row[clean_key] = clean_value
            results.append(clean_row)
    
    # 统计
    total = len(results)
    coverage_success = sum(1 for r in results if r['CoverageSuccess'] == 'Yes')
    test_passed = sum(1 for r in results if r['TestPassed'] == 'Yes')
    
    print(f"\n总体统计:")
    print(f"  总测试数: {total}")
    print(f"  覆盖率分析成功: {coverage_success}/{total} ({coverage_success/total*100:.1f}%)")
    print(f"  测试通过: {test_passed}/{total} ({test_passed/total*100:.1f}%)")
    print(f"  测试失败 (检测到bug): {total - test_passed}/{total} ({(total-test_passed)/total*100:.1f}%)")
    
    # 按项目统计
    print(f"\n按项目统计:")
    by_project = defaultdict(list)
    for r in results:
        by_project[r['Project']].append(r)
    
    for project in sorted(by_project.keys()):
        proj_results = by_project[project]
        proj_success = sum(1 for r in proj_results if r['CoverageSuccess'] == 'Yes')
        proj_test_passed = sum(1 for r in proj_results if r['TestPassed'] == 'Yes')
        
        print(f"\n  {project}:")
        print(f"    总数: {len(proj_results)}")
        print(f"    覆盖率成功: {proj_success}/{len(proj_results)} ({proj_success/len(proj_results)*100:.1f}%)")
        print(f"    测试通过: {proj_test_passed}/{len(proj_results)}")
        print(f"    检测到bug: {len(proj_results) - proj_test_passed}/{len(proj_results)}")
        
        # 计算平均覆盖率
        valid_coverages = []
        for r in proj_results:
            if r['LineCoverage(%)']:
                try:
                    line_cov = float(r['LineCoverage(%)'])
                    branch_cov = float(r['BranchCoverage(%)']) if r['BranchCoverage(%)'] else 0
                    valid_coverages.append({'line': line_cov, 'branch': branch_cov})
                except ValueError:
                    pass
        
        if valid_coverages:
            avg_line = sum(c['line'] for c in valid_coverages) / len(valid_coverages)
            avg_branch = sum(c['branch'] for c in valid_coverages) / len(valid_coverages)
            print(f"    平均行覆盖率: {avg_line:.2f}%")
            print(f"    平均分支覆盖率: {avg_branch:.2f}%")
    
    # 按模型统计
    print(f"\n按模型统计:")
    by_model = defaultdict(list)
    for r in results:
        by_model[r['Model']].append(r)
    
    for model in sorted(by_model.keys()):
        model_results = by_model[model]
        model_success = sum(1 for r in model_results if r['CoverageSuccess'] == 'Yes')
        model_test_passed = sum(1 for r in model_results if r['TestPassed'] == 'Yes')
        
        print(f"\n  {model}:")
        print(f"    总数: {len(model_results)}")
        print(f"    覆盖率成功: {model_success}/{len(model_results)} ({model_success/len(model_results)*100:.1f}%)")
        print(f"    测试通过: {model_test_passed}/{len(model_results)}")
        print(f"    检测到bug: {len(model_results) - model_test_passed}/{len(model_results)}")
        
        # 计算平均覆盖率
        valid_coverages = []
        for r in model_results:
            if r['LineCoverage(%)']:
                try:
                    line_cov = float(r['LineCoverage(%)'])
                    branch_cov = float(r['BranchCoverage(%)']) if r['BranchCoverage(%)'] else 0
                    valid_coverages.append({'line': line_cov, 'branch': branch_cov})
                except ValueError:
                    pass
        
        if valid_coverages:
            avg_line = sum(c['line'] for c in valid_coverages) / len(valid_coverages)
            avg_branch = sum(c['branch'] for c in valid_coverages) / len(valid_coverages)
            print(f"    平均行覆盖率: {avg_line:.2f}%")
            print(f"    平均分支覆盖率: {avg_branch:.2f}%")
    
    # 覆盖率分布
    print(f"\n覆盖率分布:")
    coverage_ranges = {
        '90-100%': 0,
        '80-90%': 0,
        '70-80%': 0,
        '60-70%': 0,
        '<60%': 0
    }
    
    for r in results:
        if r['LineCoverage(%)']:
            try:
                line_cov = float(r['LineCoverage(%)'])
                if line_cov >= 90:
                    coverage_ranges['90-100%'] += 1
                elif line_cov >= 80:
                    coverage_ranges['80-90%'] += 1
                elif line_cov >= 70:
                    coverage_ranges['70-80%'] += 1
                elif line_cov >= 60:
                    coverage_ranges['60-70%'] += 1
                else:
                    coverage_ranges['<60%'] += 1
            except ValueError:
                pass
    
    for range_name, count in coverage_ranges.items():
        if count > 0:
            print(f"  {range_name}: {count} 个测试")
    
    # Top 10 最高覆盖率
    print(f"\nTop 10 最高行覆盖率:")
    sorted_results = []
    for r in results:
        if r['LineCoverage(%)']:
            try:
                line_cov = float(r['LineCoverage(%)'])
                sorted_results.append((r, line_cov))
            except ValueError:
                pass
    
    sorted_results.sort(key=lambda x: -x[1])
    for i, (r, cov) in enumerate(sorted_results[:10], 1):
        print(f"  {i}. {r['Project']}-{r['BugID']}-{r['Model']}: {cov:.2f}% (分支: {r['BranchCoverage(%)']}%)")
    
    # Bug 检测分析
    print(f"\n\nBug 检测分析:")
    bug_detected = defaultdict(list)
    for r in results:
        key = f"{r['Project']}-{r['BugID']}"
        bug_detected[key].append(r)
    
    both_detected = 0
    only_one_detected = 0
    none_detected = 0
    
    for bug_key, bug_results in bug_detected.items():
        if len(bug_results) == 2:
            deepseek_passed = any(r['Model'].strip() == 'deepseek' and r['TestPassed'] == 'No' for r in bug_results)
            qwen_passed = any(r['Model'].strip() == 'qwen' and r['TestPassed'] == 'No' for r in bug_results)
            
            if deepseek_passed and qwen_passed:
                both_detected += 1
            elif deepseek_passed or qwen_passed:
                only_one_detected += 1
            else:
                none_detected += 1
    
    print(f"  两个模型都检测到bug: {both_detected}")
    print(f"  仅一个模型检测到: {only_one_detected}")
    print(f"  都未检测到: {none_detected}")

def compare_models():
    """对比 DeepSeek 和 Qwen 的整体表现"""
    
    print(f"\n\n{'='*80}")
    print("DeepSeek vs Qwen 对比分析")
    print(f"{'='*80}")
    
    # 编译成功率对比
    with open(COMPILATION_FILE, 'r', encoding='utf-8') as f:
        compilation_results = json.load(f)
    
    deepseek_compile = [r for r in compilation_results if r['model'] == 'deepseek']
    qwen_compile = [r for r in compilation_results if r['model'] == 'qwen']
    
    deepseek_success = sum(1 for r in deepseek_compile if r['compilation_success'])
    qwen_success = sum(1 for r in qwen_compile if r['compilation_success'])
    
    print(f"\n1. 编译成功率:")
    print(f"  DeepSeek: {deepseek_success}/{len(deepseek_compile)} ({deepseek_success/len(deepseek_compile)*100:.1f}%)")
    print(f"  Qwen:     {qwen_success}/{len(qwen_compile)} ({qwen_success/len(qwen_compile)*100:.1f}%)")
    
    # 覆盖率对比
    results = []
    with open(COVERAGE_FILE, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            clean_row = {}
            for k, v in row.items():
                clean_row[k.strip()] = v.strip() if v else ''
            results.append(clean_row)
    
    deepseek_cov = [r for r in results if r['Model'] == 'deepseek']
    qwen_cov = [r for r in results if r['Model'] == 'qwen']
    
    deepseek_cov_success = sum(1 for r in deepseek_cov if r['CoverageSuccess'] == 'Yes')
    qwen_cov_success = sum(1 for r in qwen_cov if r['CoverageSuccess'] == 'Yes')
    
    print(f"\n2. 覆盖率分析成功率:")
    print(f"  DeepSeek: {deepseek_cov_success}/{len(deepseek_cov)} ({deepseek_cov_success/len(deepseek_cov)*100:.1f}%)")
    print(f"  Qwen:     {qwen_cov_success}/{len(qwen_cov)} ({qwen_cov_success/len(qwen_cov)*100:.1f}%)")
    
    # 平均覆盖率
    def calc_avg_coverage(results_list):
        coverages = []
        for r in results_list:
            if r['LineCoverage(%)']:
                try:
                    coverages.append({
                        'line': float(r['LineCoverage(%)']),
                        'branch': float(r['BranchCoverage(%)']) if r['BranchCoverage(%)'] else 0
                    })
                except ValueError:
                    pass
        if coverages:
            return {
                'line': sum(c['line'] for c in coverages) / len(coverages),
                'branch': sum(c['branch'] for c in coverages) / len(coverages)
            }
        return None
    
    deepseek_avg = calc_avg_coverage(deepseek_cov)
    qwen_avg = calc_avg_coverage(qwen_cov)
    
    print(f"\n3. 平均覆盖率:")
    if deepseek_avg:
        print(f"  DeepSeek: 行 {deepseek_avg['line']:.2f}%, 分支 {deepseek_avg['branch']:.2f}%")
    if qwen_avg:
        print(f"  Qwen:     行 {qwen_avg['line']:.2f}%, 分支 {qwen_avg['branch']:.2f}%")
    
    # Bug 检测率
    deepseek_bug_detected = sum(1 for r in deepseek_cov if r['TestPassed'] == 'No')
    qwen_bug_detected = sum(1 for r in qwen_cov if r['TestPassed'] == 'No')
    
    print(f"\n4. Bug 检测数量:")
    print(f"  DeepSeek: {deepseek_bug_detected}/{len(deepseek_cov)} ({deepseek_bug_detected/len(deepseek_cov)*100:.1f}%)")
    print(f"  Qwen:     {qwen_bug_detected}/{len(qwen_cov)} ({qwen_bug_detected/len(qwen_cov)*100:.1f}%)")
    
    print(f"\n5. 综合评分 (考虑编译成功率、覆盖率、bug检测):")
    deepseek_score = (deepseek_success/len(deepseek_compile) * 0.3 + 
                      deepseek_cov_success/len(deepseek_cov) * 0.3 +
                      (deepseek_avg['line']/100 if deepseek_avg else 0) * 0.4)
    qwen_score = (qwen_success/len(qwen_compile) * 0.3 + 
                  qwen_cov_success/len(qwen_cov) * 0.3 +
                  (qwen_avg['line']/100 if qwen_avg else 0) * 0.4)
    
    print(f"  DeepSeek: {deepseek_score:.3f}")
    print(f"  Qwen:     {qwen_score:.3f}")
    
    if deepseek_score > qwen_score:
        print(f"\n  => DeepSeek 表现更好 (高出 {(deepseek_score-qwen_score)*100:.1f}%)")
    elif qwen_score > deepseek_score:
        print(f"\n  => Qwen 表现更好 (高出 {(qwen_score-deepseek_score)*100:.1f}%)")
    else:
        print(f"\n  两者表现相当")

if __name__ == "__main__":
    analyze_compilation_failures()
    analyze_coverage_results()
    compare_models()
    
    print(f"\n{'='*80}")
    print("分析完成")
    print(f"{'='*80}\n")
