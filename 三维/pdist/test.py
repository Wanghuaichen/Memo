__author__ = 'shi'
import matplotlib.pyplot as plt
import scipy.io as sp
#
# import pandas as pd
#
# data = pd.read_csv("data.txt",sep=' ',names=['a','b'],header=None)
# print data
# data  = data.set_index('a').unstack(level-12)
#
# print data
import numpy as np
from scipy.misc import imsave
#
# x = np.random.random((600,800,3))
# imsave('meelo.jpg', x)

import numpy as np
data =np.loadtxt('features.txt',delimiter=',')

data = data[:,:-1]
print data.shape

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
for i in range(100):
    print i
    data1 = ori_data[:,:,i]
    data2= data[i,:]
    data2 = data2.reshape([51,128])

    fig = plt.figure()
    ax = fig.add_subplot(221)

    ax.imshow(data1)
    ax = fig.add_subplot(223)
    ax.imshow(data1,cmap=plt.cm.gray)


    # print data1.shape
    # fig = plt.figure()
    ax = fig.add_subplot(222)

    ax.imshow(data2)
    ax = fig.add_subplot(224)
    ax.imshow(data2,cmap=plt.cm.gray)

    # ax = fig.add_subplot(223)
    # ax.imshow(data1,cmap=plt.cm.cool)
    # ax=fig.add_subplot(224)
    # ax.imshow(data1,cmap=plt.cm.hot)
    plt.show()