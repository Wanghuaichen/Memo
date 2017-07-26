
import scipy.io as sp
import numpy as np
from matplotlib import pyplot as plt
# dataFile = './data/data.mat'
#
# data = sp.loadmat(dataFile)
import os
# print data['HksHistDesc'][:,:,70]
# ttt = np.array([[1,2,3,],[2,3,4]])
# mean = ttt.mean(axis=1).reshape([2,1])
# std = ttt.std(axis=1).reshape([2,1])
# print mean,'\n',std,'\n'
#
# new_ttt = (ttt-mean)/std
# print new_ttt
# final_data = np.zeros([51, 128, 500])
# for i in range(50):
#     new_data = data['HksHistDesc'][:,:,10*i]
#     for j in range(1,10):
#         new_data = np.hstack((new_data, data['HksHistDesc'][:,:,10*i+j]))
#     mean = new_data.mean(axis=1).reshape([51,1])
#     std = new_data.std(axis=1).reshape([51,1])
#     new_data = (new_data-mean)/(std)
#     for k in range(10):
#         final_data[:,:,i*10+k]=new_data[:,k*128:k*128+128]
# print final_data.shape


# compute(os.getcwd() +'/data/feature_hksl2mean5000.txt')








# print data['C']
# dict = {}
# for i in range(128):
#     tmp = np.zeros((51,500))
#     for j in range(500):
#         tmp[:,j] = data['HksHistDesc'][:,i,j]
#     dict[i] =tmp.T
# print dict[1].shape


import pandas as pd

data =np.loadtxt('hksmean130.csv',delimiter=',')
print data[0,:131]
plt.figure(1)
plt.title('HKS/Time Curve')
plt.xlim(0,130)
plt.ylim(0,0.35)
plt.xlabel('Time')
plt.ylabel('HKS Vlaues')
x = data[1,:131]
plt.plot(x)
plt.savefig('HKS.png')
plt.show()
