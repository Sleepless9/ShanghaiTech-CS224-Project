#!/bin/bash
# 在 WSL 中运行单个测试的覆盖率分析示例
# 用法: ./test_single_coverage.sh Lang 1 deepseek

PROJECT=$1
BUG_ID=$2
MODEL=$3

# 配置
DEFECTS4J_HOME="/home/shenxx/defects4j"
WORKSPACE="/tmp/test_workspace_${PROJECT}_${BUG_ID}_${MODEL}"
TEST_DIR="/mnt/z/研一课程/程序分析/CS224_copilot/ShanghaiTech-CS224-Project/llm_experiment_results_v2/complete_workflow/generated_tests/${PROJECT}/${BUG_ID}/${MODEL}"

echo "========================================"
echo "测试: ${PROJECT}-${BUG_ID}-${MODEL}"
echo "========================================"

# 1. 清理旧工作目录
echo "[1] 清理工作目录..."
rm -rf ${WORKSPACE}

# 2. Checkout buggy version
echo "[2] Checkout ${PROJECT}-${BUG_ID}b ..."
cd ${DEFECTS4J_HOME}
./framework/bin/defects4j checkout -p ${PROJECT} -v ${BUG_ID}b -w ${WORKSPACE}

if [ $? -ne 0 ]; then
    echo "[ERROR] Checkout 失败"
    exit 1
fi

# 3. 获取测试目录
echo "[3] 获取项目结构..."
cd ${WORKSPACE}
TEST_SRC_DIR=$(${DEFECTS4J_HOME}/framework/bin/defects4j export -p dir.src.tests)
echo "测试源目录: ${TEST_SRC_DIR}"

# 4. 复制测试文件
echo "[4] 复制测试文件..."
mkdir -p ${WORKSPACE}/${TEST_SRC_DIR}
cp ${TEST_DIR}/*.java ${WORKSPACE}/${TEST_SRC_DIR}/

if [ $? -ne 0 ]; then
    echo "[ERROR] 复制测试文件失败"
    exit 1
fi

# 提取测试类名
TEST_CLASS=$(grep "^public class" ${WORKSPACE}/${TEST_SRC_DIR}/*.java | head -1 | sed 's/.*class \([^ {]*\).*/\1/')
echo "测试类: ${TEST_CLASS}"

# 5. 编译
echo "[5] 编译..."
${DEFECTS4J_HOME}/framework/bin/defects4j compile

if [ $? -ne 0 ]; then
    echo "[ERROR] 编译失败"
    exit 1
fi

# 6. 运行测试 (运行整个测试类)
echo "[6] 运行测试..."
${DEFECTS4J_HOME}/framework/bin/defects4j test
TEST_RESULT=$?

if [ ${TEST_RESULT} -eq 0 ]; then
    echo "[OK] 测试通过"
else
    echo "[WARNING] 测试失败 (可能检测到 bug)"
fi

# 7. 运行覆盖率分析 (分析整个测试类)
echo "[7] 运行覆盖率分析..."
${DEFECTS4J_HOME}/framework/bin/defects4j coverage

# 8. 查看结果
echo ""
echo "========================================"
echo "覆盖率结果文件:"
echo "========================================"
ls -lh ${WORKSPACE}/coverage* 2>/dev/null || echo "未找到 coverage 文件"
find ${WORKSPACE} -name "*.xml" -o -name "*coverage*" 2>/dev/null

echo ""
echo "工作目录: ${WORKSPACE}"
echo "你可以手动查看该目录中的覆盖率结果"
