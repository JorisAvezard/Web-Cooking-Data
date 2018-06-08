package ia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfAveragePairwiseSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfCentroidSimilarities;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.MahalanobisDistance;
import net.sf.javaml.tools.data.FileHandler;

public class MachineLearning {

	public MachineLearning() {

	}

	public static Dataset[] Kmean() throws Exception {
		/* We load some data */
		double score = 0.0;
		double score_simil = 0.0;
		double score_avg = 0.0;
		Dataset data0 = FileHandler.loadDataset(new File("fichiers_test/ia/profils_nettoyes.txt"), ";");
		Dataset data = remove_id(data0);
		/*
		 * We create a clustering algorithm, in this case the k-means algorithm
		 * with 4 clusters.
		 */
		Clusterer km = new KMeans();
		// System.out.println(data);
		/* We cluster the data */

		Dataset[] clusters = km.cluster(data);
		/* Create a measure for the cluster quality */
		System.out.println(data);
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

	public static Dataset remove_id(Dataset data) throws Exception {

		for (int i = 0; i < data.size(); i++) {
			// for(int j=0;j<data.instance(i).size();j++){
			data.instance(i).removeAttribute(0);
		}
		// System.out.println(data+"\n");
		return data;
	}

	// prend en entre le profil user sous forme de liste

	public static Dataset findProfil(ArrayList liste) throws Exception {
		Dataset[] clusters = Kmean();

		Dataset clusters_final = null;
		for (int i = 0; i < clusters.length; i++) {
			// System.out.println(i+"\n");
			for (int j = 0; j < clusters[i].size(); j++) {
				// System.out.println(clusters[i]+"\n");
				// System.out.println(j+"\n");
				ArrayList<String> ll = new ArrayList<String>();
				for (int k = 0; k < clusters[i].instance(j).size(); k++) {
					ll.add(Double.toString(clusters[i].instance(j).get(k)));
				}
				for (int p = 0; p < ll.size(); p++) {
					// System.out.println(ll.get(p)+"\n");
					// System.out.println("----------------------------------------------------------"+"\n");
				}
				if (ll.equals(liste)) {

					System.out.println(ll + "\n");
					clusters_final = clusters[i];
					System.out.println(clusters_final + "\n");
				}
				// System.out.println("------------------------------------------------"+"\n");
			}

		}

		return clusters_final;
	}

	public static ArrayList chargerProfil(String profil) {
		ArrayList profils = new ArrayList();
		String[] parts = profil.split(",");
		for (int i = 0; i < parts.length; i++) {
			profils.add(parts[i]);
		}
		for (int j = 0; j < profils.size(); j++) {
			System.out.println(profils.get(j) + "\n");
		}
		return profils;
	}

	// methode enregistrer le resultat du cluster
	public static void save_cluster(String NameFile) throws Exception {
		String fileName = "fichiers_test/ia/" + NameFile + ".txt";
		Dataset[] clusters = Kmean();

		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < clusters.length; i++) {
				for (int j = 0; j < clusters[i].size(); j++) {
					ArrayList<String> list = new ArrayList<String>();
					String val = null;
					for (int k = 0; k < clusters[i].instance(j).size(); k++) {

						// System.out.println(clusters[i].get(j)+"\n");
						val = i + ";" + clusters[i].instance(j).get(0) + "," + clusters[i].instance(j).get(1) + ","
								+ clusters[i].instance(j).get(2) + "," + clusters[i].instance(j).get(3) + ","
								+ clusters[i].instance(j).get(4) + "," + clusters[i].instance(j).get(5) + ","
								+ clusters[i].instance(j).get(6) + "," + clusters[i].instance(j).get(7) + ","
								+ clusters[i].instance(j).get(8) + "," + clusters[i].instance(j).get(9) + ","
								+ clusters[i].instance(j).get(10) + "," + clusters[i].instance(j).get(11) + ","
								+ clusters[i].instance(j).get(12) + "," + clusters[i].instance(j).get(13) + ","
								+ clusters[i].instance(j).get(14) + "," + clusters[i].instance(j).get(15) + ","
								+ clusters[i].instance(j).get(16) + "," + clusters[i].instance(j).get(17) + ","
								+ clusters[i].instance(j).get(18) + "," + clusters[i].instance(j).get(19) + ","
								+ clusters[i].instance(j).get(20) + "," + clusters[i].instance(j).get(21) + "," + clusters[i].instance(j).get(22) + ","
								+ clusters[i].instance(j).get(23) + "," + clusters[i].instance(j).get(24) + ","
								+ clusters[i].instance(j).get(25) + "," + clusters[i].instance(j).get(26) + ","
								+ clusters[i].instance(j).get(27) + "," + clusters[i].instance(j).get(28);
					}
					bw.write(val + "\n");
					// System.out.println("Done");
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

	public static ArrayList<String> find_cluster_user(String profil) throws IOException {
		// charger le fichier qui contient le resultat du cluster
		ArrayList<String> all_user = new ArrayList<String>();
		String file = "fichiers_test/ia/resultat_cluster.txt";
		File filecluster = new File(file);
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] resul = new String[2];
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String users = null;
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			// System.out.println( line+"\n");
			resul = line.split(";");
			result.add(resul);
		}
		br.close();
		fr.close();
		for (String[] arr : result) {
			String id_cluster = arr[0];
			String profil_user = arr[1];
			if (profil.equals(arr[1])) {
				users = arr[0];
			}
		}
		for (String[] arr : result) {
			if (arr[0].equals(users)) {
				String p = arr[1].replaceAll(",", ";");
				all_user.add(p);
			}
		}

		// System.out.println(Arrays.toString(arr));
		System.out.println("le profil appartient au cluster " + users);
		for (int k = 0; k < all_user.size(); k++) {
			// System.out.println("profil numero "+k+" "+all_user.get(k));
		}
		return all_user;
	}

	public static ArrayList<String> id_user(List<String> cluster_user) throws IOException {
		// charger le fichier qui contient le resultat du cluster
		ArrayList<String> all_id = new ArrayList<String>();
		String file = "fichiers_test/ia/profils_nettoyes.txt";
		File filecluster = new File(file);
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] resul = new String[29];

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String users = null;
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			String[] resul2 = new String[2];
			String id = null;
			String profil = null;
			// System.out.println( line+"\n");
			resul = line.split(";");
			id = resul[0];
			profil = resul[1] + ";" + resul[2] + ";" + resul[3] + ";" + resul[4] + ";" + resul[5] + ";" + resul[6] + ";"
					+ resul[7] + ";" + resul[8] + ";" + resul[9] + ";" + resul[10] + ";" + resul[11] + ";" + resul[12]
					+ ";" + resul[13] + ";" + resul[14] + ";" + resul[15] + ";" + resul[16] + ";" + resul[17] + ";"
					+ resul[18] + ";" + resul[19] + ";" + resul[20] + ";" + resul[21] + ";" + resul[22] + ";"
					+ resul[23] + ";" + resul[24] + ";" + resul[25] + ";" + resul[26] + ";" + resul[27] + ";"
					+ resul[28] + ";" + resul[29];

			// System.out.println("---------------------------------------
			// "+profil);
			// System.out.println("---------------------------------------
			// "+id);

			resul2[0] = id;
			resul2[1] = profil;

			// System.out.println(
			// "************************"+Arrays.toString(resul2)+"\n");
			result.add(resul2);
		}
		br.close();
		fr.close();

