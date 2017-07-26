__author__ = 'shi'
import pandas as pd
df=pd.DataFrame(
    {
        'A':[1,1,1,1,2,2,2,3,3,4,4,4],
        'B':[5,5,6,7,6,6,6,7,7,6,7,7],
        'C':[13,1,1,1,1,1,1,1,1,1,1,1]
    }
);
print df
g=df.groupby(['A','B']).sum()
print g