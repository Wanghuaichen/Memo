# -*- coding:utf-8 -*-
__author__ = 'shi'
import numpy as np

def normalize(X,p,dim):

    def __init__(self,X,p,dim):
        self.X=X
        self.p=p
        self.dim = dim

    if len(p)>1:
        if p=='L1' or p=='l1':
            normalize(X,1,dim)
        if p=='L2' or p=='l2':
            normalize(X,2,dim)
    if len(p)==1:
        n=(np.sum(np.abs(X)))
    else:
