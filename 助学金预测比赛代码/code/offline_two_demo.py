# -*-coding:utf-8 -*-
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from datetime import date
from sklearn import cross_validation, metrics
from sklearn.grid_search import GridSearchCV
# train_test
# train_data = pd.read_table('../train/subsidy_train.txt',sep=',',header=-1)
# train_data.columns = ['id','money']
# ########################################################################
# me0 = train_data.loc[train_data.money == 0]
#
# m1000 = train_data.loc[train_data.money == 1000]
# m1500 = train_data.loc[train_data.money == 1500]
# m2000 = train_data.loc[train_data.money == 2000]
# print len(me0)
# print len(m1000)
# print len(m1500)
# print len(m2000)
# train = me0.sample(frac=0.7,random_state=2016)
# train_m1000=m1000.sample(frac=0.7,random_state=2016)
# train_m1500=m1500.sample(frac=0.7,random_state=2016)
# train_m2000=m2000.sample(frac=0.7,random_state=2016)
# train=train.append(train_m1000)
# train=train.append(train_m1500)
# train=train.append(train_m2000)
# test_pre=train_data.loc[~train_data.index.isin(train.index)]
# train.to_csv("../input/train.csv",index=False)
# test_pre.to_csv("../input/test_pre.csv",index=False)
# print 'finish'
train = pd.read_csv('../input/train.csv')
# train_1=train.loc[train.money == 0]
# train_2=train.loc[~train.index.isin(train_1.index)]
# train_1=train_1.sample(frac=0.5,random_state=2016)
# train_2=train_2.append(train_1)
# train=train_2

test_pre = pd.read_csv('../input/test_pre.csv')
test=test_pre[['id']]
test['money']=np.nan
##########################################################################
train_test = pd.concat([train,test])
# score
score_train = pd.read_table('../train/score_train.txt',sep=',',header=-1)
score_train.columns = ['id','college','score']
score_train_test = score_train

college = pd.DataFrame(score_train_test.groupby(['college'])['score'].max())
college.to_csv('../input/college.csv',index=True)
college = pd.read_csv('../input/college.csv')
college.columns = ['college','num']

score_train_test = pd.merge(score_train_test, college, how='left',on='college')
score_train_test['order'] = score_train_test['score']/score_train_test['num']
train_test = pd.merge(train_test,score_train_test,how='left',on='id')

# card
################################
# dataset3['day_of_week'] = dataset3.date_received.astype('str').apply(lambda x:date(int(x[0:4]),int(x[4:6]),int(x[6:8])).weekday()+1)
#########################################
card_train = pd.read_table('../train/card_train.txt',sep=',',header=-1)
card_train.columns = ['id','consume','where','how','time','amount','remainder']
# print card_train.shape
# card_train.drop_duplicated()
# print card_train.shape
card_train_test = card_train
# print card_train_test.shape
card_train_test=card_train_test.drop_duplicates()
# print card_train_test.shape

# t=card_train_test[['id','how','time','amount']]
# t['time']=pd.to_datetime(t['time'])
# print t
########################################
card = pd.DataFrame(card_train_test.groupby(['id'])['consume'].count())
card['consumesum'] = card_train_test.groupby(['id'])['amount'].sum()
card['consumeavg'] = card_train_test.groupby(['id'])['amount'].mean()
card['consumemax'] = card_train_test.groupby(['id'])['amount'].max()
card['consumemedian'] = card_train_test.groupby(['id'])['amount'].median()
card=card.reset_index()
####################################
# card = pd.DataFrame(card_train_test.groupby(['id'])['consume'].count())
# t=card_train_test[card_train_test.consume=='xiaofei']
# card['consumesum'] = t.groupby(['id'])['amount'].sum()
# card['consumeavg'] = t.groupby(['id'])['amount'].mean()
# card['consumemax'] = t.groupby(['id'])['amount'].max()
# card['consumemedian'] = t.groupby(['id'])['amount'].median()
# card=card.reset_index()
t=card_train_test[card_train_test.consume=='chongzhi']
print t
card_t = pd.DataFrame(t.groupby(['id'])['amount'].count()).reset_index()
# print card_t
card_t['czsum'] = t.groupby(['id'])['amount'].sum()
# card_t['consumesumcha_'+how_name[i]] = (t.groupby(['id'])['amount'].sum())/card_t['consumesum_'+how_name[i]].mean()
card_t['camean'] = t.groupby(['id'])['amount'].mean()
card_t['camax'] = t.groupby(['id'])['amount'].max()
card_t['camin'] = t.groupby(['id'])['amount'].min()
 # card_t['consumesumavg_'+how_name[i]] = t.groupby(['id'])['amount'].mean()/t['amount'].mean()
