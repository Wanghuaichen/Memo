
#挨个执行以下程序段

## param
# param_test1 = {'n_estimators':range(100,400,20)}
# gsearch1 = GridSearchCV(estimator = GradientBoostingClassifier(learning_rate=0.1, min_samples_split=300,
#                                   min_samples_leaf=20,max_depth=8,max_features='sqrt', subsample=0.8,random_state=10),
#                        param_grid = param_test1, scoring='roc_auc',iid=False,cv=5)
# gsearch1.fit(train[predictors],train[target])
# gsearch1.grid_scores_, gsearch1.best_params_, gsearch1.best_score_
print 'cv finish'
#tree max_depth  min_samples_split
# param_test2 = {'max_depth':range(3,14,2), 'min_samples_split':range(100,801,200)}
# gsearch2 = GridSearchCV(estimator = GradientBoostingClassifier(learning_rate=0.1, n_estimators=60, min_samples_leaf=20,
#       max_features='sqrt', subsample=0.8, random_state=10),
#    param_grid = param_test2, scoring='roc_auc',iid=False, cv=5)
# gsearch2.fit(X,y)
# gsearch2.grid_scores_, gsearch2.best_params_, gsearch2.best_score_
# # min_samples_split   min_samples_leaf
# param_test3 = {'min_samples_split':range(800,1900,200), 'min_samples_leaf':range(60,101,10)}
# gsearch3 = GridSearchCV(estimator = GradientBoostingClassifier(learning_rate=0.1, n_estimators=60,max_depth=7,
#                                      max_features='sqrt', subsample=0.8, random_state=10),
#                        param_grid = param_test3, scoring='roc_auc',iid=False, cv=5)
# gsearch3.fit(X,y)
# gsearch3.grid_scores_, gsearch3.best_params_, gsearch3.best_score_
# # max_features
# param_test4 = {'max_features':range(7,20,2)}
# gsearch4 = GridSearchCV(estimator = GradientBoostingClassifier(learning_rate=0.1, n_estimators=60,max_depth=7, min_samples_leaf =60,
#                min_samples_split =1200, subsample=0.8, random_state=10),
#                        param_grid = param_test4, scoring='roc_auc',iid=False, cv=5)
# gsearch4.fit(X,y)
# gsearch4.grid_scores_, gsearch4.best_params_, gsearch4.best_score_
# # sample rate
# param_test5 = {'subsample':[0.6,0.7,0.75,0.8,0.85,0.9]}
# gsearch5 = GridSearchCV(estimator = GradientBoostingClassifier(learning_rate=0.1, n_estimators=60,max_depth=7, min_samples_leaf =60,
#                min_samples_split =1200, max_features=9, random_state=10),
#                        param_grid = param_test5, scoring='roc_auc',iid=False, cv=5)
# gsearch5.fit(X,y)
# gsearch5.grid_scores_, gsearch5.best_params_, gsearch5.best_score_