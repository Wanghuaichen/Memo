# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test8.py
@time:2016/7/10 19:34


"""

import matplotlib.pyplot as plt
import  os
import csv
import  numpy as np
from test_stationarity import draw_trend
import pandas as pd
dictionary = {}

direction = "NODEDQ/"


#遍历每个用户文件
data = []
time = []
file_path = "ddltest.csv"
rows = csv.reader(open(file_path,'rb'))
rows.next()
num = 0
for row in rows:
    # if num == 0:
    #     last_data = float(row[0])
    # if float(row[0]) >400 or float(row[0])<40 :
    #     data.append(last_data)
    # else:
    data.append(float(row[0]))
    last_data = float(row[0])
    time.append(row[1])
    num+=1
# x = np.linspace(0, 42, len(data))
# plt.plot(x,data)
# plt.show()
# present = pd.read_csv(file_path,sep = ',')
# print present.shape
# print present.columns
# present_day = present.set_index("data")
# present_day['date'].plot()
# plt.legend(loc = 'best')
# present_day.plot()
# present_day.date.plot(color='g')
# plt.legend(loc = 'best')
# present_day[:10].plot(kind = 'bar')
series_data = pd.Series(data)
draw_trend(series_data,10)
from test_stationarity import draw_ts
draw_ts(series_data)
from test_stationarity import testStationarity
testStationarity(series_data)
from test_stationarity import draw_acf_pacf
# draw_acf_pacf(series_data)