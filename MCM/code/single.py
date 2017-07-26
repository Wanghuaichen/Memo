import numpy as np
data = np.loadtxt("output_res(1).txt")
#f1 = open("phenotype.txt")
#f1.readline()
result = np.loadtxt("phenotype.txt")
from sklearn.ensemble import ExtraTreesClassifier
clf=ExtraTreesClassifier()
X_new=clf.fit(data,result)

print(clf.feature_importances)