__author__ = 'shi'
import numpy as np
from sklearn.linear_model import LogisticRegression
data = np.loadtxt("genotype_encode.txt")
result = np.loadtxt("phenotype.txt")
#f1 = open("phenotype.txt")
#f1.readline()
from sklearn import preprocessing
x_scaled = preprocessing.scale(data)
from sklearn.cross_validation import train_test_split
x_train, x_test, y_train, y_test = train_test_split(x_scaled, result, test_size = 0.1)
model = LogisticRegression( penalty = 'l1', tol = 0.001, max_iter = 20000)
model.fit(x_train, y_train)
answer = model.predict(x_test)
# clf2.fit(x_train,y_train)

# for m in range(len(model.feature_importances_)):
#     if model.feature_importances_[m]>0.05:
#         print "feature_importance",m,model.feature_importances_[m]

print "predict_result:",np.mean( answer == y_test)

