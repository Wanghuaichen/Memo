# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:dateframe.py
@time:2016/7/13 10:02

"""
import pandas as pd
import matplotlib as mpl
import matplotlib.pyplot as plt
import numpy as np
from test_stationarity import *
df = pd.DataFrame(pd.read_csv("zdtest.csv",sep = ',',index_col = 'datetime'))
# df.columns = ['PH','DATE']
df.index = pd.to_datetime(df.index)
# df = pd.DataFrame(df,index = df['DATE'])
# df.index = pd.to_datetime(df['date'])
# ts = df['date']
print df.index
print df.describe()
print df.dtypes
print df.shape
draw_ts(df)
draw_trend(df,10)
# draw_acf_pacf(df)
# plt.hist2d(df[0],df.index)