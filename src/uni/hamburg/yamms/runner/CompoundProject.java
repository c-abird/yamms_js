package uni.hamburg.yamms.runner;

public interface CompoundProject {
	
	//Object[] retrieveParamSet(int i);
	//int numberOfParamSets();
	Object[][] getParamSets();
	void startSimulation(Object[] runArgs);
	
}
