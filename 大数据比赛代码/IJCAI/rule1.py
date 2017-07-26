# -*- coding: utf-8 -*-

import pandas as pd
from sklearn.preprocessing import PolynomialFeatures
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from tools import calculate_score,get_result
from features import shop_info_feature
from tools import *
from datetime import date
user_pay=pd.read_csv("../data/user_pay.txt",header=None)
user_pay.columns = ["user_id","shop_id","time"]

user_pay['date'] = user_pay.time.map(lambda x:x[:10])
del user_pay['time']

per_day_shop_user_count=user_pay.groupby(['shop_id','date'])['user_id'].count().reset_index()
#per_day_shop_user_count=pd.read_csv('per_day_shop_user_count.csv')

week_A=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-08-01')&
                               (per_day_shop_user_count.date <= '2016-10-31')]
week_A.rename(columns={'user_id': 'pay_count'}, inplace=True)
# print week_A.head()
week_A['weekday']=week_A.date.apply(lambda x:date(int(x.split('-')[0]),int(x.split('-')[1]),int(x.split('-')[2])).weekday()+1)
week_A =  week_A[~week_A.pay_count<1]
# print week_A
shop_id = pd.DataFrame(week_A.shop_id,columns=['shop_id'])
t = week_A.groupby(['shop_id','weekday'])['pay_count'].mean().unstack()
t = pd.merge(shop_id,t.reset_index(),on='shop_id',how='outer')
t =t.drop_duplicates()
print t.head()
t = t.apply(np.round)
print t.head()
print t.shape
# df = DataFrame(t,columns=['col_'+str(i) for i in range(t.shape[1])])
t = pd.merge(t,t,on='shop_id')
print t.head()
t.to_csv('../data/result_2_18.csv',header=False,index=False)
# week_A['is_weekday'] = week_A.weekday.apply(lambda x:1 if x in (6,7) else 0)
# t1 = pd.get_dummies(week_A['weekday']).add_prefix('weekday_')
# week_A = pd.concat([week_A,t1],axis=1)
# print week_A.head()