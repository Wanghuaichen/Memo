# -*- coding: utf-8 -*-

import pandas as pd
from sklearn.preprocessing import PolynomialFeatures

def shop_info_feature():
    info = pd.read_csv('../data/shop_info_num.csv')
    # info = info.sort_values('shopid')
    info.rename(columns={'shopid': 'shop_id'}, inplace=True)
    cate1 = pd.get_dummies(info['cate_1_name']).add_prefix('cate1_')
    cate2 = pd.get_dummies(info['cate_2_name']).add_prefix('cate2_')
    cate3 = pd.get_dummies(info['cate_3_name']).add_prefix('cate3_')
    city = pd.get_dummies(info['city_name']).add_prefix('city_')
    level = pd.get_dummies(info['shop_level']).add_prefix('level_')
    score = pd.get_dummies(info['score']).add_prefix('score_')
    res = pd.concat([cate1, cate2, cate3, city, level, score], axis=1)
    res['shop_id'] = info.shop_id
    return res

def statistics_features(week1,week2,weekend):
    poly = PolynomialFeatures(2)
    train_x = (pd.merge(week1, week2, on='shop_id')).set_index('shop_id')  # train = weekA + weekB
    train_sum = train_x.sum(axis=1)
    train_mean = train_x.mean(axis=1)
    train_max = train_x.max(axis=1)
    train_median = train_x.median(axis=1)
    train_std = train_x.std(axis=1)
    train_ratio_wk = (train_x[weekend]).sum(axis=1) / (train_sum.replace(0, 1))


    train_x = pd.DataFrame(poly.fit_transform(train_x),
                           columns=['poly_' + str(i) for i in range(120)],
                           index=train_x.index)

    train_x['sumABCD'] = train_sum
    # train_x['meanABCD'] = train_mean
    # train_x['maxABCD'] = train_max
    train_x['medianABCD'] = train_median
    # train_x['stdABCD'] = train_std
    train_x['ratio_wk'] = train_ratio_wk
    train_x = pd.merge(train_x.reset_index(), shop_feature, on='shop_id').set_index('shop_id')
    return train_x
    ####################################################

