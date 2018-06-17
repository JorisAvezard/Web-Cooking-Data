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

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

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
		int min = 18;
		int max = 90;

		return min + (int) (Math.random() * (max - min));
	}

	// public static double poidsAleatoire() throws IOException {
	// double min = 30;
	// double max = 150;
	//
	// return min + (Math.random() * (max - min));
	// }

	public static double poidsAleatoire(String genre, int taille) throws IOException {
		double coef = -1;
		
		if(genre.equals("Femme")){
			coef = 2.5;
		}else{
			coef = 4;
		}
		
		double poids_ideal = taille - 100 - ((taille - 150) / coef);
		double min = poids_ideal - 10;
		double max = poids_ideal + 40;

		return min + (Math.random() * (max - min));
	}

	public static int tailleAleatoire() throws IOException {
		int min = 150;
		int max = 210;

		return min + (int) (Math.random() * (max - min));
	}

	public static String stringAleatoire(List<String> entry) throws IOException {

		int indice = (int) (Math.random() * (entry.size()));

		return entry.get(indice);
	}

	public static List<String> prepareList(List<String> entry) {
		for (int i = 0; i < (int) entry.size() / 1.1; i++) {
			entry.add("Aucun");
		}
		return entry;
	}

	// public void createProfil(Repository repo) {
	// String fileName = "./fichiers_test/ia/profils.txt";
	// BufferedWriter bw = null;
	// FileWriter fw = null;
	// try {
	// fw = new FileWriter(fileName, true);
	// bw = new BufferedWriter(fw);
	//
	// User user = new User();
	// Aliment aliment = new Aliment();
	// String current_user = "";
	// List<String> genres = prepareList(user.getAllGenreFromDB(repo));
	// List<String> maladies = prepareList(user.getAllMaladieFromDB(repo));
	// List<String> allergies = prepareList(user.getAllAllergiesFromDB(repo));
	// List<String> regimes =
	// prepareList(user.getAllRegimeAlimentaireFromDB(repo));
	// List<String> niveaux_acts =
	// prepareList(user.getAllNiveauActiviteFromDB(repo));
	//
	// int indice = 0;
	// String line = "";
	//
	// int age = 0;
	// double poids = 0;
	// double taille = 0;
	// String genre = "";
	// String maladie = "";
	// String allergie = "";
	// String regime = "";
	// String niveau_act = "";
	// // String aucun = "Aucun";
	//
	// while (indice < 40000) {
	// current_user = "user" + String.valueOf(indice);
	// age = ageAleatoire();
	// poids = poidsAleatoire();
	// taille = tailleAleatoire();
	// genre = stringAleatoire(genres);
	// maladie = stringAleatoire(maladies);
	// allergie = stringAleatoire(allergies);
	// regime = stringAleatoire(regimes);
	// niveau_act = stringAleatoire(niveaux_acts);
	//
	// line = current_user + ";" + String.valueOf(age) + ";" +
	// String.valueOf(poids) + ";"
	// + String.valueOf(taille) + ";" + genre + ";" + maladie + ";" + allergie +
	// ";" + regime + ";"
	// + niveau_act;
	//
	// bw.write(line + "\n");
	// indice++;
	// }
	//
	// System.out.println("End");
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (bw != null)
	// bw.close();
	// if (fw != null)
	// fw.close();
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// }
	// }

	public void createProfilsAleatoire(Repository repo) {
		String fileName = "./fichiers_test/ia/profils.txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);
			bw = new BufferedWriter(fw);

			User user = new User();
			Aliment aliment = new Aliment();
			String current_user = null;
			List<String> genres = user.getAllGenreFromDB(repo);
			List<String> maladies = prepareList(user.getAllMaladieFromDB(repo));
			List<String> allergies = prepareList(user.getAllAllergiesFromDB(repo));
			List<String> regimes = prepareList(user.getAllRegimeAlimentaireFromDB(repo));
			List<String> niveaux_acts = prepareList(user.getAllNiveauActiviteFromDB(repo));

			int indice = 0;
			String line = null;

			int age = 0;
			double poids = 0;
			int taille = 0;
			String genre = null;
			String maladie = null;
			String allergie = null;
			String regime = null;
			String niveau_act = null;

			while (indice < 40000) {
				current_user = "user" + String.valueOf(indice);
				age = ageAleatoire();
				genre = stringAleatoire(genres);
				taille = tailleAleatoire();
				poids = poidsAleatoire(genre, taille);
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

	public void insertProfilIntoDB(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		User user = new User();

		IRI predicat_age = vf.createIRI(wcd, "a_pour_age");
		IRI predicat_poids = vf.createIRI(wcd, "a_pour_poids");
		IRI predicat_taille = vf.createIRI(wcd, "a_pour_taille");
		IRI predicat_genre = vf.createIRI(wcd, "a_pour_genre");
		IRI predicat_maladie = vf.createIRI(wcd, "a_pour_maladie");
		IRI predicat_allergie = vf.createIRI(wcd, "a_pour_allergie");
		IRI predicat_regime = vf.createIRI(wcd, "a_pour_regime_alimentaire");
		IRI predicat_niveau_act = vf.createIRI(wcd, "a_pour_niveau_activite");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] line_split = line.split(";");
				System.out.println(line_split[0]);
				IRI current_user_iri = vf.createIRI(wcd, user.formatCaseResource(line_split[0]));

				model.add(current_user_iri, RDF.TYPE, FOAF.PERSON);
				model.add(current_user_iri, FOAF.NAME, vf.createLiteral(line_split[0]));
				model.add(current_user_iri, predicat_age, vf.createLiteral(line_split[1]));
				model.add(current_user_iri, predicat_poids, vf.createLiteral(line_split[2]));
				model.add(current_user_iri, predicat_taille, vf.createLiteral(line_split[3]));

				if (!line_split[4].equals("Aucun")) {
					IRI current_genre_iri = vf.createIRI(wcd, engine.formatCaseResource(line_split[4]));
					model.add(current_user_iri, predicat_genre, current_genre_iri);
				}
				if (!line_split[5].equals("Aucun")) {
					IRI current_maladie_iri = vf.createIRI(wcd, engine.formatCaseResource(line_split[5]));
					model.add(current_user_iri, predicat_maladie, current_maladie_iri);
				}
				if (!line_split[6].equals("Aucun")) {
					IRI current_allergie_iri = vf.createIRI(wcd, engine.formatCaseResource(line_split[6]));
					model.add(current_user_iri, predicat_allergie, current_allergie_iri);
				}
				if (!line_split[7].equals("Aucun")) {
					IRI current_regime_iri = vf.createIRI(wcd, engine.formatCaseResource(line_split[7]));
					model.add(current_user_iri, predicat_regime, current_regime_iri);
				}
				if (!line_split[8].equals("Aucun")) {
					IRI current_niveau_act_iri = vf.createIRI(wcd, engine.formatCaseResource(line_split[8]));
					model.add(current_user_iri, predicat_niveau_act, current_niveau_act_iri);
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
		System.out.println("End");
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

		for (i = 0; i < 40000; i++) {
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

	public String generateUserProfil(Repository repo, User user, String login) {
		String profil = null;

		String current_age = String.valueOf(Double.valueOf(user.getUserAge(repo, login)));
		String current_poids = String.valueOf(user.getUserPoids(repo, login));
		String current_taille = String.valueOf(user.getUserTaille(repo, login));
		String current_genre = user.getUserGenre(repo, login);
		String current_maladie = user.getUserMaladie(repo, login);
		String current_allergie = user.getUserAllergie(repo, login);
		String current_regime = user.getUserRegimeAlimentaire(repo, login);
		String current_niveau_act = user.getUserNiveauActivite(repo, login);

		profil = current_age + "," + current_poids + "," + current_taille + "," + current_genre + "," + current_maladie
				+ "," + current_allergie + "," + current_regime + "," + current_niveau_act;

		return profil;
	}

	public List<String> generateProfil(Repository repo, User user, List<String> logins) {
		List<String> all_profils = new ArrayList<String>();
		String current_login = null;
		String current_age = null;
		String current_poids = null;
		String current_taille = null;
		String current_genre = null;
		String current_maladie = null;
		String current_allergie = null;
		String current_regime = null;
		String current_niveau_act = null;
		int i = 0;

		for (i = 0; i < logins.size(); i++) {
			current_login = logins.get(i);
			current_age = String.valueOf(user.getUserAge(repo, current_login));
			current_poids = String.valueOf(user.getUserPoids(repo, current_login));
			current_taille = String.valueOf(user.getUserTaille(repo, current_login));
			current_genre = user.getUserGenre(repo, current_login);
			current_maladie = user.getUserMaladie(repo, current_login);
			current_allergie = user.getUserAllergie(repo, current_login);
			current_regime = user.getUserRegimeAlimentaire(repo, current_login);
			current_niveau_act = user.getUserNiveauActivite(repo, current_login);

			all_profils.add(current_login + ";" + current_age + ";" + current_poids + ";" + current_taille + ";"
					+ current_genre + ";" + current_maladie + ";" + current_allergie + ";" + current_regime + ";"
					+ current_niveau_act);
		}

		return all_profils;
	}

	// public List<String> cleanProfils(Repository repo, List<String> profils,
	// String fileName) {
	// BufferedWriter bw = null;
	// FileWriter fw = null;
	//
	// List<String> all_cleaned_profils = new ArrayList<String>();
	// User user = new User();
	// Engine engine = new Engine();
	//
	// List<String> genres = user.getAllGenreFromDB(repo);
	// List<String> maladies = user.getAllMaladieFromDB(repo);
	// List<String> allergies = user.getAllAllergiesFromDB(repo);
	// List<String> regimes = user.getAllRegimeAlimentaireFromDB(repo);
	// List<String> niveaux_acts = user.getAllNiveauActiviteFromDB(repo);
	//
	// int i = 0;
	// int j = 0;
	// int k = 0;
	// int indice = 0;
	//
	// String current_cleaned_profil = null;
	// String[] line_split = null;
	//
	// try {
	// fw = new FileWriter(fileName, true);
	// bw = new BufferedWriter(fw);
	//
	// for (k = 0; k < profils.size(); k++) {
	// line_split = profils.get(k).split(";");
	//
	// for (i = 0; i < line_split.length; i++) {
	// if (i == 0) {
	// current_cleaned_profil = line_split[0];
	// } else if (i == 1) {
	// current_cleaned_profil += ";" +
	// String.valueOf(Double.valueOf(line_split[i]));
	// } else if ((i == 2) || (i == 3)) {
	// current_cleaned_profil += ";" + line_split[i];
	// } else if (i == 4) {
	// if (line_split[4].equals("Aucun")) {
	// for (j = 0; j < genres.size(); j++) {
	// current_cleaned_profil += ";0.0";
	// }
	// } else {
	// for (j = 0; j < genres.size(); j++) {
	// if (genres.get(j).equals(line_split[4])) {
	// current_cleaned_profil += ";1.0";
	// } else {
	// current_cleaned_profil += ";0.0";
	// }
	// }
	// }
	// } else if (i == 5) {
	// if (line_split[5].equals("Aucun")) {
	// for (j = 0; j < maladies.size(); j++) {
	// current_cleaned_profil += ";0.0";
	// }
	// } else {
	// for (j = 0; j < maladies.size(); j++) {
	// if (maladies.get(j).equals(line_split[5])) {
	// current_cleaned_profil += ";1.0";
	// } else {
	// current_cleaned_profil += ";0.0";
	// }
	// }
	// }
	// } else if (i == 6) {
	// if (line_split[6].equals("Aucun")) {
	// for (j = 0; j < allergies.size(); j++) {
	// current_cleaned_profil += ";0.0";
	// }
	// } else {
	// for (j = 0; j < allergies.size(); j++) {
	// if (allergies.get(j).equals(line_split[6])) {
	// current_cleaned_profil += ";1.0";
	// } else {
	// current_cleaned_profil += ";0.0";
	// }
	// }
	// }
	// } else if (i == 7) {
	// if (line_split[7].equals("Aucun")) {
	// for (j = 0; j < regimes.size(); j++) {
	// current_cleaned_profil += ";0.0";
	// }
	// } else {
	// for (j = 0; j < regimes.size(); j++) {
	// if (regimes.get(j).equals(line_split[7])) {
	// current_cleaned_profil += ";1.0";
	// } else {
	// current_cleaned_profil += ";0.0";
	// }
	// }
	// }
	// } else if (i == 8) {
	// if (line_split[8].equals("Aucun")) {
	// for (j = 0; j < niveaux_acts.size(); j++) {
	// current_cleaned_profil += ";0.0";
	// }
	// } else {
	// for (j = 0; j < niveaux_acts.size(); j++) {
	// if (niveaux_acts.get(j).equals(line_split[8])) {
	// current_cleaned_profil += ";1.0";
	// } else {
	// current_cleaned_profil += ";0.0";
	// }
	// }
	// }
	// all_cleaned_profils.add(current_cleaned_profil);
	// bw.write(current_cleaned_profil + "\n");
	// }
	// }
	// }
	// }catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (bw != null)
	// bw.close();
	// if (fw != null)
	// fw.close();
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// return all_cleaned_profils;
	// }

	public void cleanProfils(Repository repo, List<String> profils, String fileName) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		User user = new User();
		Engine engine = new Engine();

		List<String> genres = user.getAllGenreFromDB(repo);
		List<String> maladies = user.getAllMaladieFromDB(repo);
		List<String> allergies = user.getAllAllergiesFromDB(repo);
		List<String> regimes = user.getAllRegimeAlimentaireFromDB(repo);
		List<String> niveaux_acts = user.getAllNiveauActiviteFromDB(repo);

		int i = 0;
		int j = 0;
		int k = 0;
		int indice = 0;

		String[] line_split = null;

		try {
			fw = new FileWriter(fileName, true);
			bw = new BufferedWriter(fw);

			for (k = 0; k < profils.size(); k++) {
				line_split = profils.get(k).split(";");

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

	public List<String> removeRecettesNonRegimeAlimentaire(Repository repo, User user, String login,
			List<String> recettes) {
		String regime_alimentaire = user.getUserRegimeAlimentaire(repo, login);

		if (regime_alimentaire.equals("Aucun")) {
			return recettes;
		}

		Recette rec = new Recette();
		List<String> result = new ArrayList<String>();
		int i = 0;

		List<String> recettes_regime_alimentaire = rec.getNamesRecettesByCategory(repo, regime_alimentaire);

		for (i = 0; i < recettes.size(); i++) {
			if (recettes_regime_alimentaire.contains(recettes.get(i))) {
				result.add(recettes.get(i));
			}
		}

		return result;
	}

	public List<String> removeRecettesAllergie(Repository repo, User user, String login, List<String> recettes) {
		String allergie = user.getUserAllergie(repo, login);

		if (allergie.equals("Aucun")) {
			return recettes;
		}

		Recette rec = new Recette();
		List<String> result = new ArrayList<String>();
		int i = 0;

		List<String> recettes_allergie = rec.getNamesRecettesByAllergie(repo, allergie);

		for (i = 0; i < recettes.size(); i++) {
			if (!recettes_allergie.contains(recettes.get(i))) {
				result.add(recettes.get(i));
			}
		}

		return result;
	}

	public List<String> removeRecettesNonAimees(Repository repo, User user, String login, List<String> recettes) {

		List<String> result = new ArrayList<String>();
		int i = 0;

		List<String> recettes_non_aimees = user.getPasAimeRecette(repo, login);

		for (i = 0; i < recettes.size(); i++) {
			if (!recettes_non_aimees.contains(recettes.get(i))) {
				result.add(recettes.get(i));
			}
		}

		return result;
	}

	public void processOffline(Repository repo, String fichier_profils_nettoyes, String fichier_resultat_cluster)
			throws Exception {
		MachineLearning ml = new MachineLearning();
		User user = new User();
		System.out.println("Récupération des logins");
		List<String> all_logins = user.getAllLogins(repo);
		System.out.println("Récupération des profils");
		List<String> all_profils = generateProfil(repo, user, all_logins);
		System.out.println("Nettoyages des profils");
		cleanProfils(repo, all_profils, fichier_profils_nettoyes);
		System.out.println("Lancement du cluster");
		ml.processKMean(fichier_profils_nettoyes, fichier_resultat_cluster);
	}

	public List<String> processOnline(Repository repo, String fichier_profils_nettoyes, String fichier_resultat_cluster,
			String login) throws IOException {
		MachineLearning ml = new MachineLearning();
		User user = new User();
		String user_profil = generateUserProfil(repo, user, login);
		List<String> cluster_user = ml.find_cluster_user(fichier_resultat_cluster, user_profil);
		List<String> users_id = ml.id_user(fichier_profils_nettoyes, cluster_user);
		List<String> recettes_retournees = tableDeVote(repo, users_id);
		recettes_retournees = removeRecettesNonRegimeAlimentaire(repo, user, login, recettes_retournees);
		recettes_retournees = removeRecettesAllergie(repo, user, login, recettes_retournees);
		recettes_retournees = removeRecettesNonAimees(repo, user, login, recettes_retournees);

		return recettes_retournees;
	}

}
