__author__ = 'shi'
 # -*- coding: utf-8 -*-
import numpy as np
import scipy as sp
from sklearn import tree
from sklearn.metrics import precision_recall_curve
from sklearn.metrics import classification_report
from sklearn.cross_validation import train_test_split
from sklearn.decomposition import PCA

''''' data writing '''
'''data   = []
labels = []
with open("data.txt") as ifile:
        for line in ifile:
            tokens = line.strip().split(' ')
            data.append([float(tk) for tk in tokens[:-1]])
            labels.append(tokens[-1])
x = np.array(data)
labels = np.array(labels)
y = np.zeros(labels.shape)'''
#f = open("output_res(1).txt")
#f.readline()
data = np.loadtxt("genotype_encode.txt")
newdata=np.transpose(data)
pca=PCA(n_components='mle')
newdata2=pca.fit_transform(newdata)
print np.array(newdata2).shape
# data = np.loadtxt("genotype_encode.txt")

#f1 = open("phenotype.txt")
#f1.readline()
result = np.loadtxt("phenotype.txt")
print data.shape
print result.shape
''''' change lable '''
#y[labels=='fat']=1

''''' split train data and test data '''
x_train, x_test, y_train, y_test = train_test_split(data, result, test_size = 0.25)

''''' using information shang to be the rule as split ,and train '''
# clf = tree.DecisionTreeClassifier(criterion='entropy')
clf = tree.DecisionTreeClassifier(criterion='gini')
print(clf)
clf.fit(x_train, y_train)
# print clf.feature_importances_
for m in range(len(clf.feature_importances_)):
    if clf.feature_importances_[m]>0.005:
        print "feature_importance",m,clf.feature_importances_[m]
# np.savetxt("feat_importance.txt",clf.feature_importances_)
'''''write the tree to zhe file '''
with open("tree.dot", 'w') as f:
    f = tree.export_graphviz(clf, out_file=f)

''''' Number as zhe impact of feature,bigger better'''
from sklearn.feature_selection import SelectPercentile,f_classif
selector = SelectPercentile(f_classif,percentile=1)
selector.fit(x_train, y_train)
result_dic={}
num=0
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
scores = -np.log10(selector.pvalues_)
scores /= scores.max()
scores2=[]
mark=[]
print len(scores)

for m in range(len(selector.pvalues_)):
    if selector.pvalues_[m]<0.001:
        print "pvalues:",m,selector.pvalues_[m]
'''for m in range(len(scores)):
    scores2.append(round(scores[m],2))
    if scores[m]>0.5:
        print "score"
        print scores[m]
        print m
    num = num + 1
np.savetxt("mark.txt",mark)
print scores2[2]'''

print scores.max()
score = tuple(scores)
np.savetxt("pvalue.txt",selector.pvalues_)
np.savetxt("scores.txt",scores2)
print("importance rank:")
print(scores2)

print("importance rank:")
print("##################################")
print(clf.feature_importances_)
print("##################################")
'''''print result'''
answer = clf.predict(x_test)
print("predict-result:")
print(np.mean( answer == y_test))

'''''pre and recall'''
# precision, recall, thresholds = precision_recall_curve(y_train, clf.predict(x_train))
# answer = clf.predict_proba(data)[:,1]
# print(classification_report(result, answer, target_names = ['0', '1']))