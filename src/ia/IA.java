package ia;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

public class IA {
	
	public IA(){
		
	}
	
	public void processCluster() throws IOException{
		Dataset data = FileHandler.loadDataset(new File("./fichiers_test/ia/profils.txt"), ";");
		
		 Clusterer km = new KMeans();
		 Dataset[] clusters = km.cluster(data);
		 
		 for(int i=0; i<clusters.length;i++){
			 System.out.println(clusters[i].size());
		 }
	}

}
