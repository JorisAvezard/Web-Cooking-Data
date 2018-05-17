package data;

import engine.Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import com.google.common.cache.CacheBuilder;
import org.json.simple.JSONObject;

public class Recette {

	public Recette() {

	}
	
	public static String getNameFromFile(String fichier) {
		String[] tmp2 = fichier.split(".txt");
		String[] tmp3 = tmp2[0].split("-");

		return tmp3[0];
	}


	public static String getNameFromPath(String fileName) {
		String[] tmp = fileName.split("/");
		String[] tmp2 = tmp[tmp.length - 1].split(".txt");
		String[] tmp3 = tmp2[0].split("-");

		return tmp3[0];
	}

	// ajoute le nom et le type "Recette" de la recette dans la base
	public static void addTypeAndName(ValueFactory vf, Model model, String wcd, String key) {
		Engine engine = new Engine();
		String key_iri = engine.formatCaseResource(key);
		String rec_nom_litteral = engine.formatCaseLitteral(key);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI recette_objet = vf.createIRI(wcd, "Recette");
		model.add(recette_nom, RDF.TYPE, recette_objet);
		model.add(recette_nom, FOAF.NAME, vf.createLiteral(rec_nom_litteral));
	}
	
	public static void addNote(ValueFactory vf, Model model, String wcd, String key) {
		Engine engine = new Engine();
		String key_iri = engine.formatCaseResource(key);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI note_iri = vf.createIRI(wcd, "a_pour_note");
		model.add(recette_nom, note_iri, vf.createLiteral("0"));
	}
	
