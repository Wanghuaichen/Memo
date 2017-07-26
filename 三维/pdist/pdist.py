__author__ = 'shi'

import numpy as np
import scipy.spatial.distance as disatnce
from  eval_on_shrec_modif import eval_on_shrec_modif
from get_pooled_desc import get_pooled_desc
feature = np.loadtxt("feature_hks2_0.txt",delimiter=',')
label = feature[:,50]
feature =feature[:,0:50]
# print label,feature
# print feature.shape
dist = disatnce.pdist(feature,metric='euclidean')
d= disatnce.squareform(dist)
shreceval = eval_on_shrec_modif(d,label,1)
