package ia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.repository.Repository;

import data.Aliment;
import data.User;
import engine.Engine;
//import weka.core.Instances;
//import weka.core.converters.ConverterUtils.DataSource;
//import weka.clusterers.ClusterEvaluation;
//import weka.clusterers.EM;

//import net.sf.javaml.clustering.Clusterer;
//import net.sf.javaml.clustering.KMeans;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.tools.data.FileHandler;

public class IA {

	public IA() {

	}

	///////////////////////////////////////////////////////////////////////////////
	// Weka
	///////////////////////////////////////////////////////////////////////////////
	public static int arrondirDizaine(int entry) {
		int result = 0;
		result = (int) (Math.floor(entry / 10) * 10);

		return result;
	}

	public String categorisePoids(String entry) {
		String result = null;

		int borne_inf = arrondirDizaine(Integer.valueOf(entry));
		result = String.valueOf(borne_inf) + "-" + String.valueOf(borne_inf + 10);

		return result;
	}

	public static int arrondirCinqCentaine(int entry) {
		int result = 0;
		result = (int) (Math.floor(entry / 500) * 500);

		return result;
	}

	public String categoriseBesoinCalorique(String entry) {
		String result = null;

		int borne_inf = arrondirCinqCentaine(Integer.valueOf(entry));
		result = String.valueOf(borne_inf) + "-" + String.valueOf(borne_inf + 500);

		return result;
	}

	// public void cleanData(String fileName) {
	// List<String> liste = new ArrayList<String>();
	// Engine engine = new Engine();
	//
	// String line = null;
	// String[] line_split = null;
	// String line_cleaned = null;
	// try {
	// FileReader fileReader = new FileReader(fileName);
	// BufferedReader bufferedReader = new BufferedReader(fileReader);
	//
	// while ((line = bufferedReader.readLine()) != null) {
	// line_split = line.split(";");
	// line_split[0] = "user" + line_split[0];
	// line_split[2] = categorisePoids(line_split[2]);
	// line_split[5] = categoriseBesoinCalorique(line_split[5]);
	//
	// line_cleaned = line_split[0];
	//
	// for (int i = 1; i < line_split.length; i++) {
	// line_cleaned += ";" + line_split[i];
	// }
	//
	// liste.add(line_cleaned);
	// line_cleaned = null;
	// }
	// bufferedReader.close();
	// } catch (FileNotFoundException ex) {
	// System.out.println("Unable to open file '" + fileName + "'");
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	//
	// engine.writeFile(liste, "fichiers_test/ia/profils_kmodes.txt");
	// }

	// // EM
	// public void processCluster() throws Exception {
	// DataSource source = new DataSource("fichiers_test/ia/profils.csv");
	// Instances data = source.getDataSet();
	//
	// EM clusterer = new EM();
	// ClusterEvaluation eval = new ClusterEvaluation();
	// clusterer.buildClusterer(data);
	// eval.setClusterer(clusterer);
	// eval.evaluateClusterer(data);
	// System.out.println(eval.clusterResultsToString());
	// System.out.println(eval.getNumClusters());
	// }

	///////////////////////////////////////////////////////////////////////////////
	// Java-ML
	///////////////////////////////////////////////////////////////////////////////

	public static int ageAleatoire() throws IOException {
		int min = 16;
		int max = 110;

		return min + (int) (Math.random() * (max - min));
	}

	public static double poidsAleatoire() throws IOException {
		double min = 30;
		double max = 150;

		return min + (Math.random() * (max - min));
	}

	public static double tailleAleatoire() throws IOException {
		double min = 1.4;
		double max = 2.1;

		return min + (Math.random() * (max - min));
	}

	public static String stringAleatoire(List<String> entry) throws IOException {

		int indice = (int) (Math.random() * (entry.size() - 1));

		return entry.get(indice);
	}

	public static List<String> prepareList(List<String> entry, String aucun) {
		for (int i = 0; i < (int) entry.size() / 1.1; i++) {
			entry.add(aucun);
		}
		return entry;
	}

