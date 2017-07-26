package svm.SimplifiedSMO;
import java.util.*;

public class Data {
	
	private static Data instance = null;
	
	private Map< Integer, Set< Collection<Double> > > featureVectors;
	
	public Map<Integer, Set<Collection<Double>>> getFeatureVectors() {
		return featureVectors;
	}

	public void setFeatureVectors(
			Map<Integer, Set<Collection<Double>>> featureVectors) {
		this.featureVectors = featureVectors;
	}

	private Data() {
		featureVectors = new HashMap< Integer, Set < Collection<Double> > >();
	}

	public static Data getInstance(){
		if(instance == null)
			instance = new Data();
			return instance;
	}
	
	public void addVector(Collection<Double> vectors, int featureId){
		if( !featureVectors.containsKey(featureId) ){
			featureVectors.put(featureId, new HashSet< Collection<Double> >());
		}
			Collection<Double> v = new ArrayList<Double>();
			v.addAll(vectors);
			featureVectors.get(featureId).add(v);
	}
	
	public Set< Collection<Double> > get(int featureId)
	{
		return featureVectors.get(featureId);
	}

	
}
