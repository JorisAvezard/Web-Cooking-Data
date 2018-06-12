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

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.repository.Repository;

import data.Aliment;
import data.Recette;
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

	public static List<String> prepareList(List<String> entry) {
		for (int i = 0; i < (int) entry.size() / 1.1; i++) {
			entry.add("Aucun");
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
			List<String> genres = prepareList(user.getAllGenreFromDB(repo));
			List<String> maladies = prepareList(user.getAllMaladieFromDB(repo));
			List<String> allergies = prepareList(user.getAllAllergiesFromDB(repo));
			List<String> regimes = prepareList(user.getAllRegimeAlimentaireFromDB(repo));
			List<String> niveaux_acts = prepareList(user.getAllNiveauActiviteFromDB(repo));

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
			// String aucun = "Aucun";

			while (indice < 40000) {
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
		int i = 0;
		for (i = 0; i < liste.size(); i++) {
			if (liste.get(i).equals(value)) {
				return i;
			}
		}
		return -1;
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

			// bw.write("login;age;poids;taille");
			//
			// for (i = 0; i < genres.size(); i++) {
			// bw.write(";" + engine.lowerCaseAll(genres.get(i)));
			// }
			// for (i = 0; i < maladies.size(); i++) {
			// bw.write(";" + engine.lowerCaseAll(maladies.get(i)));
			// }
			// for (i = 0; i < allergies.size(); i++) {
			// bw.write(";" + engine.lowerCaseAll(allergies.get(i)));
			// }
			// for (i = 0; i < regimes.size(); i++) {
			// bw.write(";" + engine.lowerCaseAll(regimes.get(i)));
			// }
			// for (i = 0; i < niveaux_acts.size(); i++) {
			// bw.write(";" + engine.lowerCaseAll(niveaux_acts.get(i)));
			// }
			//
			// bw.write("\n");

			while ((line = bufferedReader.readLine()) != null) {
				// System.out.println(line);
				line_split = line.split(";");

				for (i = 0; i < line_split.length; i++) {
					if (i == 0) {
						bw.write(line_split[0]);
					} else if (i == 1) {
						bw.write(";" + String.valueOf(Double.valueOf(line_split[i])));
					} else if ((i == 2) || (i == 3)) {
						bw.write(";" + line_split[i]);
					} else if (i == 4) {
						if (line_split[4].equals("Aucun")) {
							for (j = 0; j < genres.size(); j++) {
								bw.write(";0.0");
							}
						} else {
							for (j = 0; j < genres.size(); j++) {
								if (genres.get(j).equals(line_split[4])) {
									bw.write(";1.0");
								} else {
									bw.write(";0.0");
								}
							}
						}
					} else if (i == 5) {
						if (line_split[5].equals("Aucun")) {
							for (j = 0; j < maladies.size(); j++) {
								bw.write(";0.0");
							}
						} else {
							for (j = 0; j < maladies.size(); j++) {
								if (maladies.get(j).equals(line_split[5])) {
									bw.write(";1.0");
								} else {
									bw.write(";0.0");
								}
							}
						}
					} else if (i == 6) {
						if (line_split[6].equals("Aucun")) {
							for (j = 0; j < allergies.size(); j++) {
								bw.write(";0.0");
							}
						} else {
							for (j = 0; j < allergies.size(); j++) {
								if (allergies.get(j).equals(line_split[6])) {
									bw.write(";1.0");
								} else {
									bw.write(";0.0");
								}
							}
						}
					} else if (i == 7) {
						if (line_split[7].equals("Aucun")) {
							for (j = 0; j < regimes.size(); j++) {
								bw.write(";0.0");
							}
						} else {
							for (j = 0; j < regimes.size(); j++) {
								if (regimes.get(j).equals(line_split[7])) {
									bw.write(";1.0");
								} else {
									bw.write(";0.0");
								}
							}
						}
					} else if (i == 8) {
						if (line_split[8].equals("Aucun")) {
							for (j = 0; j < niveaux_acts.size(); j++) {
								bw.write(";0.0");
							}
						} else {
							for (j = 0; j < niveaux_acts.size(); j++) {
								if (niveaux_acts.get(j).equals(line_split[8])) {
									bw.write(";1.0");
								} else {
									bw.write(";0.0");
								}
							}
						}
						bw.write("\n");
					}
				}
				// indice++;
				// System.out.println(indice);
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

	public void creerDonneesUserAimeRecette(Repository repo, ValueFactory vf, Model model, String wcd) {
		Recette rec = new Recette();
		User us = new User();

		List<String> recettes = rec.getAllNamesRecettes(repo);

		int i = 0;
		int j = 0;
		int indice_rec = 0;
		int max = recettes.size();
		int nombre_user_recettes = 0;

		for (i = 0; i < 5000; i++) {
			nombre_user_recettes = (int) (Math.random() * (11));

			if (nombre_user_recettes > 0) {
				for (j = 0; j < nombre_user_recettes; j++) {
					indice_rec = (int) (Math.random() * (max));
					us.addAimeRecette(repo, vf, model, wcd, "user" + String.valueOf(i), recettes.get(indice_rec));
					System.out.println(i);
				}
			}
		}
	}

	public List<String> tableDeVote(Repository repo, List<String> users) {
		User us = new User();

		List<String> user_recettes = new ArrayList<String>();
		List<String> recettes_aimees = new ArrayList<String>();
		List<String> recettes_finales = new ArrayList<String>();
		List<Integer> votes = new ArrayList<Integer>();

		int i = 0;
		int j = 0;
		int indice_recette_aimee = -1;

		for (i = 0; i < users.size(); i++) {
			user_recettes = us.getAimeRecette(repo, users.get(i));

			for (j = 0; j < user_recettes.size(); j++) {
				if (recettes_aimees.contains(user_recettes.get(j))) {
					indice_recette_aimee = getIndiceFromValue(recettes_aimees, user_recettes.get(j));
					votes.set(indice_recette_aimee, votes.get(indice_recette_aimee) + 1);
				} else {
					recettes_aimees.add(user_recettes.get(j));
					votes.add(1);
				}
			}
		}

		int moyenne = 0;

		for (i = 0; i < votes.size(); i++) {
			moyenne = moyenne + votes.get(i);
		}

		moyenne = ((int) moyenne / votes.size()) + 1;

		for (i = 0; i < recettes_aimees.size(); i++) {
			if (votes.get(i) >= moyenne) {
				recettes_finales.add(recettes_aimees.get(i));
			}
		}

		return recettes_finales;
	}

	public List<String> suggestionRecetteParCluster(Repository repo, String fichier_profils_nettoyes,
			String fichier_resultat_cluster, String user_profil) throws IOException {
		MachineLearning ml = new MachineLearning();
		List<String> cluster_user = ml.find_cluster_user(fichier_resultat_cluster, user_profil);
		List<String> users_id = ml.id_user(fichier_profils_nettoyes, cluster_user);
		List<String> recettes_retournees = tableDeVote(repo, users_id);

		return recettes_retournees;
	}

}
