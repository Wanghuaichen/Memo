# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test_stationarity.py
@time:2016/7/11 12:07

"""
# -*- coding:utf-8 -*-
from statsmodels.tsa.stattools import adfuller,ccf
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from statsmodels.graphics.tsaplots import plot_acf,plot_pacf

# 移动平均图
def draw_trend(timeSeries, size):
    f = plt.figure(facecolor='white')
    ax3 = f.add_subplot(211)
    timeSeries.plot(color='blue',ax=ax3)
    plt.title("Original Data")
    # plt.ylim(0,0.6)
    plt.ylabel("turbidity(NTU)")
    plt.xlabel("TIME")
    # plt.xlim(300,500)
    # plt.ylabel("Conductivity(us/cm)")
    # ax1 = f.add_subplot(312)
    # # 对size个数据进行移动平均
    # rol_mean = timeSeries.rolling(window=size).mean()
    # rol_mean.plot(color='red', label='rolling Mean',ax=ax1)
    # plt.legend(loc='best')
    # plt.title('Mean Smooth')
    # # plt.xlabel("Hour")
    # #plt.xlim(45,200)
    # plt.ylabel("Conductivity(us/cm)")
    # # plt.ylim(0,12)
    # # 对size个数据进行加权移动平均
    ax2 = f.add_subplot(212)
    rol_weighted_mean = pd.ewma(timeSeries, span=size)
    # timeSeries.plot(color='blue', label='Original')


    rol_weighted_mean.plot(color='red', label='Weighted Rolling Mean',ax=ax2)
    plt.legend(loc='best')
    # plt.title('Weight Smooth')
    plt.xlabel("TIME")
    # plt.ylim(0,0.6)
    plt.ylabel("turbidity(NTU)")
    # plt.ylim(0,12)
    plt.show()

def draw_ts(timeSeries):
    f = plt.figure(facecolor='white')
    timeSeries.plot(color='blue')
    plt.title("Original Data")
    # plt.ylim(0,450)
    plt.xlabel("DATE")
    # plt.ylim(0,0.6)
    # plt.xscale(0,100)
    plt.ylabel("turbidity(NTU)")
    plt.show()

'''
　　Unit Root Test
   The null hypothesis of the Augmented Dickey-Fuller is that there is a unit
   root, with the alternative that there is no unit root. That is to say the
   bigger the p-value the more reason we assert that there is a unit root
'''
def testStationarity(ts):
    dftest = adfuller(ts)
    # 对上述函数求得的值进行语义描述
    dfoutput = pd.Series(dftest[0:4], index=['Test Statistic','p-value','#Lags Used','Number of Observations Used'])
    for key,value in dftest[4].items():
        dfoutput['Critical Value (%s)'%key] = value
    return dfoutput

# 自相关和偏相关图，默认阶数为12阶
def draw_acf_pacf(ts, lags=12):
    f = plt.figure(facecolor='white')
    ax1 = f.add_subplot(211)
    plot_acf(ts, lags=31, ax=ax1)
    ax2 = f.add_subplot(212)
    plot_pacf(ts, lags=31, ax=ax2)
    plt.show()

def draw_ccf(ts,ts1):
    ay = np.array(ts)
    print ay.shape
    ay1 = np.array(ts1)
    print(ay1.shape)
    testccf= ccf(ay,ay1)
    print testccf