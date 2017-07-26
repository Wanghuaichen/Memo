# -*- coding:utf-8 -*-
__author__ = 'shi'

import pandas as pd
import datetime
shop_info = pd.read_csv('../data/shop_info.txt',header=None)
shop_info.columns=['shop_id','city_name','location_id','per_pay','score','comment_cnt', 'shop_level','cate_1_name','cate_2_name','cate_3_name']
print shop_info.head()

#user view count number for per shop by day
# user_view = pd.read_csv('../data/user_view.txt',header=None)
# user_view.columns=['user_id','shop_id','time_stamp']
# user_view.time_stamp=user_view.time_stamp.astype('str')
# user_view['time']=user_view.time_stamp.apply(lambda s:s.split(' ')[0])
# user_view['year']=user_view.time_stamp.apply(lambda s:s.split('-')[0])
# user_view['month']=user_view.time_stamp.apply(lambda s:s.split('-')[1])
# user_view['day']=user_view.time_stamp.apply(lambda s:s.split(' ')[0].split('-')[2])
# user_view.drop('time_stamp',axis=1,inplace=True)
# print user_view.head()
# user_view=pd.DataFrame(user_view.groupby(['shop_id','time','year','month','day'])['user_id'].count()).reset_index()
# user_view.to_csv("../data/per_day_pre_shop_view_count.csv",index=False,header=False)
# print user_view.head()
user_view = pd.read_csv("../data/per_day_pre_shop_view_count.csv",header=None)
user_view.columns=['shop_id','time','year','mouth','day','view_count']
print user_view.shape

#user pay count number for per shop by day
# user_pay = pd.read_csv('../data/user_pay.txt',header=None)
# user_pay.columns=['user_id','shop_id','time_stamp']
# user_pay.time_stamp=user_pay.time_stamp.astype('str')
# user_pay['time']=user_pay.time_stamp.apply(lambda s:s.split(' ')[0])
# user_pay['year']=user_pay.time_stamp.apply(lambda s:s.split('-')[0])
# user_pay['month']=user_pay.time_stamp.apply(lambda s:s.split('-')[1])
# user_pay['day']=user_pay.time_stamp.apply(lambda s:s.split(' ')[0].split('-')[2])
# user_pay.drop('time_stamp',axis=1,inplace=True)
# print user_pay.head()
# user_pay=pd.DataFrame(user_pay.groupby(['shop_id','time','year','month','day'])['user_id'].count()).reset_index()
# user_pay.to_csv("../data/per_day_shop_pay_count.csv",index=False,header=False)
# print user_pay.head()
user_pay = pd.read_csv("../data/per_day_shop_pay_count.csv",header=None)
user_pay.columns=['shop_id','time','year','mouth','day','pay_count']
print user_pay.head()

#everyday view users count for per shop
# user_view2 = pd.read_csv('../data/user_view.txt',header=None)
# user_view2.columns=['user_id','shop_id','time_stamp']
# user_view2.time_stamp=user_view2.time_stamp.astype('str')
# user_view2['time']=user_view2.time_stamp.apply(lambda s:s.split(' ')[0])
# user_view2['year']=user_view2.time_stamp.apply(lambda s:s.split('-')[0])
# user_view2['month']=user_view2.time_stamp.apply(lambda s:s.split('-')[1])
# user_view2['day']=user_view2.time_stamp.apply(lambda s:s.split(' ')[0].split('-')[2])
# user_view2.drop('time_stamp',axis=1,inplace=True)
# print user_view2.shape
# print user_view2.duplicated()
# user_view2=user_view2.drop_duplicates()
# print user_view2.shape
# user_view2=pd.DataFrame(user_view2.groupby(['shop_id','time','year','month','day'])['user_id'].count()).reset_index()
# user_view2.to_csv("../data/per_day_pre_shop_view_count_dup.csv",index=False,header=False)
# print user_view2.head()
user_view2 = pd.read_csv("../data/per_day_pre_shop_view_count_dup.csv",header=None)
user_view2.columns=['shop_id','time','year','mouth','day','view_user_count']
print user_view2.head()


