__author__ = 'shi'
import numpy as np
import matplotlib.pyplot as plt
from sklearn.metrics import confusion_matrix, roc_curve, auc, accuracy_score
from sklearn.cross_validation import train_test_split
import os
import pandas as pd
import random
import scipy.io as sp
from sklearn.preprocessing import normalize
from sklearn import preprocessing
# normalize the data attributes
# normalized_X = preprocessing.normalize(X)
# # standardize the data attributes
# standardized_X = preprocessing.scale(X)
# print os.getcwd()
# get the data


dataFile = 'data.mat'

input_data = sp.loadmat(dataFile)

ori_data = np.zeros([51, 128, 500])
for i in range(50):
    new_data = input_data['HksHistDesc'][:,:,10*i]
    for j in range(1,10):
        new_data = np.hstack((new_data, input_data['HksHistDesc'][:,:,10*i+j]))
    mean = new_data.mean(axis=1).reshape([51,1])
    std = new_data.std(axis=1).reshape([51,1])
    new_data = (new_data-mean)/(std)
    for k in range(10):
        ori_data[:,:,i*10+k]=new_data[:,k*128:k*128+128]

print ori_data.shape
for i in range(100):
    data = ori_data[:,:,i]
    fig = plt.figure()
    ax = fig.add_subplot(221)

    ax.imshow(data)
    ax = fig.add_subplot(222)
    ax.imshow(data,cmap=plt.cm.gray)

    ax = fig.add_subplot(223)
    ax.imshow(data,cmap=plt.cm.cool)
    ax=fig.add_subplot(224)
    ax.imshow(data,cmap=plt.cm.hot)
    plt.show()
##############test###################
# print ori_data['HksHistDesc'][:,:,1]
# for i in range(500):
#     np.savetxt('./data/data/'+str(i)+'.txt',ori_data['HksHistDesc'][:,:,i],delimiter=' ')
