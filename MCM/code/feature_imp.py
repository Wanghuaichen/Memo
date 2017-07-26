__author__ = 'shi'
# Feature Importance
import numpy as np
from sklearn import datasets
from sklearn import metrics
from sklearn.ensemble import ExtraTreesClassifier
data = np.loadtxt("output_res(1).txt")
#f1 = open("phenotype.txt")
#f1.readline()
result = np.loadtxt("phenotype.txt")
print data.shape
print result.shape
# fit an Extra Trees model to the data
from sklearn.cross_validation import train_test_split
x_train, x_test, y_train, y_test = train_test_split(data, result, test_size = 0.3)
model = ExtraTreesClassifier(n_estimators=200)
model.fit(x_train,y_train)
answer = model.predict(x_test)
print "predict_result:",np.mean( answer == y_test)
# display the relative importance of each attribute
for m in range(len(model.feature_importances_)):
    if model.feature_importances_[m]>0.0005:
        print "feature_importance",m,model.feature_importances_[m]