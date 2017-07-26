# -*- coding:utf-8 -*-

import pandas as pd

df=pd.read_table('user_view.txt',seq=',',header=None,names=['user_id','shop_id','date'])
df.head()
# df.sample(n=5)

df.sort_values(by='date',ascending=True)[:3]
df.sort_values(by='date',ascending=False)[:3]

df['days'] = df['date'].apply(lambda x:x[:10])
new_df = df[['user_id','shop_id','days']]
new_df.head()

# count by days
pivot = new_df.pivot_table(index='days', columns='shop_id', aggfunc='count')
pivot = pivot.T
pivot.fillna(0,inplace=True)
pivot[list(column)] = pivot[list(column)].astype(float)
#pivot.dtypes change to float type
pivot.head()

pivot.to_csv('user_view_days.csv',encoding='utf-8')
