package svm.SimplifiedSMO;
//http://blog.csdn.net/v_july_v/article/details/7624837
//http://www.tuicool.com/articles/RRZvYb
//http://blog.csdn.net/fighting_one_piece/article/details/37927747
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


public class ASimpleSMO {
	private HashSet<Integer> boundAlpha = new HashSet<Integer>();
	private Random random = new Random();
	
	private Data svmData; 
	
	/**
	 * ���ص��������ճ���
	 */
	double a[][][];
	double b[][];
	/**
	 * �˺���
	 */
	double kernel[][];
	
	/**
	 * x[][]�Ƕ�ά������ÿһ�м�¼ÿһ���ı�������������Ȩֵ�ľ���
	*/
	private double x[][]; 
	/** 
	 * y[]��ֵ��ʾ���ı�������һ����
	 */
	int y[];
	
	/** 
	 * ��ʼ����ȡsvmData
	 */
	public ASimpleSMO(){
		svmData = Data.getInstance();
		}
	/** 
	 * length ��������
	 * n ���ı���
	 */
	int length;
	int n;
	private SVMModel train() {
		
		//��ǰ�ȸ��������ļ��ֶ���������ֵ����
		length=3;
		
		double[][][] a = new double[svmData.getFeatureVectors().size()+1][svmData.getFeatureVectors().size()+1][24];//�������ճ���
		this.a=a;
		double [][]b3=new double[svmData.getFeatureVectors().size()+1][svmData.getFeatureVectors().size()+1];
		this.b=b3;
		/**
		 * svm���������ڶ���ʱ��ֻ��ÿ��ѵ��2��
		 * id1��id2������ѭ��
		*/
		double percent=0;
		for(int id1=1;id1<=svmData.getFeatureVectors().size();id1++)
			for(int id2=id1+1;id2<=svmData.getFeatureVectors().size();id2++)
			{
			//	��¼���ȣ������� 
				percent=percent+2;
				double percent2=percent*100/svmData.getFeatureVectors().size()/(svmData.getFeatureVectors().size()-1);
				System.out.println("�����"+percent2+"%");
			/**
			 * ��ȡ����ѵ����2��������ѵ���ı�����
			 */
				Set<Collection<Double>> vectors1 = svmData.get(id1);
				Set<Collection<Double>> vectors2 = svmData.get(id2);
				n=0;
				double [][]x2=new double[vectors1.size()+vectors2.size()][length];
				int []y2=new int[vectors1.size()+vectors2.size()];
				
				this.y=y2;
				this.x=x2;
			/**
			 * ������ת�������������㣬��ʵ���Բ���
			 */

				for(Collection<Double> v : vectors1)
					{
					int j=0;
					Iterator<Double> it=v.iterator();
					while(it.hasNext()){
						double p=it.next();
						x[n][j++]=p;}
					    y[n++]=1;
					}
				
				for(Collection<Double> v : vectors2)
					{
					int j=0;
					Iterator<Double> it=v.iterator();
					while(it.hasNext()){
						double p=it.next();
						x[n][j++]=p;}
					    y[n++]=-1;
					}
				
		kernel = new double[n][n];
		initiateKernel(n);
		
		/**
		 * Ĭ���������ֵ
		 * C: �Բ��ڽ��ڵĳͷ�����
		 * tol: ���̼���ֵ
		 * maxPasses: ��ʾû�иı��������ճ��ӵ�����������
		 * boundAlpha�� ��ʾx�㴦�ڱ߽�������Ӧ���������ճ���a�ļ���
		 */
		double C = 1; 
		double tol = 0.01;
		int maxPasses = 5; 
		this.boundAlpha.clear();
		int noError=0;
		/**
		 * �����ӳ�ʼ��Ϊ0
		 */
		for (int i = 0; i < n; i++) {
			a[id1][id2][i] = 0;
		}
		int passes = 0;
		while (passes < maxPasses) {
			/**
			 * num_changed_alphas ��ʾ�ı���ӵĴ������������ǳɶԸı�ģ�
			 */
			noError++;
			if(noError>100*n)break;
			int num_changed_alphas = 0;
			for (int i = 0; i < n; i++) {
				double Ei = getE(i,id1,id2);
				/**
				 * ��Υ��KKT������ai��Ϊ��һ��
				 * ����KKT����������ǣ�
				 * yi*f(i) >= 1 and alpha == 0 (��ȷ����)
				 * yi*f(i) == 1 and 0<alpha < C (�ڱ߽��ϵ�֧������)
				 * yi*f(i) <= 1 and alpha == C (�ڱ߽�֮��)
				 *
				 * ri = y[i] * Ei = y[i] * f(i) - y[i]^2 >= 0
				 * ���ri < 0����alpha < C ��Υ����KKT����
				 * ��Ϊԭ��ri < 0 Ӧ�ö�Ӧ����alpha = C
				 * ͬ��ri > 0����alpha > 0��Υ����KKT����
				 * ��Ϊԭ��ri > 0��Ӧ��Ӧ����alpha =0
				 */
				if ((y[i] * Ei < -tol && a[id1][id2][i] < C) ||
					(y[i] * Ei > tol && a[id1][id2][i] > 0)) 
				{
					/**
					 * ui*yi=1�߽��ϵĵ� 0 < a[i] < C
					 * ��MAX|E1 - E2|
					 */
					int j;
					
					if (this.boundAlpha.size() > 0) {
						j = findMax(Ei, this.boundAlpha,id1,id2);
					} else 
					/**
					 * ����߽���û�У������ѡһ��j != i��aj
					 */
						j = RandomSelect(i);
					
					double Ej = getE(j,id1,id2);
					
					/**
					 * ���浱ǰ��ai��aj
					 */
					double oldAi = a[id1][id2][i];
					double oldAj = a[id1][id2][j];
					
					/**
					 * ������ӵķ�ΧU, V
					 */
					double L, H;
					if (y[i] != y[j]) {
						L = Math.max(0, a[id1][id2][j] - a[id1][id2][i]);
						H = Math.min(C, C - a[id1][id2][i] + a[id1][id2][j]);
					} else {
						L = Math.max(0, a[id1][id2][i] + a[id1][id2][j] - C);
						H = Math.min(C, a[id1][id2][i] + a[id1][id2][j]);
					}
					
					/**
					 * ���eta����0���ߴ���0 �����a����ֵӦ����L����U��
					 */
					double eta = 2 * k(i, j) - k(i, i) - k(j, j);
					
					if (eta >= 0)
						continue;
					
					a[id1][id2][j] = a[id1][id2][j] - y[j] * (Ei - Ej)/ eta;
					if (L< a[id1][id2][j] && a[id1][id2][j] < H)
						this.boundAlpha.add(j);
					
					if (a[id1][id2][j] < L) 
						a[id1][id2][j] = L;
					else if (a[id1][id2][j] > H) 
						a[id1][id2][j] = H;
					
					if (Math.abs(a[id1][id2][j] - oldAj) < 1e-5)
						continue;
					a[id1][id2][i] = a[id1][id2][i] + y[i] * y[j] * (oldAj - a[id1][id2][j]);
					if (0 < a[id1][id2][i] && a[id1][id2][i] < C)
						this.boundAlpha.add(i);
					
					/**
					 * ����b1�� b2
					 */
					double b1 = b[id1][id2] - Ei - y[i] * (a[id1][id2][i] - oldAi) * k(i, i) - y[j] * (a[id1][id2][j] - oldAj) * k(i, j);
					double b2 = b[id1][id2] - Ej - y[i] * (a[id1][id2][i] - oldAi) * k(i, j) - y[j] * (a[id1][id2][j] - oldAj) * k(j, j);
					
					if (0 < a[id1][id2][i] && a[id1][id2][i] < C)
						b[id1][id2] = b1;
					else if (0 < a[id1][id2][j] && a[id1][id2][j] < C)
						b[id1][id2] = b2;
					else 
						b[id1][id2] = (b1 + b2) / 2;
					
					num_changed_alphas = num_changed_alphas + 1;
					System.out.println(num_changed_alphas);
				}
			}
			if (num_changed_alphas == 0) {
				passes++;
			} else 
				passes = 0;
		}
			}
		/**
		 * ����ѵ����ɵ��������ճ���a�Ͷ�Ӧ��b
		 */
		return new SVMModel(a ,b);
	}
	/** 
	 * svm�㷨��Ҫ�������(Ei - Ej)
	 * @param Ei 
	 *        ����һ���������ճ������������Eֵ
	 * @param boundAlpha2 
	 *        ��ʾx�㴦�ڱ߽�������Ӧ���������ճ���a�ļ���
	 * @param id1
	 *        ��ʾ��ǰѵ��������
	 * @param id2
	 *        ��ʾ��ǰѵ��������
	 * @return �ܵõ����Ž�ʱ����һ���������ճ���λ��
	 */
	private int findMax(double Ei, HashSet<Integer> boundAlpha2,int id1,int id2) {
		double max = 0;
		int maxIndex = -1;
		for (Iterator<Integer> iterator = boundAlpha2.iterator(); iterator.hasNext();) {
			Integer j = (Integer) iterator.next();
			double Ej = getE(j,id1,id2);
			if (Math.abs(Ei - Ej) > max) {
				max = Math.abs(Ei - Ej);
				maxIndex = j;
			}
		}
		return maxIndex;
	}
/**
 * ���Ժ���
 * @param model
 *        ѵ��ģ��
 * @param x2
 *        �����ı���������ֵ����
 * @param y2
 *        �����ı��������������
 * @return ��ȷ��
 */
	private double predict(SVMModel model, double x2[][], int y2[]) {
		/**
		 * probability ��ȷ��
		 * sum �����ж���������һ��ʱ��ָ��
		 * correctCnt ��ȷ��������
		 * total ��������
		 * p ��ǰ�ı����п��ܵ�������
		 * q �����ж�ʱ�Ķ���
		 */
		double probability = 0;
		double sum = 0;
		double succ;
		int correctCnt = 0;
		int total = y2.length;
		int p,q;

		for (int i = 0; i < total; i++)
		{
		succ=-1;
			/**
			 * p,q��ʼ���󣬿�ʼ�ܶ���
			 */
		p=1;
		q=2;
		while(q<=svmData.getFeatureVectors().size())
		{
			/**
			 * ��ȡp,q����ı�
			 * ת����������Ƚ�
			 */
			Set<Collection<Double>> vectors1 = svmData.get(p);
			Set<Collection<Double>> vectors2 = svmData.get(q);
			n=0;
			double [][]x3=new double[vectors1.size()+vectors2.size()][length];
			int []y3=new int[vectors1.size()+vectors2.size()];
			this.y=y3;
			this.x=x3;

			for(Collection<Double> v : vectors1)
				{
				int j=0;
				Iterator<Double> it=v.iterator();
				while(it.hasNext())x[n][j++]=it.next();
				y[n++]=1;
				}
			for(Collection<Double> v : vectors2)
				{
				int j=0;
				Iterator<Double> it=v.iterator();
				while(it.hasNext())x[n][j++]=it.next();
				y[n++]=-1;
				}
			sum = 0;
			for (int j = 0; j < n; j++) {
				sum += y[j] * model.a[p][q][j] * ktest(j,x2[i]);
			}
			sum += model.b[p][q];
			if(Math.abs(sum)<succ||succ==-1)
				succ=Math.abs(sum);
			/**
			 * sum����0������p�࣬С��0������q��
			 */
			if (sum < 0){
				p=q;
				q++;
			} else q++;		
		}
		if(p==y2[i])
		correctCnt++;
		System.out.println(p+" pk "+y2[i]+" "+succ);
		}
		probability = (double)correctCnt / (double)total;
		System.out.println("�ɹ���"+correctCnt+"�ܹ���"+total);
		return probability;
	} 
	
