#include <stdio.h>  
#include <math.h>   
#include <conio.h>   
#include <stdlib.h>  
  
#define input 2            //�����  
#define hidden 10            //����  
#define output 1            //�����  
#define sampleNum 90          //�������� 
#define test 10         //���Լ�����  
#define nr 0.1          //ѧϰЧ�� 
#define EPS 0.00001  
  
float x[sampleNum][input],d[sampleNum][output],whi[input][hidden],wij[hidden][output],thi[hidden],thj[output];
 //x�������ֵ��d�������ֵ��whi��Ȩֵ�� 
int h,i,j,k,ff;   
double testdata[1][2];
float xmin[input],xmax[input],dmin[output],dmax[output];   
FILE *fp1,*fp2,*fp3,*fp4;   
  
void init(void);   
void startleaning(void);  
void testsample(void);   
void readw(void);   
void readt(void);  
void writew(void);   
float sigmoid(float a);   
double ranu(void);   
  
void init(void)   
{  
    int min,max;    
    if(fp1==0)  
    {   
        system("cls");   
        printf("Can not find the learning sample file!\n");   
        exit(0);  
    }   
    for(k=0;k<sampleNum;k++)  
    {   
        for(h=0;h<input;h++)   
            fscanf(fp1,"%f,",&x[k][h]);  //����������  
        for(j=0;j<output;j++)   
            fscanf(fp1,"%f,",&d[k][j]);  //���������  
    } 
    for(j=0;j<output;j++)   
    {   
        min=1;max=1;   
        for(k=0;k<sampleNum;k++)   
       {   
            if(d[k][j]<d[min][j])  
                min=k;   
            if(d[k][j]>d[max][j])   
                max=k;   
        }   
        dmin[j]=d[min][j];   
        dmax[j]=d[max][j];   
        for(k=0;k<sampleNum;k++)                //�����������һ��  
            d[k][j]=(d[k][j]-dmin[j])/(dmax[j]-dmin[j]);   
    }  
}  
 
