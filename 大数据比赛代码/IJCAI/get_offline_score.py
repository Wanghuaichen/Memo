# -*- coding: utf-8 -*-
"""
Created on Thu Feb 09 13:48:55 2017

@author: Administrator
"""
day_time = '_02_21_3'

import pandas as pd
from pandas import DataFrame
from sklearn.ensemble import RandomForestRegressor,GradientBoostingRegressor,ExtraTreesRegressor
from sklearn.multioutput import MultiOutputRegressor
import sys
from tools import *
from sklearn.cross_validation import KFold
from sklearn.grid_search import GridSearchCV
sys.path.append('../tools')
from tools import calculate_score
from sklearn.metrics import make_scorer

train_x = pd.read_csv('../data/online/train_x'+day_time+'.csv')
train_y = pd.read_csv('../data/online/train_y'+day_time+'.csv')
test_x = pd.read_csv('../data/online/test_x'+day_time+'.csv')
# test_y = pd.read_csv('../data/online/test_y'+day_time+'.csv')
print test_x.shape
# RF = RandomForestRegressor(n_estimators=1200,random_state=1,n_jobs=-1,min_samples_split=2,min_samples_leaf=1,max_depth=25)
# RF.fit(train_x,train_y)
# pre = (RF.predict(test_x)).round()
# score = calculate_score(pre,test_y.values)

# ET = ExtraTreesRegressor(n_estimators=1000,random_state=1,n_jobs=-1,min_samples_split=2,min_samples_leaf=2,max_depth=30,max_features=150)
# ET.fit(train_x,train_y)
# pre = (ET.predict(test_x)).round()
# score = calculate_score(pre,test_y.values)
# print score
if __name__=='__main__':
    ET= MultiOutputRegressor(GradientBoostingRegressor(n_estimators=200,max_depth=2,min_samples_split=2,min_samples_leaf=2,max_features=150)).fit(train_x, train_y)
    pre = (ET.predict(test_x)).round()
    # score = calculate_score(pre,test_y.values)
    result = get_result(pre)

    result.to_csv('../data/result'+day_time+'.csv',index=False,header=False)
    #
    # for i in range(0,7):
    #     GB = GradientBoostingRegressor(n_estimators=500,learning_rate=0.05,random_state=1,subsample=0.85)
    #
    #     print train_y.icol(i)
    #     GB.fit(train_x,train_y.icol(i))
    #     pre = (GB.predict(test_x)).round()
    #
    #     result['col'+str(i)] = pre
    # result=result.values
    # score =calculate_score(result,test_y.values)
    # print "gbdt",score1