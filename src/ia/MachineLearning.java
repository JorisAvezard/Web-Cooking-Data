package ia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfAveragePairwiseSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.MahalanobisDistance;
import net.sf.javaml.tools.data.FileHandler;

public class MachineLearning {

	public MachineLearning() {

	}

	public static Dataset[] Kmean(String filename) throws Exception {
		/* We load some data */
		// fichiers_test/ia/profils_nettoyes.txt
		double score = 0.0;
		double score_simil = 0.0;
		double score_avg = 0.0;
		Dataset data0 = FileHandler.loadDataset(new File(filename), ";");
		Dataset data = remove_id(data0);
		/*
		 * We create a clustering algorithm, in this case the k-means algorithm
		 * with 4 clusters.
		 */
		Clusterer km = new KMeans();

		/* We cluster the data */
		Dataset[] clusters = km.cluster(data);
		/* Create a measure for the cluster quality */
		// System.out.println(data);
		ClusterEvaluation sse = new SumOfSquaredErrors();
		/* Measure the quality of the clustering */
		score = sse.score(clusters);
		System.out.println("le taux erreur :" + score + "\n");
		// System.out.println("Cluster count: " + clusters.length);

		ClusterEvaluation eval = new SumOfCentroidSimilarities();
		score_simil = eval.score(clusters);
		// System.out.println("le score centroide Similarite
		// :"+score_simil+"\n");

		ClusterEvaluation eval2 = new SumOfAveragePairwiseSimilarities();
		score_avg = eval2.score(clusters);
		// System.out.println("le score avg :"+score_avg+"\n");

		ClusterEvaluation aic = new AICScore();
		ClusterEvaluation bic = new BICScore();

		// double aicScore4 = aic.score(clusters); //plus il est faible plus c
		// mieux , depends du nombre de param�tre
		// double bicScore4 = bic.score(clusters);//depends auusi du nbr d
		// entr�es

		// System.out.println("le score aic :"+aicScore4+"\n");
		// System.out.println("le score bic :"+bicScore4+"\n");

		// for(int i = 0; i < clusters.length; i++) {
		// System.out.println(clusters[i]+"\n");
		// // System.out.println(clusters[i].instance(6)+"\n");
		// }
		return clusters;
	}

	public static double[] convertStringListToDoubleList(String[] entry) {
		double[] result = new double[entry.length-1];
		int i = 0;

		for (i = 1; i < entry.length; i++) {
			result[i-1] = Double.valueOf(entry[i]);
		}

		return result;
	}

//	public void processKMean(List<String> profils, String fichier_de_sortie) throws Exception {
//		double score = 0.0;
//		Dataset data = new DefaultDataset();
//		int i = 0;
//		int j = 0;
//
//		double[] profil_split = null;
//		
//		for (i = 0; i < profils.size(); i++) {
//			profil_split = convertStringListToDoubleList(profils.get(i).split(";"));
//			Instance instance = new DenseInstance(profil_split);
//			data.add(instance);
//		}
//
//		Clusterer km = new KMeans(15);
//		Dataset[] clusters = km.cluster(data);
//		ClusterEvaluation sse = new SumOfSquaredErrors();
//		score = sse.score(clusters);
//		System.out.println("le taux erreur :" + score + "\n");
//
//		String val = "";
//		BufferedWriter bw = null;
//		FileWriter fw = null;
//		try {
//			fw = new FileWriter(fichier_de_sortie);
//			bw = new BufferedWriter(fw);
//			for (i = 0; i < clusters.length; i++) {
//				for (j = 0; j < clusters[i].size(); j++) {
//					val = i + ";" + clusters[i].instance(j).get(0);
//					for (int k = 1; k < clusters[i].instance(j).size(); k++) {
//						val += "," + clusters[i].instance(j).get(k);
//					}
//					bw.write(val + "\n");
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (bw != null)
//					bw.close();
//				if (fw != null)
//					fw.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//	}
	