# card_t['consumemaxavg_'+how_name[i]] = t.groupby(['id'])['amount'].max()/card_t['consumemax_'+how_name[i]].mean()
card_t['czmeidian'] = t.groupby(['id'])['amount'].median()
# card_t['consumemedianavg_'+how_name[i]] = t.groupby(['id'])['amount'].median()/card_t['consumemedian_'+how_name[i]].mean()
card_t.rename(columns={'amount':'cz_count'},inplace=True)
card=pd.merge(card,card_t,on=['id'],how='left')
# ##################################
# piece_card=dict(list(card_train_test.groupby(['how'])))
# piece_card_name=list(card_train_test.groupby(['how']))
# pdf=pd.DataFrame(piece_card_name)
# print pdf


# how_name=['shitang','chaoshi']
# print piece_card,'\n'
# print piece_card_name,"asdsadsada\n"
# print "testname",piece_card_name[1],'\n'
# print piece_card[1],'\n'

# card['time'] =  pd.to_datetime(card['time'], format='%d%b%Y:%H:%M:%S.%f')


# t=card_train_test[['id','how','time','amount']]
# t=t[t.how=='shitang']
# t=t.sort('id')
# print t
# t['time_pre'] = t.time.astype('str').apply(lambda x:x[:-6])
# t=t.groupby(['id','time_pre'])['amount'].sum().reset_index()
# print t,'123'



# how_name=['shitang','qita','tushuguan','kaishui','jiaowuchu','wenyin','lingyu','chaoshi','xiyifang','xiaoche','xiaoyiyuan']
how_name=['shitang','qita','tushuguan','kaishui','lingyu','chaoshi','xiyifang']
for i in range(len(how_name)):
    t=card_train_test[['id','how','time','amount']]
    t=t[t.how==how_name[i]]
    # t['day_of_week'] = t.time.astype('str').apply(lambda x:date(int(x[0:4]),int(x[5:7]),int(x[8:10])).weekday()+1)
    # t['is_weekend'] = t.day_of_week.apply(lambda x:1 if x in (6,7) else 0)
    # t['xiaofei_zhaocan']=t.time.astype('str').apply(lambda x:0 if int(x[11:13])<=9 else 1)
    # t['time_pre'] = t.time.astype('str').apply(lambda x:x[:-6])
    # t=t.groupby(['id','time_pre'])['amount'].sum().reset_index()
    print t
    # for j in range(2):
     # t=t.loc[t.xiaofei_zhaocan == 1]
    # t1=t[t.is_weekend==i]
    card_t = pd.DataFrame(t.groupby(['id'])['amount'].count()).reset_index()
    # print card_t
    card_t['consumesum_'+how_name[i]] = t.groupby(['id'])['amount'].sum()
    # card_t['consumesumcha_'+how_name[i]] = (t.groupby(['id'])['amount'].sum())/card_t['consumesum_'+how_name[i]].mean()
    card_t['consumeavg_'+how_name[i]] = t.groupby(['id'])['amount'].mean()
    # card_t['consumesumavg_'+how_name[i]] = t.groupby(['id'])['amount'].mean()/t['amount'].mean()
    card_t['consumemax_'+how_name[i]] = t.groupby(['id'])['amount'].max()
    # card_t['consumemaxavg_'+how_name[i]] = t.groupby(['id'])['amount'].max()/card_t['consumemax_'+how_name[i]].mean()
    card_t['consumemedian_'+how_name[i]] = t.groupby(['id'])['amount'].median()
    # card_t['consumemedianavg_'+how_name[i]] = t.groupby(['id'])['amount'].median()/card_t['consumemedian_'+how_name[i]].mean()
    card_t.rename(columns={'amount':'amount_count_'+how_name[i]},inplace=True)
    card=pd.merge(card,card_t,on=['id'],how='left')
