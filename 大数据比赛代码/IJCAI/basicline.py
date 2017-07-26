# -*- coding: utf-8 -*-

import pandas as pd
from sklearn.preprocessing import PolynomialFeatures
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from tools import calculate_score,get_result
from features import shop_info_feature
from tools import *
shop_info=pd.read_csv("../data/shop_info_num.csv")
user_pay=pd.read_csv("../data/user_pay.txt",header=None)
user_pay.columns = ["user_id","shop_id","time"]
shop_info.rename(columns={'shopid': 'shop_id'}, inplace=True)

user_pay['date'] = user_pay.time.map(lambda x:x[:10])
del user_pay['time']

per_day_shop_user_count=user_pay.groupby(['shop_id','date'])['user_id'].count().reset_index()
#per_day_shop_user_count=pd.read_csv('per_day_shop_user_count.csv')
userId cuole
week_A=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-09-20')&
                               (per_day_shop_user_count.date <= '2016-09-26')]
week_B=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-10-11')&
                               (per_day_shop_user_count.date <= '2016-10-17')]
week_C=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-10-18')&
                               (per_day_shop_user_count.date <= '2016-10-24')]
week_D=per_day_shop_user_count[(per_day_shop_user_count.date >='2016-10-25')&
                               (per_day_shop_user_count.date <= '2016-10-31')]

# print week_A
week_A = week_A.groupby(['shop_id','date'])['user_id'].sum().unstack()
week_B = week_B.groupby(['shop_id','date'])['user_id'].sum().unstack()
week_C = week_C.groupby(['shop_id','date'])['user_id'].sum().unstack()
week_D = week_D.groupby(['shop_id','date'])['user_id'].sum().unstack()
# print week_A

##change
shop_id = pd.DataFrame(shop_info.shop_id,columns=['shop_id'])
week_A = pd.merge(shop_id,week_A.reset_index(),on='shop_id',how='outer').fillna(0)
week_B = pd.merge(shop_id,week_B.reset_index(),on='shop_id',how='outer').fillna(0)
week_C = pd.merge(shop_id,week_C.reset_index(),on='shop_id',how='outer').fillna(0)
week_D = pd.merge(shop_id,week_D.reset_index(),on='shop_id',how='outer').fillna(0)
# print week_A
poly = PolynomialFeatures(2,interaction_only=True)
shop_feature = shop_info_feature()

def statistics_features1(week1,week2,week3,weekend):
    train_x = pd.merge(week1, week2, on='shop_id')  # train = weekA + weekB
    print "week1+week2:",train_x.shape
    train_x=(pd.merge(train_x, week3, on='shop_id')).set_index('shop_id')
    print "week1+2+3:", train_x.shape
    train_sum = train_x.sum(axis=1)
    # print "train_sum",train_sum
    train_mean = train_x.mean(axis=1)
    train_max = train_x.max(axis=1)
    train_median = train_x.median(axis=1)
    train_std = train_x.std(axis=1)
    train_ratio_wk = (train_x[weekend]).sum(axis=1) / (train_sum.replace(0, 1))
    train_open_ratio_wkA = every_shop_open_ratio(start_day=447,end_day=453)
    train_open_ratio_wkB = every_shop_open_ratio(start_day=468,end_day=474)
    train_open_ratio_wkC = every_shop_open_ratio(start_day=475,end_day=481)
    train_open_ratio = (train_open_ratio_wkA.open_ratio + train_open_ratio_wkB.open_ratio+train_open_ratio_wkC)/3

    train_x = pd.DataFrame(poly.fit_transform(train_x),
                           columns=['poly_' + str(i) for i in range(232)],
                           index=train_x.index)

    train_x['sumABCD'] = train_sum
    # train_x['meanABCD'] = train_mean
    # train_x['maxABCD'] = train_max
    train_x['medianABCD'] = train_median
    # train_x['stdABCD'] = train_std
    train_x['ratio_wk'] = train_ratio_wk
    train_x['open_ratio_ABCD'] = train_open_ratio.values
    train_x = pd.merge(train_x.reset_index(), shop_feature, on='shop_id').set_index('shop_id')
    return train_x
    ####################################################
