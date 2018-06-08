package Intelligence;

import java.util.List;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class kmean_weka {

	public static void main(String[] args) throws Exception {
		/* We load some data */
		DataSource source = new DataSource("C:/Users/nassima/Desktop/extraction/fichier_extraction/profils-old.csv");
		Instances data = source.getDataSet();
		System.out.println( data);
		String Options="-init 0 -max-candidates 100 -periodic-pruning 10000 -min-"
				+ "density 2.0 -t1 -1.25 -t2 -1.0 -N 10 -A \"weka.core.EuclideanDistance -R first-last\" -I 500 -num-slots 1 -S 10"; 
		SimpleKMeans kmean= new SimpleKMeans(); 

		kmean.setOptions(weka.core.Utils.splitOptions(Options));
		kmean.setNumClusters(4);
		kmean.buildClusterer( data); 
		System.out.println(kmean.toString());
		//we get the 6th instance (line) 
		Instance test = (Instance) ((List<net.sf.javaml.core.Instance>)  data).get(5); 
		//we classify the instance 
		System.out.println(kmean.clusterInstance(test));  
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(kmean);
		eval.evaluateClusterer(data);
		System.out.println(eval.clusterResultsToString());
	}

}
