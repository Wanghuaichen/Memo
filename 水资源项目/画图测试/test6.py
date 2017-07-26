# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test6.py
@time:2016/7/7 20:28

"""

import matplotlib.pyplot as plt
import  os
import csv
import  numpy as np

dictionary = {}

direction = "NODEDQ/"
file_list = os.listdir(direction)

#遍历每个用户文件
for file_name in file_list:
         data = []
         file_path = direction+file_name
         rows = csv.reader(open(file_path,'rb'))

         for row in rows:
             data.append(float(row[0]))
         x = np.linspace(0, 30, len(data))
         plt.plot(x,data)
         plt.show()
