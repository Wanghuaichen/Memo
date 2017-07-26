# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test3.py
@time:2016/7/7 19:22

"""

# coding = utf-8

import dateutil, pylab,random
from pylab import *
from datetime import datetime,timedelta



today = datetime.now()

dates = [today + timedelta(days=i) for i in range(10)]

#values = [random.randint(1, 20) for i in range(10)]
values = [3,2,8,4,5,6,7,8,11,2]

pylab.plot_date(pylab.date2num(dates), values, linestyle='-')

text(17, 277, u'瞬时流量示意')

xtext = xlabel(u'时间time (s)')
ytext = ylabel(u'单位 (m3)')
ttext = title(u'xx示意图')
grid(True)

setp(ttext, size='large', color='r')
#setp(text, size='medium', name='courier', weight='bold',color='b')
setp(xtext, size='medium', name='courier', weight='bold', color='g')
setp(ytext, size='medium', name='helvetica', weight='light', color='b')
#savefig('simple_plot.png')
savefig('simple_plot')

show()