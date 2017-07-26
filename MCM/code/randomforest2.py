__author__ = 'shi'
#encoding:utf-8
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.feature_selection import SelectPercentile,f_classif
import numpy as np

f = open("genotype_encode.txt")
data = np.loadtxt(f)
# print data.transpose()[1][]
# print data.transpose()[2937]
# np.savetxt("2937.txt", data.transpose()[2937])
# l = [x[2937] for x in data]
# a=tuple(l)

data.shape
# f1 = open("phenotype.txt")
f1 = open("phenotype.txt")
result = np.loadtxt(f1)
print 'start train'
from sklearn.cross_validation import train_test_split
x_train, x_test, y_train, y_test = train_test_split(data, result, test_size = 0.25)
clf2 = RandomForestClassifier(n_estimators=500)
#clf2=GradientBoostingClassifier(f_classif,percentile=10)
clf2.fit(x_train,y_train)
for m in range(len(clf2.feature_importances_)):
    if clf2.feature_importances_[m]>0.03:
        print "feature_importance",m,clf2.feature_importances_[m]
answer = clf2.predict(x_test)
print "predict_result:",np.mean( answer == y_test)
# selector = SelectPercentile(f_classif,percentile=10)
# selector.fit(data, result)
# num=1
# for m in range(len(selector.pvalues_)):
#
#     if selector.pvalues_[m]<0.05:
#         print "pvalues",m,selector.pvalues_[m],num
#         num=num+1


# result_dic={}
# num=0
# f=open("string_data.txt")
# lines=f.readlines()
# for line in lines:
#     result_dic[line]=selector.pvalues_[num]
#     num=num+1
# num2=0
# for line in lines:
#     if result_dic[line]<0.001:
#         print "rs;",line,"pvalues:",selector.pvalues_[num2],"num:",num2
#     num2=num2+1
# scores = -np.log10(selector.pvalues_)
# scores /= scores.max()
# print(scores)
#plt.bar(X_indices - .45, scores, width=.2,
 #       label=r'Univariate score ($-Log(p_{value})$)', color='g')

#clf2 = LogisticRegression().fit(X, y)