		for (int i = 0; i < result.size(); i++) {
			// System.out.println(
			// "//////////////////////"+Arrays.toString(result.get(i))+"\n");
		}

		for (int p = 0; p < cluster_user.size(); p++) {
			for (String[] arr : result) {
				String id_user = arr[0];
				// System.out.println( "id ::::"+id_user+"\n");
				String profil_user = arr[1];
				// System.out.println( "profil ::::"+profil_user+"\n");
//				System.out.println("cluster_user : " + cluster_user.get(p));
//				System.out.println("profil_user : " + profil_user);
				if (cluster_user.get(p).equals(profil_user)) {
					all_id.add(id_user);
				}
			}
		}

		// System.out.println(Arrays.toString(arr));
		// System.out.println("le profil appartient au cluster "+users);
		for (int k = 0; k < all_id.size(); k++) {
			System.out.println("id est " + k + " " + all_id.get(k));
		}
		return all_id;
	}

	// public static void main(String[] args) throws Exception {
	//
	// // Kmean();
	// // ArrayList profil=new ArrayList();
	// // profil=chargerProfil("4.0,1.0,30.0,1011.0,11.0,1040.0,10.0");
	// // findProfil(profil);
	// save_cluster("resultat_cluster");
	//
	// //
	// profil=find_cluster_user("25.0,136.6961201117902,1.5894170109599939,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0");
	// // for(int i=0;i<profil.size();i++){
	// // System.out.println(profil.get(i));
	// // }
	// // id_user(profil);
	//
	// }

}