# #everyday pay users count for per shop
# user_pay2 = pd.read_csv('../data/user_pay.txt',header=None)
# user_pay2.columns=['user_id','shop_id','time_stamp']
# user_pay2.time_stamp=user_pay2.time_stamp.astype('str')
# user_pay2['time']=user_pay2.time_stamp.apply(lambda s:s.split(' ')[0])
# user_pay2['year']=user_pay2.time_stamp.apply(lambda s:s.split('-')[0])
# user_pay2['month']=user_pay2.time_stamp.apply(lambda s:s.split('-')[1])
# user_pay2['day']=user_pay2.time_stamp.apply(lambda s:s.split(' ')[0].split('-')[2])
# user_pay2.drop('time_stamp',axis=1,inplace=True)
# print user_pay2.shape
# print user_pay2.duplicated()
# user_pay2=user_pay2.drop_duplicates()
# print user_pay2.shape
# user_pay2=pd.DataFrame(user_pay2.groupby(['shop_id','time','year','month','day'])['user_id'].count()).reset_index()
# user_pay2.to_csv("../data/per_day_shop_pay_count_dup.csv",index=False,header=False)
# print user_pay2.head()
user_pay2=pd.read_csv("../data/per_day_shop_pay_count_dup.csv",header=None)
user_pay2.columns=['shop_id','time','year','mouth','day','pay_user_count']
user_pay2.head()

##########################################

# del user_pay
# del user_pay2
# del user_view
# del user_view2

user_pay_all = pd.merge(user_pay,user_pay2,on=['shop_id','time','year','mouth','day'],how='left')
user_view_all= pd.merge(user_view,user_view2,on=['shop_id','time','year','mouth','day'],how='left')
# user_pay_all.to_csv("../data/user_pay_all.csv",header=False,index=False)
# user_view_all.to_csv("../data/user_view_all.csv",header=False,index=False)
shop_id = user_pay_all['shop_id']
shop_id = shop_id.drop_duplicates()
##############################################################
train_all =pd.merge(user_pay_all,user_view_all,on=['shop_id','time','year','mouth','day'],how='left')
train_all = pd.merge(train_all,shop_info,on=['shop_id'],how='left')

# # 输出含缺失值的汇总文件
# # train_all_2.to_csv("../../data//dataset/feature/train_all_null.txt",header=False,index=False)
#
# #缺失值处理  view_count
# #查看缺失值
# train_all_2[pd.isnull(train_all_2["view_count"])].head()
# #view_count 填充为0
train_all["view_count"]=train_all["view_count"].fillna(0)  #填充缺失值
# #view_user_count
train_all["view_user_count"]=train_all["view_user_count"].fillna(0)  #填充缺失值
# #score  评分 填充为均值？ 某类均值？ round(a,1) 保留小数点后1位
tmp=round(train_all[-pd.isnull(train_all["score"])]["score"].mean(),1)
train_all["score"]=train_all["score"].fillna(tmp)
# #comment_cnt 评论数 填充为均值？ 某门店等级一级品类名称二级分类名称的均值？
tmp=round(train_all[-pd.isnull(train_all["comment_cnt"])]["comment_cnt"].mean(),1)
train_all["comment_cnt"]=train_all["comment_cnt"].fillna(tmp)
# #cate_3_name 三级分类名称 标记为缺失
tmp="缺失"
train_all["cate_3_name"]=train_all["cate_3_name"].fillna(tmp)
#
# #保存结果
train_all.to_csv("../data/train_all.txt",header=False,index=False)
print train_all.head()


##########################################################

#
# #处理天气
# weather = pd.read_csv("../../data/dataset/feature/ijcai17-wheather.csv",header=None)
# weather.columns=['city_name','date','topTemp','lowTemp','weather','wind','windStrength','no']
# date_time=weather['date']
# time=[]
# for dt in date_time:
#   time.append(int(dt[0:4]+dt[5:7]+dt[8:10]))
# time=pd.DataFrame({'time':time})
# weather=pd.concat([weather[['city_name','date','topTemp','lowTemp','weather','wind','windStrength']],time],axis=1)
#
# train_all_2=pd.merge(train_all_2,weather,on=['city_name','time'],how='left')
# train_all_2=train_all_2[['shop_id','time','year','mouth','day','pay_count','pay_user_count','view_count','view_user_count','city_name','location_id','per_pay','score','comment_cnt','shop_level','cate_1_name','cate_2_name','cate_3_name','topTemp','lowTemp','weather','wind','windStrength']]
#
# train_all.to_csv("../data/train_all_weather.txt",header=False,index=False)