# -*- coding:utf-8 -*-

"""
@author:XF-shi
@file:test9.py
@time:2016/7/10 19:42

"""
from __future__ import division
import numpy
import pylab
from scipy.signal import convolve2d
def moving_average_2d(data, window):
 """Moving average on two-dimensional data.
 """
 # Makes sure that the window function is normalized.
 window /= window.sum()
 # Makes sure data array is a numpy array or masked array.
 if type(data).__name__ not in ['ndarray', 'MaskedArray']:
  data = numpy.asarray(data)
 # The output array has the same dimensions as the input data
 # (mode='same') and symmetrical boundary conditions are assumed
 # (boundary='symm').
 return convolve2d(data, window, mode='same', boundary='symm')

#code above abribu
M, N = 20, 2000 # The shape of the data array
m, n = 3, 10  # The shape of the window array
y, x = numpy.mgrid[1:M+1, 0:N]
# The signal and lots of noise
signal = -10 * numpy.cos(x / 500 + y / 10) / y
noise = numpy.random.normal(size=(M, N))
z = signal + noise
# Calculating a couple of smoothed data.
win = numpy.ones((m, n))
z1 = moving_average_2d(z, win)
win = numpy.ones((2*m, 2*n))
z2 = moving_average_2d(z, win)
win = numpy.ones((2*m, 4*n))
z3 = moving_average_2d(z, win)
win = numpy.ones((2*m, 10*n))
z4 = moving_average_2d(z, win)

###################################

# Initializing the plot
pylab.close('all')
pylab.ion()
fig = pylab.figure()
bbox = dict(edgecolor='w', facecolor='w', alpha=0.9)
crange = numpy.arange(-15, 16, 1.) # color scale data range
# The plots
ax = pylab.subplot(2, 2, 1)
pylab.contourf(x, y, z, crange)
pylab.contour(x, y, z1, crange, colors='k')
ax.text(0.05, 0.95, 'n=10, m=3', ha='left', va='top', transform=ax.transAxes,
 bbox=bbox)
bx = pylab.subplot(2, 2, 2, sharex=ax, sharey=ax)
pylab.contourf(x, y, z, crange)
pylab.contour(x, y, z2, crange, colors='k')
bx.text(0.05, 0.95, 'n=20, m=6', ha='left', va='top', transform=bx.transAxes,
 bbox=bbox)
bx = pylab.subplot(2, 2, 3, sharex=ax, sharey=ax)
pylab.contourf(x, y, z, crange)
pylab.contour(x, y, z3, crange, colors='k')
bx.text(0.05, 0.95, 'n=40, m=6', ha='left', va='top', transform=bx.transAxes,
 bbox=bbox)
bx = pylab.subplot(2, 2, 4, sharex=ax, sharey=ax)
pylab.contourf(x, y, z, crange)
pylab.contour(x, y, z4, crange, colors='k')
bx.text(0.05, 0.95, 'n=100, m=6', ha='left', va='top', transform=bx.transAxes,
 bbox=bbox)
ax.set_xlim([x.min(), x.max()])
ax.set_ylim([y.min(), y.max()])
fig.savefig('movingavg_sample.png')