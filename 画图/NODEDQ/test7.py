# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test7.py
@time:2016/7/10 13:58

"""
import matplotlib.pyplot as plt
import pandas as pd
import matplotlib
d = pd.read_csv("NODEDQ_ph.csv",header = None,names = ['data','time'],sep = ',')
print d
print d.describe()
print d["data"].plot()





