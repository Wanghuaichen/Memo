# -*- coding:utf-8 -*-
__author__ = 'shi'
from datetime import date
import pandas as pd
from tools import *
from sklearn.ensemble import RandomForestRegressor
from sklearn import preprocessing
import  datetime

# train_all = pd.read_csv("../data/train_all.txt",header=None)
# train_all.columns=['shop_id','time','year','mouth','day','pay_count','pay_user_count','view_count','view_user_count','city_name','location_id','per_pay','score','comment_cnt','shop_level','cate_1_name','cate_2_name','cate_3_name']
#
# # print train_all.head()
#
# #标签类别化
# tmp=train_all["city_name"]
# le = preprocessing.LabelEncoder()
# #将类别转变成编码类型
# le.fit(tmp)
# #list(le.classes_)
# # for i in le.classes_:
# #     print i
# #将类别序列转变为编码
# tmp=le.transform(tmp)
# # list(le.inverse_transform([0, 2, 1]))
# train_all['city_name']=tmp
# # print train_all.head()
#
# tmp=train_all["cate_1_name"]
# le.fit(tmp)
# tmp=le.transform(tmp)
# train_all['cate_1_name']=tmp
#
# tmp=train_all["cate_2_name"]
# le.fit(tmp)
# tmp=le.transform(tmp)
# train_all['cate_2_name']=tmp
#
# tmp=train_all["cate_3_name"]
# le.fit(tmp)
# tmp=le.transform(tmp)
# train_all['cate_3_name']=tmp
#
# train_all.to_csv('../data/train_all_del_chinese.txt',header=False,index=False)

# train_all = pd.read_csv("../data/train_all_del_chinese.txt",header=None)
# train_all.columns=['shop_id','time','year','mouth','day','pay_count','pay_user_count','view_count','view_user_count','city_name','location_id','per_pay','score','comment_cnt','shop_level','cate_1_name','cate_2_name','cate_3_name']
# # print train_all.shape
# train_all=train_all[train_all.year>2015]
# train_all = train_all[train_all.mouth>7]
# # print train_all.head()
# # train_all=train_all.sort_values('shop_id')
# cate1 = pd.get_dummies(train_all['cate_1_name']).add_prefix('cate1_')
# cate2 = pd.get_dummies(train_all['cate_2_name']).add_prefix('cate2_')
# cate3 = pd.get_dummies(train_all['cate_3_name']).add_prefix('cate3_')
# city = pd.get_dummies(train_all['city_name']).add_prefix('city_')
# level = pd.get_dummies(train_all['shop_level']).add_prefix('level_')
# score = pd.get_dummies(train_all['score']).add_prefix('score_')
# res = pd.concat([cate1, cate2, cate3, city, level, score], axis=1)
# print res.head()
# # res['shop_id'] = train_all.shop_id
# # print res.head()
# del cate1
# del cate2
# del cate3
# del city
# del level
# del score
# del train_all['cate_1_name']
# del train_all['cate_2_name']
# del train_all['cate_3_name']
# del train_all['city_name']
# del train_all['shop_level']
# del train_all['score']
# del train_all['year']
# del train_all['mouth']
# train_all =pd.concat([train_all,res],axis=1)
# train_all.to_csv("../data/train_all_onhot.txt",header=True,index=False)
# print train_all.head()

# train_all=pd.read_csv("../data/train_all_onhot.txt",header=0)
# print train_all.head()
# print train_all.shape
# #weekend onhot add month lable
# t = train_all[['shop_id','time','day']]
# t['weekday']=t.time.apply(lambda x:date(int(x.split('-')[0]),int(x.split('-')[1]),int(x.split('-')[2])).weekday()+1)
# # t['is_weekday'] = t.weekday.apply(lambda x:1 if x in (6,7) else 0)
# t['month_bin'] = t.day.astype('str').apply(lambda x:int((int(x)-1)/10)+1)
# t1 = pd.get_dummies(t['weekday']).add_prefix('weekday_')
# print t1.head()
# t=pd.concat([t,t1],axis=1)
# del t['shop_id']
# del t['time']
# del t['day']
# del t['weekday']
# train_all = pd.concat([train_all,t],axis=1)
# print train_all.head()
# print train_all.shape
# del t
# # print '2'
# train_all=train_all.drop_duplicates()
# train_all.to_csv('../data/train_all_add_week_month.txt',header=True,index=False)
train_all = pd.read_csv('../data/train_all_add_week_month.txt',header=0)
print train_all.head(),'12'
#########################################################
#statistic