	public void createProfil(Repository repo) {
		String fileName = "./fichiers_test/ia/profils.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);
			bw = new BufferedWriter(fw);

			User user = new User();
			Aliment aliment = new Aliment();
			String current_user = "";
			List<String> genres = prepareList(user.getAllGenreFromDB(repo), "Aucun_genre");
			List<String> maladies = prepareList(user.getAllMaladieFromDB(repo), "Aucun_maladie");
			List<String> allergies = prepareList(user.getAllAllergiesFromDB(repo), "Aucun_allergie");
			List<String> regimes = prepareList(user.getAllRegimeAlimentaireFromDB(repo), "Aucun_regime");
			List<String> niveaux_acts = prepareList(user.getAllNiveauActiviteFromDB(repo), "Aucun_niveauact");

			int indice = 0;
			String line = "";

			int age = 0;
			double poids = 0;
			double taille = 0;
			String genre = "";
			String maladie = "";
			String allergie = "";
			String regime = "";
			String niveau_act = "";
			String aucun = "Aucun";

			while (indice < 100000) {
				current_user = "user" + String.valueOf(indice);
				age = ageAleatoire();
				poids = poidsAleatoire();
				taille = tailleAleatoire();
				genre = stringAleatoire(genres);
				maladie = stringAleatoire(maladies);
				allergie = stringAleatoire(allergies);
				regime = stringAleatoire(regimes);
				niveau_act = stringAleatoire(niveaux_acts);

				line = current_user + ";" + String.valueOf(age) + ";" + String.valueOf(poids) + ";"
						+ String.valueOf(taille) + ";" + genre + ";" + maladie + ";" + allergie + ";" + regime + ";"
						+ niveau_act;

				bw.write(line + "\n");
				indice++;
			}

			System.out.println("End");
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

	public int getIndiceFromValue(List<String> liste, String value) {
		int indice = -1;

		for (int i = 0; i < liste.size(); i++) {
			if (liste.get(i).equals(value)) {
				return i;
			}
		}
		return indice;
	}

	public void cleanData(Repository repo, String fileName, String fileNameDest) {
		FileWriter fw = null;
		BufferedWriter bw = null;

		User user = new User();
		Engine engine = new Engine();

		List<String> genres = user.getAllGenreFromDB(repo);
		List<String> maladies = user.getAllMaladieFromDB(repo);
		List<String> allergies = user.getAllAllergiesFromDB(repo);
		List<String> regimes = user.getAllRegimeAlimentaireFromDB(repo);
		List<String> niveaux_acts = user.getAllNiveauActiviteFromDB(repo);
		genres.add("Aucun_genre");
		maladies.add("Aucun_maladie");
		allergies.add("Aucun_allergie");
		regimes.add("Aucun_regime");
		niveaux_acts.add("Aucun_niveauact");

		int i = 0;
		int j = 0;
		int indice = 0;

		String line = null;
		String[] line_split = null;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			fw = new FileWriter(fileNameDest, true);
			bw = new BufferedWriter(fw);

			bw.write("login;age;poids;taille");
			
			for (i = 0; i < genres.size(); i++) {
				bw.write(";" + engine.lowerCaseAll(genres.get(i)));
			}
			for (i = 0; i < maladies.size(); i++) {
				bw.write(";" + engine.lowerCaseAll(maladies.get(i)));
			}
			for (i = 0; i < allergies.size(); i++) {
				bw.write(";" + engine.lowerCaseAll(allergies.get(i)));
			}
			for (i = 0; i < regimes.size(); i++) {
				bw.write(";" + engine.lowerCaseAll(regimes.get(i)));
			}
			for (i = 0; i < niveaux_acts.size(); i++) {
				bw.write(";" + engine.lowerCaseAll(niveaux_acts.get(i)));
			}

			bw.write("\n");

			while ((line = bufferedReader.readLine()) != null) {
//				System.out.println(line);
				line_split = line.split(";");

				for (i = 0; i < line_split.length; i++) {
					if (i == 0) {
						bw.write(line_split[0]);
					} else if (i > 0 && i < 4) {
						bw.write(";" + line_split[i]);
					} else if (i == 4) {
						for (j = 0; j < genres.size(); j++) {
							if (genres.get(j).equals(line_split[4])) {
								bw.write(";1");
							} else {
								bw.write(";0");
							}
						}
					} else if (i == 5) {
						for (j = 0; j < maladies.size(); j++) {
							if (maladies.get(j).equals(line_split[5])) {
								bw.write(";1");
							} else {
								bw.write(";0");
							}
						}
					} else if (i == 6) {
						for (j = 0; j < allergies.size(); j++) {
							if (allergies.get(j).equals(line_split[6])) {
								bw.write(";1");
							} else {
								bw.write(";0");
							}
						}
					} else if (i == 7) {
						for (j = 0; j < regimes.size(); j++) {
							if (regimes.get(j).equals(line_split[7])) {
								bw.write(";1");
							} else {
								bw.write(";0");
							}
						}
					} else if (i == 8) {
						for (j = 0; j < niveaux_acts.size(); j++) {
							if (niveaux_acts.get(j).equals(line_split[8])) {
								bw.write(";1");
							} else {
								bw.write(";0");
							}
						}
						bw.write("\n");
					}
				}
//				indice++;
//				System.out.println(indice);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
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
	
//	public List<String> cleanData(Repository repo, String fileName) {
//		FileWriter fw = null;
//		BufferedWriter bw = null;
//
//		User user = new User();
//		Engine engine = new Engine();
//
//		List<String> genres = user.getAllGenreFromDB(repo);
//		List<String> maladies = user.getAllMaladieFromDB(repo);
//		List<String> allergies = user.getAllAllergiesFromDB(repo);
//		List<String> regimes = user.getAllRegimeAlimentaireFromDB(repo);
//		List<String> niveaux_acts = user.getAllNiveauActiviteFromDB(repo);
//		genres.add("aucun");
//		maladies.add("aucun");
//		allergies.add("aucun");
//		regimes.add("aucun");
//		niveaux_acts.add("aucun");
//
//		int i = 0;
//		int j = 0;
//		int indice = 0;
//
//		List<String> liste = new ArrayList<String>();
//		String tmp = null;
//		String line = null;
//		String[] line_split = null;
//
//		try {
//			FileReader fileReader = new FileReader(fileName);
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//			tmp = "login;age;poids;taille";
//			
//			for (i = 0; i < genres.size(); i++) {
//				tmp += ";" + engine.lowerCaseAll(genres.get(i));
//			}
//			for (i = 0; i < maladies.size(); i++) {
//				tmp += ";" + engine.lowerCaseAll(maladies.get(i));
//			}
//			for (i = 0; i < allergies.size(); i++) {
//				tmp += ";" + engine.lowerCaseAll(allergies.get(i));
//			}
//			for (i = 0; i < regimes.size(); i++) {
//				tmp += ";" + engine.lowerCaseAll(regimes.get(i));
//			}
//			for (i = 0; i < niveaux_acts.size(); i++) {
//				tmp += ";" + engine.lowerCaseAll(niveaux_acts.get(i));
//			}
//			
//			System.out.println(tmp);
//
//			liste.add(tmp);
//
//			while ((line = bufferedReader.readLine()) != null) {
////				System.out.println(line);
//				line_split = line.split(";");
//
//				for (i = 0; i < line_split.length; i++) {
//					if (i == 0) {
//						tmp = line_split[0];
//					}
//					if (i > 0 && i < 4) {
//						tmp += ";" + line_split[i];
//					}
//					if (i == 4) {
//						for (j = 0; j < genres.size(); i++) {
//							System.out.println(genres.get(j));
//							if (genres.get(j).equals(engine.lowerCaseAll(line_split[4]))) {
//								tmp += ";1";
//							} else {
//								tmp += ";0";
//							}
//						}
//					}
//					if (i == 5) {
//						for (j = 0; j < maladies.size(); i++) {
//							System.out.println(maladies.get(j));
//							if (maladies.get(j).equals(engine.lowerCaseAll(line_split[5]))) {
//								tmp += ";1";
//							} else {
//								tmp += ";0";
//							}
//						}
//					}
//					if (i == 6) {
//						for (j = 0; j < allergies.size(); i++) {
//							System.out.println(allergies.get(j));
//							if (allergies.get(j).equals(engine.lowerCaseAll(line_split[6]))) {
//								tmp += ";1";
//							} else {
//								tmp += ";0";
//							}
//						}
//					}
//					if (i == 7) {
//						for (j = 0; j < regimes.size(); i++) {
//							System.out.println(regimes.get(j));
//							if (regimes.get(j).equals(engine.lowerCaseAll(line_split[7]))) {
//								tmp += ";1";
//							} else {
//								tmp += ";0";
//							}
//						}
//					}
//					if (i == 8) {
//						for (j = 0; j < niveaux_acts.size(); i++) {
//							System.out.println(niveaux_acts.get(j));
//							if (niveaux_acts.get(j).equals(engine.lowerCaseAll(line_split[8]))) {
//								tmp += ";1";
//							} else {
//								tmp += ";0";
//							}
//						}
//					}
//					liste.add(tmp);
//				}
//				indice++;
//				System.out.println(indice);
//			}
//			bufferedReader.close();
//		} catch (FileNotFoundException ex) {
//			System.out.println("Unable to open file '" + fileName + "'");
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		
//		return liste;
//	}
	
//	public void processCleanData(Repository repo, String fileName, String fileNameDest){
//		List<String> liste = new ArrayList<>();
//		liste = cleanData(repo, fileName);
//		
//		Engine en = new Engine();
//		en.writeFile(liste, fileNameDest);
//	}

	// public void processCluster() throws IOException{
	// Dataset data = FileHandler.loadDataset(new
	// File("./fichiers_test/ia/profils.txt"), ";");
	//
	// Clusterer km = new KMeans();
	// Dataset[] clusters = km.cluster(data);
	//
	// for(int i=0; i<clusters.length;i++){
	// System.out.println(clusters[i].size());
	// }
	// }

}
