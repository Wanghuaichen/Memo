# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test2.py
@time:2016/7/7 18:56

"""
import numpy as np
import matplotlib.pyplot as plt
import os
import csv



dictionary = {}
direction = "NODEDQ/"
file_list = os.listdir(direction)

#遍历每个用户文件
plt.figure(1)
x = np.linspace(0,43,1000)
for file_name in file_list:
	file_path = direction+file_name
	rows = csv.reader(open(file_path,'rb'))
	rows.next()

	for row in rows:
            if row[0]!="2014-12-18":#########缩进有问题
		 uid = file_name.split('.')[0]
		if not dictionary.has_key(uid):
		    dictionary[uid]=[0,0,0,0]
		dictionary[uid][int(row[2])-1] += 1
	#过滤掉加购比大于0.05的用户
	gg=dictionary[uid][1]+dictionary[uid][2]
	ggb=dictionary[uid][3]/(float(gg)+1)
	if ggb>0.05 and dictionary[uid][3]==0:
		del dictionary[uid]




f = open("../data/dictionary/uidfeature.pkl",'wb')
cPickle.dump(dictionary,f,-1)
f.close()

#########################################################
plt.figure(1) # 创建图表1

#plt.figure(2) # 创建图表2

#ax1 = plt.subplot(211) # 在图表2中创建子图1

#ax2 = plt.subplot(212) # 在图表2中创建子图2

x = np.linspace(0,43,1000)

for i in xrange(5):

    plt.figure(1)  #❶ # 选择图表1

    plt.plot(x, np.sin(i*x))

   # plt.sca(ax1)   #❷ # 选择图表2的子图1

    #plt.plot(x, np.sin(i*x))

    #plt.sca(ax2)  # 选择图表2的子图2

    #plt.plot(x, np.cos(i*x))
#
plt.show()
