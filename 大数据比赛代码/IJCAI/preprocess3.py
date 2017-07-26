# -*- coding:utf-8 -*-
__author__ = 'shi'

import pandas as pd
from datetime import date
train_all = pd.read_csv("../data/train_all_onhot.txt")
print train_all.head()
# train_all.columns=['shop_id','time','year','month','day','pay_count','pay_user_count','view_count','view_user_count','city_name','location_id','per_pay','score','comment_cnt','shop_level','cate_1_name','cate_2_name','cate_3_name']

t = train_all[['shop_id','time','day']]
t['weekday']=t.time.astype('str').apply(lambda x:date(int(x.split('-')[0]),int(x.split('-')[1]),int(x.split('-')[2])).weekday()+1)
t['is_weekday'] = t.weekday.apply(lambda x:1 if x in (6,7) else 0)
t['month_bin'] = t.day.astype('str').apply(lambda x:int((int(x)-1)/10)+1)
t1 = pd.get_dummies(t['is_weekday']).add_prefix('weekday_')
print t1.head()
t=pd.concat([t,t1],axis=1)
print t.head()
# weekday onehot
# 各种统计量
# 月末月初月中
# 再把0.85那个放进去
# t['weekday'] =
# print t.head()