# print t.head()
# def get_day_gap_before1(s):
#     year,month,day = s.split('-')
#     myday = datetime.datetime( int(year),int(month),int(day) ) + datetime.timedelta(days=-7)
#     dt = myday.strftime('%Y-%m-%d')
#     return dt
# t1=t[t.time>t.time.apply(get_day_gap_before1)].agg('sum')
# train_all =pd.concat([train_all,t],axis=1)
# print t1.head()
def get_train_x(tdf,week1,month1,month2,month3,end):
    train_x=tdf[['shop_id','time']]
    train_week_y=tdf[(tdf.time >=week1) & (tdf.time <= end)]
    train_month_helfy=tdf[(tdf.time >=month1) & (tdf.time <= end)]
    train_month_y=tdf[(tdf.time >=month2) & (tdf.time <= end)]
    train_month_2y=tdf[(tdf.time >=month3) & (tdf.time <= end)]
    # print '1',train_week_y.head()
    t1=pd.DataFrame(train_week_y.groupby(['shop_id'])['pay_count'].mean()).reset_index()
    t1.rename(columns={'pay_count': 'pay_count1'}, inplace=True)
    t1['pay_count_median_w']=train_week_y.groupby(['shop_id'])['pay_count'].median()
    t1['pay_count_std_w'] = train_week_y.groupby(['shop_id'])['pay_count'].std()
    t1['pay_user_count_meadian_w'] = train_week_y.groupby(['shop_id'])['pay_user_count'].median()
    t1['pay_user_count_mean_w'] = train_week_y.groupby(['shop_id'])['pay_user_count'].mean()
    t1['pay_user_count_std_w'] = train_week_y.groupby(['shop_id'])['pay_user_count'].std()
    t1['view_count_meadian_w'] = train_week_y.groupby(['shop_id'])['view_count'].median()
    t1['view_count_mean_w'] = train_week_y.groupby(['shop_id'])['view_count'].mean()
    t1['view_count_std_w'] = train_week_y.groupby(['shop_id'])['view_count'].std()
    t1['view_user_count_meadian_w'] = train_week_y.groupby(['shop_id'])['view_user_count'].median()
    t1['view_user_count_mean_w'] = train_week_y.groupby(['shop_id'])['view_user_count'].mean()
    t1['view_user_count_std_w'] = train_week_y.groupby(['shop_id'])['view_user_count'].std()
    # print t1
    train_x = pd.merge(train_x,t1,on=['shop_id'],how='left')

    t1=pd.DataFrame(train_month_helfy.groupby(['shop_id'])['pay_count'].mean()).reset_index()
    t1.rename(columns={'pay_count': 'pay_count2'}, inplace=True)
    t1['pay_count_medianm1']=train_month_helfy.groupby(['shop_id'])['pay_count'].median()
    t1['pay_count_stdm1'] = train_month_helfy.groupby(['shop_id'])['pay_count'].std()
    t1['pay_user_count_meadianm1'] = train_month_helfy.groupby(['shop_id'])['pay_user_count'].median()
    t1['pay_user_count_meanm1'] = train_month_helfy.groupby(['shop_id'])['pay_user_count'].mean()
    t1['pay_user_count_stdm1'] = train_month_helfy.groupby(['shop_id'])['pay_user_count'].std()
    t1['view_count_meadianm1'] = train_month_helfy.groupby(['shop_id'])['view_count'].median()
    t1['view_count_meanm1'] = train_month_helfy.groupby(['shop_id'])['view_count'].mean()
    t1['view_count_stdm1'] = train_month_helfy.groupby(['shop_id'])['view_count'].std()
    t1['view_user_count_meadianm1'] = train_month_helfy.groupby(['shop_id'])['view_user_count'].median()
    t1['view_user_count_meanm1'] = train_month_helfy.groupby(['shop_id'])['view_user_count'].mean()
    t1['view_user_count_stdm1'] = train_month_helfy.groupby(['shop_id'])['view_user_count'].std()
    train_x = pd.merge(train_x,t1,on=['shop_id'],how='left')

    t1=pd.DataFrame(train_month_y.groupby(['shop_id'])['pay_count'].mean()).reset_index()
    t1.rename(columns={'pay_count': 'pay_count3'}, inplace=True)
    t1['pay_count_medianm2']=train_month_y.groupby(['shop_id'])['pay_count'].median()
    t1['pay_count_stdm2'] = train_month_y.groupby(['shop_id'])['pay_count'].std()
    t1['pay_user_count_meadianm2'] = train_month_y.groupby(['shop_id'])['pay_user_count'].median()
    t1['pay_user_count_meanm2'] = train_month_y.groupby(['shop_id'])['pay_user_count'].mean()
    t1['pay_user_count_stdm2'] = train_month_y.groupby(['shop_id'])['pay_user_count'].std()
    t1['view_count_meadianm2'] = train_month_y.groupby(['shop_id'])['view_count'].median()
    t1['view_count_meanm2'] = train_month_y.groupby(['shop_id'])['view_count'].mean()
    t1['view_count_stdm2'] = train_month_y.groupby(['shop_id'])['view_count'].std()
    t1['view_user_count_meadianm2'] = train_month_y.groupby(['shop_id'])['view_user_count'].median()
    t1['view_user_count_meanm2'] = train_month_y.groupby(['shop_id'])['view_user_count'].mean()
    t1['view_user_count_stdm2'] = train_month_y.groupby(['shop_id'])['view_user_count'].std()
    train_x = pd.merge(train_x,t1,on=['shop_id'],how='left')

    t1=pd.DataFrame(train_month_2y.groupby(['shop_id'])['pay_count'].mean()).reset_index()
    t1.rename(columns={'pay_count': 'pay_count4'}, inplace=True)
    t1['pay_count_medianm3']=train_month_2y.groupby(['shop_id'])['pay_count'].median()
    t1['pay_count_stdm3'] = train_month_2y.groupby(['shop_id'])['pay_count'].std()
    t1['pay_user_count_meadianm3'] = train_month_2y.groupby(['shop_id'])['pay_user_count'].median()
    t1['pay_user_count_meanm3'] = train_month_2y.groupby(['shop_id'])['pay_user_count'].mean()
    t1['pay_user_count_stdm3'] = train_month_2y.groupby(['shop_id'])['pay_user_count'].std()
    t1['view_count_meadianm3'] = train_month_2y.groupby(['shop_id'])['view_count'].median()
    t1['view_count_meanm3'] = train_month_2y.groupby(['shop_id'])['view_count'].mean()
    t1['view_count_stdm3'] = train_month_2y.groupby(['shop_id'])['view_count'].std()
    t1['view_user_count_meadianm3'] = train_month_2y.groupby(['shop_id'])['view_user_count'].median()
    t1['view_user_count_meanm3'] = train_month_2y.groupby(['shop_id'])['view_user_count'].mean()
    t1['view_user_count_stdm3'] = train_month_2y.groupby(['shop_id'])['view_user_count'].std()
    train_x = pd.merge(train_x,t1,on=['shop_id'],how='left')
    del train_x['time']
    # print train_x.shape
    return train_x

