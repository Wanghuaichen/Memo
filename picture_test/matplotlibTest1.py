# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:matplotlibTest1.py
@time:2016/7/7 16:47

"""
#import matplotlib.pyplot as plt

#plt.bar(left = (0,1),height = (1,0.5),width=(0.2,1))
#plt.show()

import matplotlib.pyplot as plt

X1 = range(0, 50)

Y1 = [num**2 for num in X1] # y = x^2

X2 = [0, 1]

Y2 = [0, 1]  # y = x

Fig = plt.figure(figsize=(8,4))                      # Create a `figure' instance

Ax = Fig.add_subplot(111)               # Create a `axes' instance in the figure

Ax.plot(X1, Y1, X2, Y2)                 # Create a Line2D instance in the axes



#Fig.savefig("test.pdf")
Fig.show()

