#-*-coding:utf-8-*-
"""
将tianchi_mobile_recommend_train_user.csv按照日期分割为31份**.csv文件，放在'/data/date/'目录下。
生成的**.csv文件内容格式如下：

user_id, item_id, behavior_type,user_geohash,item_category,    hour
99512554,37320317,  3,            94gn6nd,    9232,             20

"""

import csv
import os

words = []
last_h = 0
last_dta = ""
f = open("NODEDQ/NODEDQ_alz.csv",'rb')
rows = csv.reader(f)
rows.next()
for row in rows:
    data = row[1].split(" ")[0]
    time = row[1].split(" ")[1]
    h = time.split(":")[0]
    if int(h) == last_h and data == last_dta:
        continue
    tt = (float(row[0]),data,h)
    words.append(tt)
    last_dta = data
    last_h = int(h)
f.close()

f = open("NODEDQ/alztest.csv","wb")
write = csv.writer(f)
write.writerow(["date","data","time"])
total = 0
for key in words:
    write.writerow(key)
    total += 1
print "generate submission file,total %d  (uid,iid)" %total
f.close()
