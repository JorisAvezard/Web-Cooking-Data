package Intelligence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfAveragePairwiseSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;
import weka.classifiers.trees.lmt.LogisticBase;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

import weka.core.converters.ArffSaver;
import weka.core.converters.C45Saver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;

import java.io.File;
 
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instances;

public class Preprocess {


	public static void main(String[] args) throws Exception {
		/* We load some data */
		DataSource source = new DataSource("C:/Users/nassima/Desktop/extraction/fichier_extraction/Allergies.csv");
		Instances data = source.getDataSet();
	    Instances filteredData = new Instances(data);	
	    System.out.println(data);
		NominalToBinary nominalToBinary = new NominalToBinary();			
		nominalToBinary.setInputFormat(filteredData);
		String[] options = {"-A"};
		nominalToBinary.setOptions(options);
		filteredData = Filter.useFilter(filteredData, nominalToBinary);	
		 System.out.println(filteredData);
		 
		 //enregistrer dans un csv
		 CSVSaver saver = new CSVSaver();
		 saver.setFieldSeparator(",");
		 saver.setInstances(filteredData);
		 final File tmpFile =
			        Paths.get(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString(), ".csv")
			            .toFile();
			    saver.setFile(tmpFile);
			    saver.setNoHeaderRow(true);
			    saver.writeBatch();
			    loadEmbeddingFromCSV(tmpFile);
			    tmpFile.delete();
		 
	}

}