	/**
	 * ��ʼ���˺���
	 * @param length
	 *        ����������С
	 */
	private void initiateKernel(int length) {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				kernel[i][j] = k(i, j);
			}
		}
	}


	/**
	 * ���õ��Ƕ���ʽ���Ժ˺���
	 * kernel(i, j) = xTx
	 */
	private double k(int i, int j) {
		double sum = 0.0;
		for (int t = 0; t < x[i].length; t++) {
			sum += x[i][t] * x[j][t];
		}
		return sum;
	}
	/**
	 * ����ר�ú˺������㣬��ʽͬ��
	 * @param i
	 * @param x2
	 * @return �˺���������ֵ
	 */
private double ktest(int i, double x2[]) {
	
	double sum = 0.0;
	for (int t = 0; t < length; t++) {
		sum += x[i][t] * x2[t];
	}
	return sum;
}


	/**
	 * select j which is not equal with i
	 */
	private int RandomSelect(int i) {
		int j;
		do {
			j = random.nextInt(n);
		} while(i == j);
		return j;
	}

/**
 * ���㵱ǰ�������ճ���ʱ�ĺ���ֵ
 * @param j
 * @param id1
 * @param id2
 * @return ����ֵ
 */

	private double f(int j,int id1,int id2) {
		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += a[id1][id2][i] * y[i] * kernel[i][j]; 
		}
		return sum + b[id1][id2];
	}
