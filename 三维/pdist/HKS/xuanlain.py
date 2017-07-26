# -*- coding:utf-8 -*-



import matplotlib.pyplot as plt

import numpy as np
data =np.loadtxt('hksmean130.csv',delimiter=',')
#----------------------------------------------
from scipy.misc import imsave

imsave('meelo.jpg', data[:,0:131])


# fig = plt.figure()
# # 第一个子图,按照默认配置
# ax = fig.add_subplot(111)
# ax.imshow(data)

# # 第二个子图,使用自定义的colormap
#
#
# ax = fig.add_subplot(222)
# cmap=plt.cm.cool #可以使用自定义的colormap
# ax.imshow(data,cmap=cmap)
#
#
#
#
# # 第三个子图增加一个colorbar
# ax = fig.add_subplot(223)
# cmap=plt.cm.hot #可以使用自定义的colormap
# im=ax.imshow(data,cmap=cmap)
# plt.colorbar(im)
#



plt.show()