	public static void addNoteTest(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI note_iri = vf.createIRI(wcd, "a_pour_note");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, note_iri, vf.createLiteral(Float.valueOf(line)));
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
	}
	
	public static void addCategorie(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI cat_iri = vf.createIRI(wcd, "a_pour_catégorie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, cat_iri, vf.createLiteral(line));
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
	}
	
	public static void addDifficulte(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI dif_iri = vf.createIRI(wcd, "a_pour_difficulté");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, dif_iri, vf.createLiteral(line));
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
	}
	
	// ajoute les ingrédients d'une recette
	public static void addIngredients(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_ingrédient");
		IRI data_type = vf.createIRI(wcd, "Ingrédient");
		IRI ing_desc = vf.createIRI(wcd, "description");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String ing_iri = engine.formatCaseResource(line);
				IRI ing = vf.createIRI(wcd, ing_iri);
				model.add(ing, RDF.TYPE, data_type);
				model.add(recette_nom, data_predicate, ing);
				String ing_desc_value = engine.formatCaseLitteral(line);
				model.add(ing, ing_desc, vf.createLiteral(ing_desc_value));
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
	}

	// ajoute l'auteur de la recette
	public static void addAuteur(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String aut_iri = line.replace(' ', '_');
				IRI per = vf.createIRI(wcd, aut_iri);
				model.add(per, RDF.TYPE, FOAF.PERSON);
				model.add(per, FOAF.NAME, vf.createLiteral(line));
				model.add(recette_nom, DC.CREATOR, per);
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
	}

	// ajoute les différentes étapes d'une recette
	public static void addEtapes(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);

		String line = null;
		int ind = 1;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				IRI data_predicate = vf.createIRI(wcd, "a_pour_étape_" + Integer.toString(ind));
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
				ind++;
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
	}

	// ajoute les ustensiles d'une recette
	public static void addUstensiles(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_ustensile");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
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
	}

	// ajoute le temps total d'une recette
	public static void addTempsTotal(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "temps_total");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
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
	}

	// ajoute le temps de préparation d'une recette
	public static void addTempsPreparation(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "temps_préparation");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
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
	}

	// ajoute le temps de cuisson d'une recette
	public static void addTempsCuisson(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "temps_cuisson");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
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
	}

	// ajoute le nb de personnes d'une recette
	public static void addNbPersonnes(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "nb_personnes");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
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
	}

	// ajoute le nb de personnes d'une recette
	public static void processInsertion(Repository repo, ValueFactory vf, Model model, String wcd, String path) {
		Engine engine = new Engine();
		String key = "";
		File folder = new File(path); //./fichiers_test/recettes/
		for (File fileEntry : folder.listFiles()) {
			String fichier = fileEntry.getName();
			if (engine.goodFile(fichier)) {
				 key = getNameFromFile(fichier);
				 addTypeAndName(vf, model, wcd, key);
//				 addNote(vf, model, wcd, key);
				 addNoteTest(repo, vf, model, wcd, path + key + "-note.txt");
				 addCategorie(repo, vf, model, wcd, path + key + "-catégorie.txt");
				 addDifficulte(repo, vf, model, wcd, path + key + "-difficulté.txt");
				 addIngredients(repo, vf, model, wcd, path + key + "-ingrédient.txt");
				 addAuteur(repo, vf, model, wcd, path + key + "-publisher.txt");
				 addEtapes(repo, vf, model, wcd, path + key + "-préparation.txt");
				 addUstensiles(repo, vf, model, wcd, path + key + "-ustensiles.txt");
				 addTempsTotal(repo, vf, model, wcd, path + key + "-temps total.txt");
				 addTempsPreparation(repo, vf, model, wcd, path + key + "-temps préparation.txt");
				 addTempsCuisson(repo, vf, model, wcd, path + key + "-temps cuisson.txt");
				 addNbPersonnes(repo, vf, model, wcd, path + key + "-nb personnes.txt");
			}
		}
		System.out.println("End");
	}

	// retourne les ingrédients étant donné une recette
	public static List<String> getIngredients(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?ii \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " wcd:a_pour_ingrédient ?i. \n";
			queryString += "    ?i wcd:description ?ii. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					// System.out.println(solution.getValue("ii").stringValue());
					liste.add(solution.getValue("ii").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne pour combien de personnes est destinée une recette
	public static List<String> getNbPersonnes(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " wcd:nb_personnes ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					// System.out.println(solution.getValue("ii").stringValue());
					liste.add(solution.getValue("i").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne les étapes étant donné une recette
	public static List<String> getEtapes(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		int indice = 1;
		int indice2 = 0;
		boolean cont = true;

		try (RepositoryConnection conn = repo.getConnection()) {
			while (cont) {
				String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
				queryString += "SELECT ?i \n";
				queryString += "WHERE { \n";
				queryString += "    wcd:" + key + " wcd:a_pour_étape_" + indice + " ?i. \n";
				queryString += "}";
				TupleQuery query = conn.prepareTupleQuery(queryString);
				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
						// System.out.println(solution.getValue("ii").stringValue());
						liste.add("Etape " + indice + " : " + solution.getValue("i").stringValue());
						indice2++;
					}
				}
				if (indice2 == 0) {
					cont = false;
				}
				indice2 = 0;
				indice++;
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne l'auteur étant donné une recette
	public static List<String> getAuteur(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "PREFIX dc: <" + DC.NAMESPACE + "> \n";
			queryString += "SELECT ?ii \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " dc:creator ?i. \n";
			queryString += "    ?i foaf:name ?ii. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					// System.out.println(solution.getValue("ii").stringValue());
					liste.add(solution.getValue("ii").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne l'auteur étant donné une recette
	public static List<String> getTempsTotal(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " wcd:temps_total ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("i").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne l'auteur étant donné une recette
	public static List<String> getTempsCuisson(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " wcd:temps_cuisson ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("i").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne l'auteur étant donné une recette
	public static List<String> getTempsPreparation(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " wcd:temps_préparation ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("i").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne les ingrédients étant donné une recette
	public static List<String> getUstensiles(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + key + " wcd:a_pour_ustensile ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("i").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public static JSONObject setJson(Repository repo, ValueFactory vf, Model model, String key) {
		JSONObject result = new JSONObject();
		Engine engine = new Engine();
		String key_iri = engine.formatCaseResource(key);

		List<String> ing_val = getIngredients(repo, vf, model, key_iri);
		List<String> nbper_val = getNbPersonnes(repo, vf, model, key_iri);
		List<String> et_val = getEtapes(repo, vf, model, key_iri);
		List<String> aut_val = getAuteur(repo, vf, model, key_iri);
		List<String> tt_val = getTempsTotal(repo, vf, model, key_iri);
		List<String> tc_val = getTempsCuisson(repo, vf, model, key_iri);
		List<String> tp_val = getTempsPreparation(repo, vf, model, key_iri);
		List<String> ust_val = getUstensiles(repo, vf, model, key_iri);
		result.put("ingrédients", ing_val);
		result.put("nb personnes", nbper_val);
		result.put("étapes", et_val);
		result.put("auteur", aut_val);
		result.put("temps total", tt_val);
		result.put("temps cuisson", tc_val);
		result.put("temps préparation", tp_val);
		result.put("ustensiles", ust_val);

		System.out.print(result);

		return result;
	}
	
	public static List<String> getNamesRecettesByKeyWord(Repository repo, ValueFactory vf, Model model, List<String> key_words) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();
		int i =0;
		String key = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			for(i=0;i<key_words.size();i++){
				key = engine.lowerCaseAll(key_words.get(i));
				
				String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
				queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
				queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
				queryString += "SELECT ?ii \n";
				queryString += "WHERE { \n";
				queryString += "    ?i rdf:type wcd:Recette. \n";
				queryString += "    ?i foaf:name ?ii. \n";
				queryString += "   FILTER regex(?ii, \""+key+"\", \"i\") \n";
//				queryString += "   OPTIONAL { FILTER regex(?ii, \""+key+"\", \"i\")}. \n";
//				queryString += "   OPTIONAL { FILTER regex(?ii, \""+key2+"\", \"i\")}. \n";
				queryString += "}";
				TupleQuery query = conn.prepareTupleQuery(queryString);
				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
						if(!liste.contains(solution.getValue("ii").stringValue())){
							liste.add(solution.getValue("ii").stringValue());
						}
//						System.out.println(solution.getValue("i").stringValue());
					}
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}
	
	public static List<String> getNamesRecettesByCategory(Repository repo, ValueFactory vf, Model model, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();
		key = engine.lowerCaseAll(key);

		try (RepositoryConnection conn = repo.getConnection()) {
				
				String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
				queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
				queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
				queryString += "SELECT ?ii \n";
				queryString += "WHERE { \n";
				queryString += "    ?i rdf:type wcd:Recette. \n";
				queryString += "    ?i foaf:name ?ii. \n";
				queryString += "    ?i wcd:a_pour_catégorie ?cat. \n";
				queryString += "   FILTER regex(?cat, \""+key+"\", \"i\") \n";
				queryString += "}";
				TupleQuery query = conn.prepareTupleQuery(queryString);
				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
							liste.add(solution.getValue("ii").stringValue());
					}
				}
		} finally {
			repo.shutDown();
		}

		return liste;
	}
	
	public static List<String> getNamesRecettesByNote(Repository repo, ValueFactory vf, Model model, float note) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();

		try (RepositoryConnection conn = repo.getConnection()) {
				
				String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
				queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
				queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
				queryString += "SELECT ?ii \n";
				queryString += "WHERE { \n";
				queryString += "    ?i rdf:type wcd:Recette. \n";
				queryString += "    ?i foaf:name ?ii. \n";
				queryString += "    ?i wcd:a_pour_note ?note. \n";
				queryString += "   FILTER (?note >= "+note+") \n";
				queryString += "}";
				queryString += "ORDER BY DESC(?note)";
				TupleQuery query = conn.prepareTupleQuery(queryString);
				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
							liste.add(solution.getValue("ii").stringValue());
					}
				}
		} finally {
			repo.shutDown();
		}

		return liste;
	}
	
	

}