	public void processKMean(String fichier_profils_nettoyes, String fichier_resultat_cluster) throws Exception {
		double score = 0.0;
		int i = 0;
		int j = 0;
		int k = 0;

		Dataset data0 = FileHandler.loadDataset(new File(fichier_profils_nettoyes), ";");
		Dataset data = remove_id(data0);

		Clusterer km = new KMeans(31);
		Dataset[] clusters = km.cluster(data);
		ClusterEvaluation sse = new SumOfSquaredErrors();
		score = sse.score(clusters);
		System.out.println("le taux erreur :" + score + "\n");

		String val = "";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fichier_resultat_cluster);
			bw = new BufferedWriter(fw);
			for (i = 0; i < clusters.length; i++) {
				for (j = 0; j < clusters[i].size(); j++) {
					val = i + ";" + clusters[i].instance(j).get(0);
					for (k = 1; k < clusters[i].instance(j).size(); k++) {
						val += "," + clusters[i].instance(j).get(k);
					}
					bw.write(val + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static Dataset remove_id(Dataset data) throws Exception {

		for (int i = 0; i < data.size(); i++) {
			data.instance(i).removeAttribute(0);
		}

		return data;
	}

	// methode enregistrer le resultat du cluster
	public static void save_cluster(String fichier_profils_nettoyes, String fichier_de_sortie) throws Exception {
		Dataset[] clusters = Kmean(fichier_profils_nettoyes);
		String val = "";

		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fichier_de_sortie);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < clusters.length; i++) {
				for (int j = 0; j < clusters[i].size(); j++) {
					val = i + ";" + clusters[i].instance(j).get(0);
					for (int k = 1; k < clusters[i].instance(j).size(); k++) {
						val += "," + clusters[i].instance(j).get(k);
					}
					bw.write(val + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static ArrayList<String> find_cluster_user(String fichier_resultat_cluster, String profil)
			throws IOException {
		ArrayList<String> all_user = new ArrayList<String>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] resul = new String[2];
		FileReader fr = new FileReader(fichier_resultat_cluster);
		BufferedReader br = new BufferedReader(fr);
		String users = null;
		int indice = 0;
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			resul = line.split(";");
			result.add(resul);
		}
		br.close();
		fr.close();
		for (String[] arr : result) {
			if (profil.equals(arr[1])) {
				users = arr[0];
			}
		}
		
		for (String[] arr : result) {
			if(indice<1000){
				if (arr[0].equals(users)) {
//					String p = arr[1].replaceAll(",", ";");
					all_user.add(arr[1]);
					indice++;
				}
			}else{
				break;
			}
		}

		return all_user;
	}

	public static int getNumberAttributesLine(String fileName) {
		int i = 0;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			String[] line_split = null;

			while ((line = bufferedReader.readLine()) != null) {
				line_split = line.split(";");
				i = line_split.length;
				break;
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return i;
	}

	public static ArrayList<String> id_user(String fichier_profils_nettoyes, List<String> cluster_user)
			throws IOException {
		ArrayList<String> all_id = new ArrayList<String>();
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] resul = new String[getNumberAttributesLine(fichier_profils_nettoyes)];
		FileReader fr = new FileReader(fichier_profils_nettoyes);
		BufferedReader br = new BufferedReader(fr);
		String profil = null;
		int i = 0;
		int indice = 0;
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			String[] resul2 = new String[2];
			resul = line.split(";");

			profil = resul[1];
			
//			System.out.println(cluster_user.get(0));

//			for (i = 2; i < resul.length; i++) {
//				profil += ";" + resul[i];
//			}

			resul2[0] = resul[0];
			resul2[1] = profil;
			
//			System.out.println("resul2[0] : " + resul2[0]);
//			System.out.println("resul2[1] : " + resul2[1]);

			result.add(resul2);
//			System.out.println("result : " + result.get(0));
		}
		br.close();
		fr.close();

		for (int p = 0; p < cluster_user.size(); p++) {
			for (String[] arr : result) {
				if(indice<1000){
					if (cluster_user.get(p).equals(arr[1])) {
//						System.out.println("cluster_user : " + cluster_user.get(p));
//						System.out.println("arr[1] : " + arr[1]);
						all_id.add(arr[0]);
						indice++;
					}
				}else{
					break;
				}
			}
		}

		return all_id;
	}

//	public static ArrayList<String> id_user(List<String> profils, List<String> cluster_user)
//			throws IOException {
//		ArrayList<String> all_id = new ArrayList<String>();
//		ArrayList<String[]> result = new ArrayList<String[]>();
//		String profil = null;
//		int i = 0;
//		for (int j=0;j<profils.size();j++) {
//			String[] resul2 = new String[2];
//			String [] resul = profils.get(j).split(";");
//
//			profil = resul[1];
//
//			for (i = 2; i < resul.length; i++) {
//				profil += ";" + resul[i];
//			}
//
//			resul2[0] = resul[0];
//			resul2[1] = profil;
//
//			result.add(resul2);
//		}
//	
//		for (int p = 0; p < cluster_user.size(); p++) {
//			for (String[] arr : result) {
//				if (cluster_user.get(p).equals(arr[1])) {
//					all_id.add(arr[0]);
//				}
//			}
//		}
//		for ( int f=0;f<all_id.size();f++){
//			System.out.println(all_id.get(f));
//		}
//
//		return all_id;
//	}
	
}