void startlearning(void)   
{   
    long int nt,n;   
    float t,error[sampleNum],gerror,xj[output],xi[hidden],yj[output],yi[hidden],pxi[hidden],pxj[output];  
    float u0=0,u1=0,u2=0,u3=0;  
    float v0,v1,v2,v3;  
  
    for(i=0;i<hidden;i++)   
    {   
        for(h=0;h<input;h++)   
            whi[h][i]=-0.8+1.6*ranu();  //whiΪ���뵽���ز��Ȩֵ 
        for(j=0;j<output;j++)   
            wij[i][j]=-0.8+1.6*ranu();  //wijΪ���ز㵽�����Ȩֵ 
  
        thi[i]=-0.5+ranu();  //thiΪ���뵽���ز��bias 
    }   
    for(j=0;j<output;j++)   
        thj[j]=-0.5+ranu();  //thjΪ���ز㵽�����bias 
//ѧϰ��ʼ  
    printf("\t\nPlease enter the learning times:\n");   
    scanf("%ld",&nt);  
    for(n=0;n<nt;n++)                    /*ntΪѧϰ����*/  
    {  
        gerror=0;   
        for(k=0;k<sampleNum;k++)                /*������ѭ��*/   
        {   
            for(i=0;i<hidden;i++)   
            {   
                t=0;   
                for(h=0;h<input;h++)   
                    t+=whi[h][i]*x[k][h];  
                xi[i]=t+thi[i];                  //xiΪ����㵽���ز��Ȩֵ�� 
                yi[i]=sigmoid(xi[i]);           //yiΪ�����任��������
				//�������  
            }   
            for(j=0;j<output;j++)   
            {  
                t=0;  
                for(i=0;i<hidden;i++)  
                    t+=wij[i][j]*yi[i];  
                xj[j]=t+thj[j];                  //xjΪ���ز㵽������Ȩֵ�� 
                yj[j]=sigmoid(xj[j]);            //yjΪ�����任�������� 
            }                                //�������� 
            for(j=0;j<output;j++)                //pxjΪ����㵥���������仯��   
                pxj[j]=yj[j]*(1-yj[j])*(yj[j]-d[k][j]);  
            for(i=0;i<hidden;i++)                //pxiΪ���㵥���������仯��   
            {   
                t=0;  
                for(j=0;j<output;j++)  
                    t+=pxj[j]*wij[i][j];  
                pxi[i]=yi[i]*(1-yi[i])*t;  
            }  
            for(j=0;j<output;j++)  
            {   
                thj[j]=thj[j]-nr*pxj[j];  
                for(i=0;i<hidden;i++)  
                {  
                    wij[i][j]=wij[i][j]-nr*pxj[j]*yi[i];     //���㵽�����Ȩֵ����������nrΪ����  
                }  
            }  
            for(i=0;i<hidden;i++)   
            {  
                thi[i]=thi[i]-nr*pxi[i];  
                for(h=0;h<input;h++)  
                {  
                    whi[h][i]=whi[h][i]-nr*pxi[i]*x[k][h];    //����㵽����Ȩֵ����������nrΪ����  
                }  
            }  
            t=0;   
            for(j=0;j<output;j++)   
                t+=(yj[j]-d[k][j])*(yj[j]-d[k][j])/2.0;   
            error[k]=t;   
            gerror+=error[k];                           //ȫ����� g(lobal)error   
        }  
        //������ѭ������
        if(gerror<EPS)  
            break;   
    }  
     writew();  
	 fclose(fp2);   
    int k=0;
    for(k=0;k<sampleNum;k++)
    {
	    for(i=0;i<hidden;i++)   
	    {   
	        t=0;   
	        for(h=0;h<input;h++)   
	            t+=whi[h][i]*x[k][h];  
	        	xi[i]=t+thi[i];                  //xiΪ����㵽���ز��Ȩֵ�� 
	        	yi[i]=sigmoid(xi[i]);           //yiΪ�����任��������
					//������� 
	    }   
	    for(j=0;j<output;j++)   
	    {  
	        t=0;  
	        for(i=0;i<hidden;i++)  
	            t+=wij[i][j]*yi[i];  
	        xj[j]=t+thj[j];                  //xjΪ���ز㵽������Ȩֵ�� 
	        yj[j]=sigmoid(xj[j]);            //yjΪ�����任�������� 
	    }
	    printf("%f %f %f\n",x[k][0],x[k][1],yj[0]*(dmax[0]-dmin[0])+dmin[0]);
	}
	while(1){
      	printf("press any number to test Network,press 'C' exit to use test.txt\n");
		scanf("%lf%lf",&testdata[0][0],&testdata[0][1]); 
		if(getchar()=='c') break;
	    for(i=0;i<hidden;i++)   
	    {   
	        t=0;   
	        for(h=0;h<input;h++)   
	            t+=whi[h][i]*testdata[0][h];  
	        	xi[i]=t+thi[i];                  //xiΪ����㵽���ز��Ȩֵ�� 
	        	yi[i]=sigmoid(xi[i]);           //yiΪ�����任��������
					//������� 
	    }   
	    for(j=0;j<output;j++)   
	    {  
	        t=0;  
	        for(i=0;i<hidden;i++)  
	            t+=wij[i][j]*yi[i];  
	        xj[j]=t+thj[j];                  //xjΪ���ز㵽������Ȩֵ�� 
	        yj[j]=sigmoid(xj[j]);            //yjΪ�����任�������� 
	    }
	    printf("test:%f\n",yj[0]*(dmax[0]-dmin[0])+dmin[0]);
	}
    // ѧϰѭ������   
     for(i=0;i<hidden;i++)   
    {   
        for(h=0;h<input;h++)    
          printf("W(input->hidden)[%d][%d]=%f\n",h,i,whi[h][i]);
    }   
    for(i=0;i<hidden;i++)   
    {   
        for(j=0;j<output;j++)   
           printf("W(hidden->output)[%d][%d]=%f\n",i,j,wij[i][j]);
    }  
	for(i=0;i<hidden;i++)     
	      printf("bias(input->hidden)[%d]=%f\n",i,thi[i]);       
    for(j=0;j<output;j++)   
        printf("bias(hidden->output)[%d]=%f\n",j,thj[j]);
    printf("\t\nGlobal error=%f\n",gerror);  
    printf("Press any key to choose a next task!/n");   
    getch();  

}   

