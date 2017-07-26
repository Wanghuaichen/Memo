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
	 * 返回的拉格朗日乘子
	 */
	double a[][][];
	double b[][];
	/**
	 * 核函数
	 */
	double kernel[][];
	
	/**
	 * x[][]是二维向量，每一行记录每一个文本包涵特征向量权值的矩阵
	*/
	private double x[][]; 
	/** 
	 * y[]的值表示该文本属于哪一个类
	 */
	int y[];
	
	/** 
	 * 初始化调取svmData
	 */
	public ASimpleSMO(){
		svmData = Data.getInstance();
		}
	/** 
	 * length 总特征数
	 * n 总文本数
	 */
	int length;
	int n;
	private SVMModel train() {
		
		//当前先根据输入文件手动调整特征值个数
		length=3;
		
		double[][][] a = new double[svmData.getFeatureVectors().size()+1][svmData.getFeatureVectors().size()+1][24];//拉格朗日乘子
		this.a=a;
		double [][]b3=new double[svmData.getFeatureVectors().size()+1][svmData.getFeatureVectors().size()+1];
		this.b=b3;
		/**
		 * svm分类器用于多类时，只能每次训练2类
		 * id1和id2，两两循环
		*/
		double percent=0;
		for(int id1=1;id1<=svmData.getFeatureVectors().size();id1++)
			for(int id2=id1+1;id2<=svmData.getFeatureVectors().size();id2++)
			{
			//	记录进度，测试用 
				percent=percent+2;
				double percent2=percent*100/svmData.getFeatureVectors().size()/(svmData.getFeatureVectors().size()-1);
				System.out.println("完成了"+percent2+"%");
			/**
			 * 获取进行训练的2类所含的训练文本向量
			 */
				Set<Collection<Double>> vectors1 = svmData.get(id1);
				Set<Collection<Double>> vectors2 = svmData.get(id2);
				n=0;
				double [][]x2=new double[vectors1.size()+vectors2.size()][length];
				int []y2=new int[vectors1.size()+vectors2.size()];
				
				this.y=y2;
				this.x=x2;
			/**
			 * 这里是转换成数组来运算，其实可以不用
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
		 * 默认输入参数值
		 * C: 对不在界内的惩罚因子
		 * tol: 容忍极限值
		 * maxPasses: 表示没有改变拉格朗日乘子的最多迭代次数
		 * boundAlpha： 表示x点处于边界上所对应的拉格朗日乘子a的集合
		 */
		double C = 1; 
		double tol = 0.01;
		int maxPasses = 5; 
		this.boundAlpha.clear();
		int noError=0;
		/**
		 * 将乘子初始化为0
		 */
		for (int i = 0; i < n; i++) {
			a[id1][id2][i] = 0;
		}
		int passes = 0;
		while (passes < maxPasses) {
			/**
			 * num_changed_alphas 表示改变乘子的次数（基本上是成对改变的）
			 */
			noError++;
			if(noError>100*n)break;
			int num_changed_alphas = 0;
			for (int i = 0; i < n; i++) {
				double Ei = getE(i,id1,id2);
				/**
				 * 把违背KKT条件的ai作为第一个
				 * 满足KKT条件的情况是：
				 * yi*f(i) >= 1 and alpha == 0 (正确分类)
				 * yi*f(i) == 1 and 0<alpha < C (在边界上的支持向量)
				 * yi*f(i) <= 1 and alpha == C (在边界之间)
				 *
				 * ri = y[i] * Ei = y[i] * f(i) - y[i]^2 >= 0
				 * 如果ri < 0并且alpha < C 则违反了KKT条件
				 * 因为原本ri < 0 应该对应的是alpha = C
				 * 同理，ri > 0并且alpha > 0则违反了KKT条件
				 * 因为原本ri > 0对应的应该是alpha =0
				 */
				if ((y[i] * Ei < -tol && a[id1][id2][i] < C) ||
					(y[i] * Ei > tol && a[id1][id2][i] > 0)) 
				{
					/**
					 * ui*yi=1边界上的点 0 < a[i] < C
					 * 找MAX|E1 - E2|
					 */
					int j;
					
					if (this.boundAlpha.size() > 0) {
						j = findMax(Ei, this.boundAlpha,id1,id2);
					} else 
					/**
					 * 如果边界上没有，就随便选一个j != i的aj
					 */
						j = RandomSelect(i);
					
					double Ej = getE(j,id1,id2);
					
					/**
					 * 保存当前的ai和aj
					 */
					double oldAi = a[id1][id2][i];
					double oldAj = a[id1][id2][j];
					
					/**
					 * 计算乘子的范围U, V
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
					 * 如果eta等于0或者大于0 则表明a最优值应该在L或者U上
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
					 * 计算b1， b2
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
		 * 返回训练完成的拉格朗日乘子a和对应的b
		 */
		return new SVMModel(a ,b);
	}
	/** 
	 * svm算法需要——最大化(Ei - Ej)
	 * @param Ei 
	 *        其中一个拉格朗日乘子所计算出的E值
	 * @param boundAlpha2 
	 *        表示x点处于边界上所对应的拉格朗日乘子a的集合
	 * @param id1
	 *        表示当前训练的类编号
	 * @param id2
	 *        表示当前训练的类编号
	 * @return 能得到较优解时的另一个拉格朗日乘子位置
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
 * 测试函数
 * @param model
 *        训练模型
 * @param x2
 *        测试文本的总特征值矩阵
 * @param y2
 *        测试文本各个的所属类别
 * @return 正确率
 */
	private double predict(SVMModel model, double x2[][], int y2[]) {
		/**
		 * probability 正确率
		 * sum 二类判断其属于哪一类时的指标
		 * correctCnt 正确的样本数
		 * total 总样本数
		 * p 当前文本最有可能的所属类
		 * q 多类判断时的队首
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
			 * p,q初始化后，开始跑队列
			 */
		p=1;
		q=2;
		while(q<=svmData.getFeatureVectors().size())
		{
			/**
			 * 获取p,q类的文本
			 * 转成数组后来比较
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
			 * sum大于0则属于p类，小于0则属于q类
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
		System.out.println("成功了"+correctCnt+"总共有"+total);
		return probability;
	} 
	
	/**
	 * 初始化核函数
	 * @param length
	 *        总特征数大小
	 */
	private void initiateKernel(int length) {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				kernel[i][j] = k(i, j);
			}
		}
	}


	/**
	 * 采用的是多项式线性核函数
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
	 * 测试专用核函数计算，公式同上
	 * @param i
	 * @param x2
	 * @return 核函数计算后的值
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
 * 计算当前拉格朗日乘子时的函数值
 * @param j
 * @param id1
 * @param id2
 * @return 函数值
 */

	private double f(int j,int id1,int id2) {
		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += a[id1][id2][i] * y[i] * kernel[i][j]; 
		}
		return sum + b[id1][id2];
	}
/**
 * 计算偏移量
 * @param i   第i个拉格朗日乘子，每个文本各有一个
 * @param id1
 * @param id2
 * @return 偏移量
 */
	private double getE(int i,int id1,int id2) {
		return f(i,id1,id2) - y[i];
	}
/**
 * 调试用
 * 打印各类别所含的文本特征值
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
		 * 读取训练文本 
		 * 当前是一个24*3的矩阵，表示有24个训练用例，共3个特征值
		 */
		FileReader reader = new FileReader(".\\src\\svm\\SimplifiedSMO\\heart_scale");
		reader.getSVMData(24);
	//	simplifiedSMO.printData();
		System.out.println("开始训练...");
		SVMModel model = simplifiedSMO.train();
		System.out.println("训练结束");
		//开始预测
		System.out.println("开始预测...");
		/**
		 * 读取测试文本
		 */
		//SVMFileReader reader2 = new SVMFileReader(".\\src\\lib\\SimplifiedSMO\\heart_scale2");
		FileReadertest reader2 = new FileReadertest(".\\src\\svm\\SimplifiedSMO\\heart_scale2");
		Datatest svmData2 = reader2.getSVMData(24);
		double probability = simplifiedSMO.predict(model, svmData2.getX(), svmData2.getY());
		System.out.println("预测正确率为：" + probability);
	}
}