tdf = train_all[['shop_id','time','pay_count','pay_user_count','view_count','view_user_count']]
train_x = get_train_x(tdf,'2016-09-27','2016-09-18','2016-09-03','2016-08-03','2016-10-03')
train_x_all = pd.concat([train_all,train_x],axis=1)
# print 'sdsdfst',train_x_all.head()
train_x_all=train_x_all[(train_x_all.time>='2016-10-04')&(train_x_all.time<='2016-10-17')]
# print train_x_all.head()
# print train_x_all.shape
train_x_all = train_x_all.drop_duplicates()
print 'train_x:',train_x_all
# shop_id = train_x_all[['shop_id','time']]
train_y=train_x_all['pay_count']
# del train_x_all['pay_count']
del train_x_all['pay_user_count']
del train_x_all['view_count']
del train_x_all['view_user_count']
# del train_x_all['time']
del train_x_all['day']
train_x_all = train_x_all.fillna(0)
train_y = train_y.fillna(0)
print train_x_all.shape
print train_x_all
print train_y.shape
print train_y

train_x_all=train_x_all.reset_index()
print train_x_all
# train_x_all.to_csv('../data/check_train_X.txt',header=True,index=False)
# train_y.to_csv('../data/check_train_y.txt',header=True,index=False)
for i in range(1,2000):
    train = train_x_all.ix[:,train_x_all.shop_id==i].as_matrix()
    y=train['pay_count']
    shop_id = train[['shop_id','time']]
    del train['pay_count']
    del train['shop_id']
    del train['time']
    RF = RandomForestRegressor(n_estimators=500,random_state=2017)
    RF.fit(train, y)
    pre = (RF.predict(train)).round()
    pre = pd.concat([shop_id,pre])
    pre = pre.groupby(['shop_id','time']).unstack().reset_index()
    print pre
