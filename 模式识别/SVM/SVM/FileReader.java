package svm.SimplifiedSMO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class FileReader {
	String filename;
	BufferedReader br;
	
	private Data svmData; 
	
	public FileReader(String filename) {
		svmData = Data.getInstance();
		
		File file = new File(filename);
		try {
			file = new File(file.getCanonicalPath());
			//System.out.println();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			 br = new BufferedReader(new InputStreamReader(fis));
			try {
				br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private DataLine getSVMDataLine() {
		String strs[];
		String s1[];
		ArrayList<Double> curX=new ArrayList<Double>();
		int y = 0;
		try {
			strs = br.readLine().split(" ");
			y=(int)Double.parseDouble(strs[0]);
			//s1 = strs[1].split(" ");
			for (int i = 1; i < strs.length; i++) 
				curX.add(Double.parseDouble(strs[i]));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DataLine(curX, y);
	}
	
	
	public void printData( ) {
	
		for (int i = 1; i <= svmData.getFeatureVectors().size(); i++) {
			Set<Collection<Double>> vectors = svmData.get(i);

			for(Collection<Double> v : vectors)
			{
				System.out.print(i+": ");
				
				Iterator<Double> it=v.iterator();
				while(it.hasNext()){
					double p=it.next();
					System.out.print(p + " ");
				}
				
				System.out.println();
			}
		}
	}

	public Data getSVMData(int lines) {
		
		Data svmData = Data.getInstance();
		for (int i = 0; i < lines; i++) {
			DataLine svmDataLine = getSVMDataLine();
			svmData.addVector(svmDataLine.x, svmDataLine.y);
		}
		return svmData;
	}
	
	public static void main(String[] args) {
		
		FileReader reader = new FileReader(".\\src\\svm\\SimplifiedSMO\\heart_scale");
		reader.getSVMData(24);
		reader.printData();
		//printDataLen(svmData);
	}
	
}
