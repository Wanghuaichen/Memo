# -*- coding: utf-8 -*-
"""
Created on Wed Feb 15 08:49:19 2017

@author: Administrator
"""

import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from tools import calculate_score

shop_info = pd.read_csv('../data/shop_info_num.csv')
###################################################################
user_pay=pd.read_csv("../data/user_pay.txt",header=None)
user_pay.columns = ["user_id","shop_id","time"]
shop_info.rename(columns={'shopid': 'shop_id'}, inplace=True)

user_pay['date'] = user_pay.time.map(lambda x:x[:10])
del user_pay['time']

per_day_shop_user_count=user_pay.groupby(['shop_id','date'])['user_id'].count().reset_index()
# per_day_shop_user_count.rename(columns={'user_id': 'count'}, inplace=True)
# print per_day_shop_user_count
#per_day_shop_user_count=pd.read_csv('per_day_shop_user_count.csv')

week_A=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-09-20')&
                               (per_day_shop_user_count.date <= '2016-09-26')]
week_B=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-10-11')&
                               (per_day_shop_user_count.date <= '2016-10-17')]
week_C=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-10-18')&
                               (per_day_shop_user_count.date <= '2016-10-24')]
week_D=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-10-25')&
                               (per_day_shop_user_count.date <= '2016-10-31')]

print week_A
week_A = week_A.groupby(['shop_id','date'])['user_id'].sum().unstack()
week_B = week_B.groupby(['shop_id','date'])['user_id'].sum().unstack()
week_C = week_C.groupby(['shop_id','date'])['user_id'].sum().unstack()
week_D = week_D.groupby(['shop_id','date'])['user_id'].sum().unstack()
print week_A

##change
shop_id = pd.DataFrame(shop_info.shop_id,columns=['shop_id'])
week_A = pd.merge(shop_id,week_A.reset_index(),on='shop_id',how='outer').fillna(0)
week_B = pd.merge(shop_id,week_B.reset_index(),on='shop_id',how='outer').fillna(0)
week_C = pd.merge(shop_id,week_C.reset_index(),on='shop_id',how='outer').fillna(0)
week_D = pd.merge(shop_id,week_D.reset_index(),on='shop_id',how='outer').fillna(0)
print week_A



def shop_info():
    info = pd.read_csv('../data/shop_info_num.csv')
    info = info.sort_values('shopid')
    cate1 = pd.get_dummies(info['cate_1_name']).add_prefix('cate1_')
    cate2 = pd.get_dummies(info['cate_2_name']).add_prefix('cate2_')
    cate3 = pd.get_dummies(info['cate_3_name']).add_prefix('cate3_')
    city = pd.get_dummies(info['city_name']).add_prefix('city_')
    level = pd.get_dummies(info['shop_level']).add_prefix('level_')
    score = pd.get_dummies(info['score']).add_prefix('score_')
    res = pd.concat([cate1, cate2, cate3, city, level, score], axis=1)
    res['shopid'] = info.shopid
    return res
def train_info(weekA,weekB,weekC,weekD,weekend):
    train_x = (pd.merge(weekA,weekB,on='shop_id'))   #train = weekA + weekB
    train_x = (pd.merge(train_x,weekC,on='shop_id')).set_index('shop_id')
    train_sum = train_x.sum(axis=1)
    train_median = train_x.median(axis=1)
    train_weekend = weekend
    train_ratio_wk = (train_x[train_weekend]).sum(axis=1)/(train_sum.replace(0,1))
    from sklearn.preprocessing import PolynomialFeatures
    poly = PolynomialFeatures(2)
    cd = poly.fit_transform(train_x.head(1)).shape[1]
    train_x = pd.DataFrame(poly.fit_transform(train_x),
                          columns=['poly_' + str(i) for i in range(cd)],
                          index=train_x.index)
    train_x['sumABCD'] = train_sum
    train_x['medianABCD'] = train_median
    train_x['ratio_wk'] = train_ratio_wk
    train_y = weekD.set_index('shop_id')
    shopfeature = shop_info()
    train_x = pd.merge(train_x.reset_index(),shopfeature,on='shop_id').set_index('shop_id')
    return train_x,train_y

print '生成训练集'
train_x,train_y = train_info(week_A,week_B,week_C,week_D,['2016-09-24','2016-09-25','2016-10-15','2016-10-16','2016-10-22','2016-10-23'])
test_x,_ = train_info(week_B,week_C,week_D,week_D,['2016-10-15','2016-10-16','2016-10-22','2016-10-23','2016-10-29','2016-10-30'])

from sklearn.ensemble import ExtraTreesRegressor
ET = ExtraTreesRegressor(n_estimators=1200,random_state=1,n_jobs=-1,min_samples_split=2,min_samples_leaf=2,max_depth=25,max_features=140)
ET.fit(train_x,train_y)
pre = (ET.predict(test_x)).round()
result = pd.DataFrame(pre,index=train_info.index)
result = pd.concat([result,result],axis=1)
result.to_csv('cs1.csv',header=False)