def statistics_features2(week1,week2,week3,weekend):
    train_x = pd.merge(week1, week2, on='shop_id')  # train = weekA + weekB
    print "week1+week2:",train_x.shape
    train_x=(pd.merge(train_x, week3, on='shop_id')).set_index('shop_id')
    print "week1+2+3:", train_x.shape
    train_sum = train_x.sum(axis=1)
    # print "train_sum",train_sum
    train_mean = train_x.mean(axis=1)
    train_max = train_x.max(axis=1)
    train_median = train_x.median(axis=1)
    train_std = train_x.std(axis=1)
    train_ratio_wk = (train_x[weekend]).sum(axis=1) / (train_sum.replace(0, 1))
    train_open_ratio_wkA = every_shop_open_ratio(start_day=468,end_day=474)
    train_open_ratio_wkB = every_shop_open_ratio(start_day=475,end_day=481)
    train_open_ratio_wkC = every_shop_open_ratio(start_day=482,end_day=488)
    train_open_ratio = (train_open_ratio_wkA.open_ratio + train_open_ratio_wkB.open_ratio+train_open_ratio_wkC)/3

    train_x = pd.DataFrame(poly.fit_transform(train_x),
                           columns=['poly_' + str(i) for i in range(232)],
                           index=train_x.index)

    train_x['sumABCD'] = train_sum
    # train_x['meanABCD'] = train_mean
    # train_x['maxABCD'] = train_max
    train_x['medianABCD'] = train_median
    # train_x['stdABCD'] = train_std
    train_x['ratio_wk'] = train_ratio_wk
    train_x['open_ratio_ABCD'] = train_open_ratio.values
    train_x = pd.merge(train_x.reset_index(), shop_feature, on='shop_id').set_index('shop_id')
    return train_x
    ####################################################
# ##train
# weekendAB = ['2016-09-24', '2016-09-25', '2016-10-15', '2016-10-16']
# train_x=statistics_features(week_A,week_B,weekendAB)
# train_y = week_C.set_index('shop_id')
# print "gener_train_data"
# #test
# weekendBC = ['2016-10-15', '2016-10-16', '2016-10-22', '2016-10-23']
# test_x_offline=statistics_features(week_B,week_C,weekendBC)
# test_y_offline = week_D.set_index('shop_id')
# print "gen_test_data"
# #online
# weekendCD = ['2016-10-22','2016-10-23','2016-10-29','2016-10-30']
# test_x_online=statistics_features(week_B,week_C,week_D,weekendCD)
# print "gen_online_data"

##train
weekendABC = ['2016-09-24', '2016-09-25', '2016-10-15', '2016-10-16','2016-10-22', '2016-10-23']
train_x=statistics_features1(week_A,week_B,week_C,weekendABC)
train_y = week_D.set_index('shop_id')
print train_x.shape
print train_y
print "gener_train_data"
#online
weekendBCD = ['2016-10-15', '2016-10-16','2016-10-22', '2016-10-23','2016-10-29','2016-10-30']
test_x_online=statistics_features2(week_B,week_C,week_D,weekendBCD)
print "gen_online_data"

# RF = RandomForestRegressor(n_estimators=600, random_state=1, n_jobs=-1, min_samples_split=2, min_samples_leaf=2,
#                            max_depth=25)
RF = RandomForestRegressor(n_estimators=1000,random_state=2017)
RF.fit(train_x, train_y)
pre = (RF.predict(train_x)).round()
score = calculate_score(pre, train_y.values)
print 'train:', score

# pre = (RF.predict(test_x_offline)).round()
# score = calculate_score(pre, test_y_offline.values)
# print 'test:', score

online = (RF.predict(test_x_online)).round()
result = get_result(pre)

result.to_csv('../data/result2_14_3.csv',index=False,header=False)