void testsample(void)   
{  
    float tx[input],t,xj[output],xi[hidden],yj[output],yi[hidden];   
    if(fp2==0)   
    {   
        printf("\t can not find the weight file:w.txt\n");   
        exit(0);   
    }   
    readw();        
    for(ff=0;ff<test;ff++)                      
    {  
        for(h=0;h<input;h++)   
            fscanf(fp3,"%f,",&tx[h]);   
        for(i=0;i<hidden;i++)   
        {   
            t=0;   
            for(h=0;h<input;h++)   
                t+=whi[h][i]*tx[h];  
            xi[i]=t+thi[i];  
            yi[i]=sigmoid(xi[i]);  
        }   
        for(j=0;j<output;j++)   
        {   
            t=0;   
            for(i=0;i<hidden;i++)   
                t+=wij[i][j]*yi[i];  
            xj[j]=t+thj[j];   
            yj[j]=sigmoid(xj[j]);   
        }        
        for(j=0;j<output;j++)                         
        {   
            yj[j]=yj[j]*(dmax[j]-dmin[j])+dmin[j];   
            fprintf(fp4,"%f\n",yj[j]);   
        }   
 }  
   fclose(fp4);  
   printf("\t\nThe result save in testreslut.txt?\n");   
   printf("Press any key to choose a next task!\n");   
   getch();  
}   
  
void writew(void)   
{   
rewind(fp2);   
for(h=0;h<input;h++)   
 {   
  for(i=0;i<hidden;i++)   
   fprintf(fp2,"%8.3f ",whi[h][i]);   
   fprintf(fp2,"\n");   
 }   
fprintf(fp2,"\n");   
  
for(i=0;i<hidden;i++)   
 fprintf(fp2,"%8.3f ",thi[i]);  
fprintf(fp2,"\n\n");   
  
for(j=0;j<output;j++)   
 {   
  for(i=0;i<hidden;i++)   
   fprintf(fp2,"%8.3f ",wij[i][j]);   
  fprintf(fp2,"\n");   
 }   
fprintf(fp2,"\n");   
for(j=0;j<output;j++)   
 fprintf(fp2,"%8.3f ",thj[j]);   
}   
  
void readw(void)   
{   
for(h=0;h<input;h++)   
 for(i=0;i<hidden;i++)   
   fscanf(fp2,"%f",&whi[h][i]);   
for(i=0;i<hidden;i++)   
   fscanf(fp2,"%f",&thi[i]);  
for(j=0;j<output;j++)   
   for(i=0;i<hidden;i++)   
      fscanf(fp2,"%f",&wij[i][j]);   
for(j=0;j<output;j++)   
   fscanf(fp2,"%f",&thj[j]);   
}   
  
float sigmoid(float a)   
{  
    return (1.0/(1+exp(-a)));  
}  
   
double ranu(void)   
{  
    static double xrand=3.0;   
    double m=8589934592.0, a=30517578125.0;   
lp: xrand=fmod(xrand*a,m);     /*����ȡ��*/  
   if(xrand>1.0)   
       return(xrand/m);   
   else   
   {  
       xrand=1.0;  
       goto lp;  
   }  
}  
  
  
void main()   
{   
fp1=fopen("D:/BP/sample.txt","r");  
fp2=fopen("D:/BP/weight.txt","w+");  
fp3=fopen("D:/BP/test.txt","r+");  
fp4=fopen("D:/BP/testreslut.txt","w+");  
init();  
  while(1)  
 {  
   system("cls");   
   printf("\t\n    choose a task...\n\n");   
   printf("\t\n     (S) Press 'S' Start Learning.\n");   
   printf("\t\n     (T) Press 'T' Test Samples.\n");    
   printf("\t\n     (Q) Press 'Q' quit!\n");  
	switch(getchar())   
  	{  
	    case 's': startlearning();break;   
	    case 't': testsample();break;  
	    case 'q': exit(0);break;   
  	}   
 }  
fclose(fp1);   
fclose(fp3);  
getch();  
}  
