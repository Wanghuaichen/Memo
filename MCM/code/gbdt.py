__author__ = 'shi'
from sklearn.ensemble import GradientBoostingClassifier
import numpy as np
from sklearn.svm import SVC
# model = SVC(kernel='rbf', probability=True)

# data = np.loadtxt("output_res(1).txt")
dat = np.loadtxt("genotype_encode.txt")
# data=np.transpose(dat)
#f1 = open("phenotype.txt")
#f1.readline()
result = np.loadtxt("phenotype.txt")
print result.shape
from sklearn.cross_validation import train_test_split
x_train, x_test, y_train, y_test = train_test_split(dat, result, test_size = 0.2)
clf2=GradientBoostingClassifier(loss = 'deviance',n_estimators=1000,learning_rate=0.01,max_depth=20)
# clf2.fit(x_train,y_train)
clf2.fit(x_train, y_train)
# for m in range(len(model.feature_importances_)):
#     if model.feature_importances_[m]>0.05:
#         print "feature_importance",m,model.feature_importances_[m]
answer = clf2.predict(x_test)
print "predict_result:",np.mean( answer == y_test)