#####################################################################

# print card
# # print card


card['remaindersum'] = card_train_test.groupby(['id'])['remainder'].sum()
card['remainderavg'] = card_train_test.groupby(['id'])['remainder'].mean()
card['remaindermax'] = card_train_test.groupby(['id'])['remainder'].max()
card['remaindermedian'] = card_train_test.groupby(['id'])['remainder'].median()
card.to_csv('../input/card.csv',index=False)
card = pd.read_csv('../input/card.csv')
train_test = pd.merge(train_test, card, how='left',on='id')



train = train_test[train_test['money'].notnull()]
test = train_test[train_test['money'].isnull()]

train = train.fillna(-1)
test = test.fillna(-1)
target = 'money'
IDcol = 'id'
ids = test['id'].values
predictors = [x for x in train.columns if x not in [target]]
# predictors.remove('id')
print predictors,'\n'
print len(predictors),'\n'
# Oversample
Oversampling1000 = train.loc[train.money == 1000]
Oversampling1500 = train.loc[train.money == 1500]
Oversampling2000 = train.loc[train.money == 2000]
for i in range(5):
    train = train.append(Oversampling1000)
for j in range(8):
    train = train.append(Oversampling1500)
for k in range(10):
    train = train.append(Oversampling2000)
# model
clf = GradientBoostingClassifier(n_estimators=200,random_state=2016)
# clf = RandomForestClassifier(n_estimators=500,random_state=2016)
clf = clf.fit(train[predictors],train[target])
result = clf.predict(test[predictors])
result_p= clf.predict_log_proba(test[predictors])
# result_pre=clf.staged_predict_proba(test[predictors])
print 'pre',result_p
print 'feature_important:',clf.feature_importances_
# Save results
test_result = pd.DataFrame(columns=["id",'subsidy'])
test_result.id = ids
test_result.subsidy=result
test_result.subsidy = test_result.subsidy.apply(lambda x:int(x))
# test_result.pre=result_pre
result_compare= pd.merge(test_pre, test_result,on='id')

from sklearn.metrics import f1_score
print result_compare.money.values
print result_compare.subsidy.values
print f1_score(result_compare.money.values,result_compare.subsidy.values,average=None)
f1=list(f1_score(result_compare.money.values,result_compare.subsidy.values,average=None))
len_0=len(result_compare[result_compare.money==0])
len_1000=len(result_compare[result_compare.money==1000])
len_1500=len(result_compare[result_compare.money==1500])
len_2000=len(result_compare[result_compare.money==2000])
print '1000--'+str(len(test_result[test_result.subsidy==1000])) + ':'+str(len_1000)
print '1500--'+str(len(test_result[test_result.subsidy==1500])) + ':'+ str(len_1500)
print '2000--'+str(len(test_result[test_result.subsidy==2000])) + ':'+str(len_2000)
cum_count=len_0+len_1000+len_1500+len_2000
maroc_f1=(float(f1[1])*len_1000+float(f1[2])*len_1500+float(f1[3])*len_2000)/cum_count
print maroc_f1
result_compare.to_csv("../output/compare.csv",index=False)

# '''