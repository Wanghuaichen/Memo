__author__ = 'shi'
import numpy as np
f = open("genotype_encode.txt")
data = np.loadtxt(f)
print data.shape
rows=np.transpose(data)
print rows.shape
b=[]
num=0
for row in rows:
    if num==2937 or num == 7738 or num ==79:
        b.append(row)
        print num
    num=num+1
c=np.array(b)
d= c.transpose()
print d.shape
np.savetxt("2937.txt", d)