score = calculate_score(pre, train_y.values)
print 'train:', score
# train_y =train_all[train_all.time>='2016-10-04'& train_all.time<='2016-10-17']
# test_x = get_train_x(t,'2016-10-11','2016-10-02','2016-09-17','2016-08-17','2016-10-17')
# print train_x_all.head()
# test_x_online = get_train_x(t,'2016-10-25','2016-10-16','2016-09-31','2016-08-31','2016-10-31')
# print test_x_online.head()

#
# t1=pd.DataFrame(train_month_helfy.groupby(['shop_id'])['pay_count'].mean())
# t1['pay_count_median']=train_month_helfy.groupby(['shop_id'])['pay_count'].median()
# t1['pay_count_std'] = train_month_helfy.groupby(['shop_id'])['pay_count'].std()
# t1['pay_user_count_meadian'] = train_month_helfy.groupby(['shop_id'])['pay_user_count'].median()
# t1['pay_user_count_mean'] = train_month_helfy.groupby(['shop_id'])['pay_user_count'].mean()
# t1['pay_user_count_std'] = train_month_helfy.groupby(['shop_id'])['pay_user_count'].std()
# t1['view_count_meadian'] = train_week_y.groupby(['shop_id'])['view_count'].median()
# t1['view_count_mean'] = train_week_y.groupby(['shop_id'])['view_count'].mean()
# t1['view_count_std'] = train_week_y.groupby(['shop_id'])['view_count'].std()
# t1['view_user_count_meadian'] = train_week_y.groupby(['shop_id'])['view_user_count'].median()
# t1['view_user_count_mean'] = train_week_y.groupby(['shop_id'])['view_user_count'].mean()
# t1['view_user_count_std'] = train_week_y.groupby(['shop_id'])['view_user_count'].std()


# week_B=t[(t.time >='2016-10-11') & (t.time <= '2016-10-17')]
# week_C=t[(t.time >='2016-10-18') & (t.time <= '2016-10-24')]
# week_D=t[(t.time >='2016-10-25') & (t.time <= '2016-10-31')]
# week_A=pd.concat([week_A,week_B],axis=0)
# week_A=pd.concat([week_A,week_C],axis=0)
# week_A=pd.concat([week_A,week_D],axis=0)
# print train_all.head()
# train_all.to_csv("../data/train_all_onhot.txt",header=True,index=False)