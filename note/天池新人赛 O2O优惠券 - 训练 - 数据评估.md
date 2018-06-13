# 天池新人赛 O2O优惠券 - 训练 - 数据评估

## ROC 曲线
《Java 机器学习》第一章内容
大多数分类算法都会返回一个分类置信度，记作 $f(X)$, 他反过来计算预测。
沿用前面信用卡欺诈的例子，规则可能如下


f(X) = 欺诈，如果f(X) > 阈值
非欺诈，其他

阈值决定错误率与真正率。我们可以把所有可能的阈值结果绘制成 ROC 曲线（受试者工作特征曲线）

## AUC 
根据 ROC 曲线下面的区域面积的算术平均值对提交的方案进行评估。ROC 曲线使用一条曲线表示模型性能，这条曲线通过为各种阈值（确定分类结果）秒回敏感性与特异性而得到的。

AUC 指 ROC 曲线下面区域的面积，面积越大，分类器越好。包含 weka 在内的大多数工具箱都提供用于计算 AUC 分数的 API。

KDD Cup 挑战规则，参赛者胜过最基本的朴素贝叶斯分类器。

## Weka 计算 AUC

```java
import weka.classifiers.Evaluation;

// 实现朴素贝叶斯基准线
Classifier baselineNEModel = new NaiveBayes();

// 对数据做交叉验证
Evaluation eval = new Evaluation(train_data);
eval.crossValidateModel(baselineNEModel, train_data, 5, new Random(1));
result[i] = eval.areaUnderROC(train_data.classAttribute().indexOfValue("1"));
```

## Spark 计算 AUC