/**
 * ����ƫ����
 * @param i   ��i���������ճ��ӣ�ÿ���ı�����һ��
 * @param id1
 * @param id2
 * @return ƫ����
 */
	private double getE(int i,int id1,int id2) {
		return f(i,id1,id2) - y[i];
	}
/**
 * ������
 * ��ӡ������������ı�����ֵ
 */
	public void printData( ) {
		
		for (int i = 1; i <= svmData.getFeatureVectors().size(); i++) {
			Set<Collection<Double>> vectors = svmData.get(i);

			for(Collection<Double> v : vectors)
			{
				System.out.print(i+": ");
				
				for(double d : v){
					System.out.print(d + " ");
				}
				System.out.println();
			}
		}
	}
	
	public static void main(String[] args) {
		ASimpleSMO simplifiedSMO = new ASimpleSMO();
		/**
		 * ��ȡѵ���ı� 
		 * ��ǰ��һ��24*3�ľ��󣬱�ʾ��24��ѵ����������3������ֵ
		 */
		FileReader reader = new FileReader(".\\src\\svm\\SimplifiedSMO\\heart_scale");
		reader.getSVMData(24);
	//	simplifiedSMO.printData();
		System.out.println("��ʼѵ��...");
		SVMModel model = simplifiedSMO.train();
		System.out.println("ѵ������");
		//��ʼԤ��
		System.out.println("��ʼԤ��...");
		/**
		 * ��ȡ�����ı�
		 */
		//SVMFileReader reader2 = new SVMFileReader(".\\src\\lib\\SimplifiedSMO\\heart_scale2");
		FileReadertest reader2 = new FileReadertest(".\\src\\svm\\SimplifiedSMO\\heart_scale2");
		Datatest svmData2 = reader2.getSVMData(24);
		double probability = simplifiedSMO.predict(model, svmData2.getX(), svmData2.getY());
		System.out.println("Ԥ����ȷ��Ϊ��" + probability);
	}
}
