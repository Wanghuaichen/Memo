# -*- coding:utf-8 -*-
__author__ = 'shi'
import numpy as np
import scipy.io as sp
import os
from  sklearn.preprocessing import normalize


def get_pooled_desc(type,normation1=None,normation2=None):

    def __init__(self,type,normation1,normation2):
        self.type=type
        self.normation1=normation1
        self.normation2 = normation2

    SHAPES = 'D://Matlab/csd_lmnn/data/SHREC14_HUMAN/REAL/resol_20kf/evecs.cot/'
    DESCS = 'D://Matlab/csd_lmnn/data/SHREC14_HUMAN/REAL/resol_20kf/descriptors.cot/HKS/'
    X_desc = []
    count=0
    file_list = os.listdir(SHAPES)
    for file in file_list:
        print 'Pooling descriptor ',count
        count=count+1
        if count==4:
            break
        file_path = SHAPES+file
        print file
        data = sp.loadmat(file_path)
        Area = data['S']
        m=Area.shape[0]
        n=Area.shape[1]
        Area_list=[]
        if m==n:
            for i in range(m):
                Area_list.append(Area[i,i])
        else:
            print 'dim not same\n'
            break
        Area=np.array(Area_list)
        #make area elements sum up to 1
        pArea = Area/np.sum(Area)
        pArea.shape=(len(pArea),1)
        pArea = np.transpose(pArea)

        #load shape's point desriptors
        file_num = file.split('.')[0]
        file_path2 = DESCS+file_num+'.mat'
        Desc = sp.loadmat(file_path2)
        Desc = Desc['desc']
        print 'Desc',Desc.shape
        #normalize point descriptors
        if normation1 is not None:
            Desc = normalize(Desc,norm='l2')
        #Pooling weighted by the area elements
        print 'pArea.shape',pArea.shape
        print 'desc.shape',Desc.shape
        descPooled =np.dot(pArea,Desc)
        print  'descpooled',descPooled.shape
        #Normalize the pooled descriptor
        descPooled=normalize(descPooled,norm='l2')
        descPooled = list(descPooled[0])
        X_desc.append(descPooled)
    descPooled = np.array(X_desc)
    print descPooled.shape
    return descPooled
if __name__=='__main__':
    get_pooled_desc(1,2,3)








