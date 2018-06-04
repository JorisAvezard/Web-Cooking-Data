package Intelligence;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfAveragePairwiseSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.MahalanobisDistance;
import net.sf.javaml.tools.data.FileHandler;

public class MachineLearning {

	public static void main(String[] args) throws IOException {
		/* We load some data */
		double score=0.0;
		double score_simil=0.0;
		double score_avg=0.0;
		Dataset data = FileHandler.loadDataset(new File("C:/Users/nassima/Desktop/extraction/fichier_extraction/profils.txt"),";");
		/* We create a clustering algorithm, in this case the k-means
		 * algorithm with 4 clusters. */
		Clusterer km=new KMeans();
		System.out.println(data);
		/* We cluster the data */
		Dataset[] clusters = km.cluster(data);
		/* Create a measure for the cluster quality */
		//System.out.println(data);
		ClusterEvaluation sse= new SumOfSquaredErrors();
		/* Measure the quality of the clustering */
		 score=sse.score(clusters);
		System.out.println("le taux erreur :"+score+"\n");
		// System.out.println("Cluster count: " + clusters.length);
		 
		 
		 ClusterEvaluation eval = new SumOfCentroidSimilarities(); 
		 score_simil=eval.score(clusters);
		//System.out.println("le score centroide Similarite :"+score_simil+"\n");
		
		ClusterEvaluation eval2 = new SumOfAveragePairwiseSimilarities();
		score_avg=eval2.score(clusters);
		//System.out.println("le score avg  :"+score_avg+"\n");
		
		ClusterEvaluation aic = new AICScore();
        ClusterEvaluation bic = new BICScore();
		
	//	double aicScore4 = aic.score(clusters);  //plus il est faible plus c mieux , depends du nombre de paramétre
      //  double bicScore4 = bic.score(clusters);//depends auusi du nbr d entrées
        
       // System.out.println("le score aic  :"+aicScore4+"\n");
      //  System.out.println("le score bic  :"+bicScore4+"\n");
		
		for(int i = 0; i < 10; i++) {
	        System.out.println(clusters[i]+"\n");
	    }
		
	}

}
