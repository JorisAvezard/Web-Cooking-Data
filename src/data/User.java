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

public class User {

	public User() {

	}

	public String formatCaseResource(String login) {
		String result = login.replace(' ', '_');
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methodes sur le remplissage de la base
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void insertAllDataMaladieIntoDB(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI maladie_type = vf.createIRI(wcd, "Maladie");

		String line = null;
		try {
			// ./fichiers_test/donnees_nutritionnelles.csv
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI maladie_iri = vf.createIRI(wcd, key_iri);
				model.add(maladie_iri, RDF.TYPE, maladie_type);
				model.add(maladie_iri, FOAF.NAME, vf.createLiteral(line));
			}
			System.out.println("Success");
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

	public void insertAllDataRegimeAlimentaireIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI regime_type = vf.createIRI(wcd, "RegimeAlimentaire");

		String line = null;
		try {
			// ./fichiers_test/donnees_nutritionnelles.csv
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI regime_iri = vf.createIRI(wcd, key_iri);
				model.add(regime_iri, RDF.TYPE, regime_type);
				model.add(regime_iri, FOAF.NAME, vf.createLiteral(line));
			}
			System.out.println("Success");
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

	public void insertAllDataGenreIntoDB(Repository repo, ValueFactory vf, Model model, String wcd) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI genre_type = vf.createIRI(wcd, "Genre");
		IRI genre_iri = null;
		List<String> genres = new ArrayList<String>();
		genres.add("Femme");
		genres.add("Homme");

		for (int i = 0; i < genres.size(); i++) {
			genre_iri = vf.createIRI(wcd, genres.get(i));
			model.add(genre_iri, RDF.TYPE, genre_type);
			model.add(genre_iri, FOAF.NAME, vf.createLiteral(genres.get(i)));
		}
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void insertAllDataNiveauActiviteIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "NiveauActivite");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, RDF.TYPE, data_type);
				model.add(data_iri, FOAF.NAME, vf.createLiteral(line));
			}
			System.out.println("Success");
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
	
	public void insertAllDataAllergieFruitDeMerIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "FruitDeMer");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Fruits de mer"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieArachideIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "Arachide");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Arachide"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieBleIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "BlÃ©");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("BlÃ©"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieLaitIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "Lait");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Lait"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieMoutardeIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "Moutarde");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Moutarde"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieNoixIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "Noix");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Noix"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieOeufIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "Oeuf");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Oeuf"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieSojaIntoDB(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = "";
		IRI data_type = vf.createIRI(wcd, "Allergie");
		IRI data_category = vf.createIRI(wcd, "Soja");
		model.add(data_category, RDF.TYPE, data_type);
		model.add(data_category, FOAF.NAME, vf.createLiteral("Soja"));
		
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_categorie_allergie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null) {
				key_iri = engine.formatCaseResource(line);
				IRI data_iri = vf.createIRI(wcd, key_iri);
				model.add(data_iri, predicat_iri, data_category);
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
	
	public void insertAllDataAllergieIntoDB(Repository repo, ValueFactory vf, Model model, String wcd) {
		insertAllDataAllergieFruitDeMerIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/fruits_de_mer.txt");
		insertAllDataAllergieArachideIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_arachide.txt");
		insertAllDataAllergieBleIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_bleÌ�.txt");
		insertAllDataAllergieLaitIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_laitier.txt");
		insertAllDataAllergieMoutardeIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_moutarde.txt");
		insertAllDataAllergieNoixIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_noix.txt");
		insertAllDataAllergieOeufIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_oeuf.txt");
		insertAllDataAllergieSojaIntoDB(repo, vf, model, wcd, "./fichiers_test/aliments/produits_soja.txt");
		
	}
	
	public List<String> getAllAllergiesFromDB(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?data \n";
			queryString += "WHERE { \n";
			queryString += "    ?data_iri rdf:type wcd:Allergie. \n";
			queryString += "    ?data_iri foaf:name ?data. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("data").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methodes sur l'inscription et connexion
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addDataTestConnexion(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();

		IRI iri_mdp = vf.createIRI(wcd, "a_pour_mdp");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {

				String[] line_split = line.split(";");
				IRI user_resource = vf.createIRI(wcd, formatCaseResource(line_split[0]));

				model.add(user_resource, RDF.TYPE, FOAF.PERSON);
				model.add(user_resource, FOAF.NAME, vf.createLiteral(line_split[0]));
				model.add(user_resource, iri_mdp, vf.createLiteral(line_split[1]));

			}
			System.out.println("Success");
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

	public void getMDP(Repository repo, ValueFactory vf, Model model) {
		repo.initialize();
		// List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?ii \n";
			queryString += "WHERE { \n";
			queryString += "    ?i rdf:type foaf:Person. \n";
			queryString += "    ?i wcd:a_pour_mdp ?ii. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					System.out.println(solution.getValue("ii").stringValue());
					// liste.add(solution.getValue("ii").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		// return liste;
	}

	public String checkConnexion(String login_entry, String password_entry) {
		File dataDir = new File("./db/");
		Repository repo = new SailRepository(new NativeStore(dataDir));
		repo.initialize();
		String mdp_true = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login_entry) + " wcd:a_pour_mdp ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					mdp_true = solution.getValue("i").stringValue();
				}
			}
			if (password_entry.equals(mdp_true)) {
				return "succes";
			} else {
				return "echec";
			}

		} finally {
			repo.shutDown();
		}
	}

	public String checkLoginAlreadyExists(Repository repo, ValueFactory vf, Model model, String login_entry) {
		repo.initialize();
		String login_stored = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "wcd:" + formatCaseResource(login_entry) + " rdf:type foaf:Person. \n";
			queryString += "wcd:" + formatCaseResource(login_entry) + " foaf:name ?i. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					login_stored = solution.getValue("i").stringValue();
				}
			}
			if (login_entry.equals(login_stored)) {
				return "yes";
			} else {
				return "no";
			}

		}
	}

	public String processInscription(String login_entry, String mdp_entry) {
		File dataDir = new File("./db/");
		Repository repo = new SailRepository(new NativeStore(dataDir));
		ValueFactory vf = SimpleValueFactory.getInstance();
		Model model = new TreeModel();
		String wcd = "http://m2bigcookingdata.org/";
		repo.initialize();

		if (checkLoginAlreadyExists(repo, vf, model, login_entry).equals("yes")) {
			return "Le login est deja pris.";
		} else if ((mdp_entry.equals(null)) || (mdp_entry.equals(""))) {
			return "Le mot de passe ne doit pas etre vide";
		}
		IRI user_resource = vf.createIRI(wcd, formatCaseResource(login_entry));
		IRI iri_mdp = vf.createIRI(wcd, "a_pour_mdp");
		// IRI iri_garde_manger = vf.createIRI(wcd,
		// "nombre_aliments_garde_manger");

		model.add(user_resource, RDF.TYPE, FOAF.PERSON);
		model.add(user_resource, FOAF.NAME, vf.createLiteral(login_entry));
		model.add(user_resource, iri_mdp, vf.createLiteral(mdp_entry));
		// model.add(user_resource, iri_garde_manger,
		// vf.createLiteral(Integer.valueOf("0")));

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
			return "Insertion finie";
		} finally {
			repo.shutDown();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methodes sur le garde-manger
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	// public int getNumberAlimentsInGardeManger(Repository repo, String login)
	// {
	// int total = 0;
	//
	// try (RepositoryConnection conn = repo.getConnection()) {
	// String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
	// queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
	// queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
	// queryString += "SELECT ?nombre \n";
	// queryString += "WHERE { \n";
	// queryString += " wcd:" + formatCaseResource(login) + "
	// wcd:nombre_aliments_garde_manger ?nombre. \n";
	// queryString += "}";
	// TupleQuery query = conn.prepareTupleQuery(queryString);
	// try (TupleQueryResult result = query.evaluate()) {
	// while (result.hasNext()) {
	// BindingSet solution = result.next();
	// total = Integer.valueOf(solution.getValue("nombre").stringValue());
	// }
	// }
	// }
	//
	// return total;
	//
	// }

	// public void updateNumberAlimentsInGardeManger(Repository repo,
	// ValueFactory vf, Model model, String wcd, String login) {
	// Engine engine = new Engine();
	// IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
	//
	// int nombre_courant = getNumberAlimentsInGardeManger(repo, login);
	// int nouveau_nombre = nombre_courant-1;
	//
	// String aliment_resource = engine.formatCaseResource(aliment);
	// IRI aliment_iri = vf.createIRI(wcd, aliment_resource);
	// IRI nb_ali_gm_iri = vf.createIRI(wcd, "nombre_aliments_garde_manger");
	// model.add(login_iri, garde_manger_iri, aliment_iri);
	// model.add(aliment_iri, aliment_quantite_iri, vf.createLiteral(quantite));
	//
	// try (RepositoryConnection conn = repo.getConnection()) {
	// conn.remove(login_iri, nb_ali_gm_iri, null);
	//
	// conn.add(model);
	// }
	//
	// repo.initialize();
	// IRI garde_manger_iri = vf.createIRI(wcd, "contenu_garde_manger");
	// try (RepositoryConnection conn = repo.getConnection()) {
	// // let's check that our data is actually in the database
	// try (RepositoryResult<Statement> result = conn.getStatements(null,
	// garde_manger_iri, null);) {
	// while (result.hasNext()) {
	// Statement st = result.next();
	// System.out.println(st);
	// }
	// }
	// } finally {
	// repo.shutDown();
	// }
	// }

	public void addAlimentIntoGardeManger(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String aliment, double quantite) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String aliment_resource = engine.formatCaseResource(aliment);
		IRI aliment_iri = vf.createIRI(wcd, aliment_resource);
		IRI garde_manger_iri = vf.createIRI(wcd, "contenu_garde_manger");
		// IRI nb_ali_gm_iri = vf.createIRI(wcd,
		// "nombre_aliments_garde_manger");
		IRI new_ali_gm_iri = vf.createIRI(wcd, formatCaseResource(login) + "_GM_" + aliment_resource);
		IRI aliment_quantite_iri = vf.createIRI(wcd, "quantite_aliment_GM");
		IRI aliment_gm_iri = vf.createIRI(wcd, "aliment_GM");

		try (RepositoryConnection conn = repo.getConnection()) {
			// conn.remove(login_iri, nb_ali_gm_iri, null);

			// model.add(login_iri, nb_ali_gm_iri,
			// vf.createLiteral(nouveau_nombre));
			model.add(login_iri, garde_manger_iri, new_ali_gm_iri);
			model.add(new_ali_gm_iri, aliment_gm_iri, aliment_iri);
			model.add(new_ali_gm_iri, aliment_quantite_iri, vf.createLiteral(quantite));

			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeAlimentInGardeManger(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String aliment) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String aliment_resource = engine.formatCaseResource(aliment);
		IRI garde_manger_iri = vf.createIRI(wcd, "contenu_garde_manger");
		IRI ali_gm_iri = vf.createIRI(wcd, formatCaseResource(login) + "_GM_" + aliment_resource);
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, garde_manger_iri, ali_gm_iri);
			conn.remove(ali_gm_iri, null, null);
		} finally {
			repo.shutDown();
		}
	}

	public void updateAlimentQuantityInGardeManger(Repository repo, ValueFactory vf, Model model, String wcd,
			String login, String aliment, double quantity) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String aliment_resource = engine.formatCaseResource(aliment);
		IRI ali_gm_iri = vf.createIRI(wcd, formatCaseResource(login) + "_GM_" + aliment_resource);
		IRI predicat_iri = vf.createIRI(wcd, "quantite_aliment_GM");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(ali_gm_iri, predicat_iri, null);
			model.add(ali_gm_iri, predicat_iri, vf.createLiteral(quantity));
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getAlimentsFromGardeManger(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?aliment_name \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:contenu_garde_manger ?contenu. \n";
			queryString += "    ?contenu wcd:aliment_GM ?aliment. \n";
			queryString += "    ?aliment foaf:name ?aliment_name. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					// System.out.println(solution.getValue("aliment_name").stringValue());
					liste.add(solution.getValue("aliment_name").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;

	}

	public List<String> getAlimentsWithQuantityFromGardeManger(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?aliment_name ?quantity \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:contenu_garde_manger ?contenu. \n";
			queryString += "    ?contenu wcd:aliment_GM ?aliment. \n";
			queryString += "    ?aliment foaf:name ?aliment_name. \n";
			queryString += "    ?contenu wcd:quantite_aliment_GM ?quantity. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					// System.out.println(solution.getValue("aliment_name").stringValue());
					liste.add(solution.getValue("aliment_name").stringValue());
					liste.add(solution.getValue("quantity").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methodes sur le profil
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<String> getAllMaladieFromDB(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?maladie \n";
			queryString += "WHERE { \n";
			queryString += "    ?maladie_iri rdf:type wcd:Maladie. \n";
			queryString += "    ?maladie_iri foaf:name ?maladie. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("maladie").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public List<String> getUserMaladie(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?maladie \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_maladie ?maladie_iri. \n";
			queryString += "    ?maladie_iri foaf:name ?maladie. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("maladie").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;

	}

	public void addMaladie(Repository repo, ValueFactory vf, Model model, String wcd, String login, String maladie) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String maladie_formate = engine.formatCaseResource(maladie);
		IRI maladie_iri = vf.createIRI(wcd, maladie_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_maladie");
		model.add(login_iri, predicat_iri, maladie_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeMaladie(Repository repo, ValueFactory vf, Model model, String wcd, String login, String maladie) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String maladie_formate = engine.formatCaseResource(maladie);
		IRI maladie_iri = vf.createIRI(wcd, maladie_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_maladie");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, maladie_iri);
		} finally {
			repo.shutDown();
		}
	}

	public void updateMaladie(Repository repo, ValueFactory vf, Model model, String wcd, String login, String maladie) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String maladie_formate = engine.formatCaseResource(maladie);
		IRI maladie_iri = vf.createIRI(wcd, maladie_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_maladie");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, maladie_iri);
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getAllRegimeAlimentaireFromDB(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?regime \n";
			queryString += "WHERE { \n";
			queryString += "    ?regime_iri rdf:type wcd:RegimeAlimentaire. \n";
			queryString += "    ?regime_iri foaf:name ?regime. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("regime").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public void addRegimeAlimentaire(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String regime) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String regime_formate = engine.formatCaseResource(regime);
		IRI regime_iri = vf.createIRI(wcd, regime_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_regime_alimentaire");
		model.add(login_iri, predicat_iri, regime_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeRegimeAlimentaire(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String regime) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String regime_formate = engine.formatCaseResource(regime);
		IRI regime_iri = vf.createIRI(wcd, regime_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_regime_alimentaire");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, regime_iri);
		} finally {
			repo.shutDown();
		}
	}

	public void updateRegimeAlimentaire(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String regime) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String regime_formate = engine.formatCaseResource(regime);
		IRI regime_iri = vf.createIRI(wcd, regime_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_regime_alimentaire");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, regime_iri);
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getUserRegimeAlimentaire(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?regime \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_regime_alimentaire ?regime_iri. \n";
			queryString += "    ?regime_iri foaf:name ?regime. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("regime").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;

	}

	public void addAllergie(Repository repo, ValueFactory vf, Model model, String wcd, String login, String allergie) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String allergie_formate = engine.formatCaseResource(allergie);
		IRI allergie_iri = vf.createIRI(wcd, allergie_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_allergie");
		model.add(login_iri, predicat_iri, allergie_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeAllergie(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String allergie) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String allergie_formate = engine.formatCaseResource(allergie);
		IRI allergie_iri = vf.createIRI(wcd, allergie_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_allergie");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, allergie_iri);
		} finally {
			repo.shutDown();
		}
	}

	public void updateAllergie(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String allergie) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String allergie_formate = engine.formatCaseResource(allergie);
		IRI allergie_iri = vf.createIRI(wcd, allergie_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_allergie");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, allergie_iri);
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getUserAllergie(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX foaf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?allergie \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_allergie ?allergie_iri. \n";
			queryString += "    ?allergie_iri rdf:type wcd:Allergie. \n";
			queryString += "    ?allergie_iri foaf:name ?allergie. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("allergie").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public void addAge(Repository repo, ValueFactory vf, Model model, String wcd, String login, int age) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_age");
		model.add(login_iri, predicat_iri, vf.createLiteral(age));

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeAge(Repository repo, ValueFactory vf, Model model, String wcd, String login) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_age");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
		} finally {
			repo.shutDown();
		}
	}

	public void updateAge(Repository repo, ValueFactory vf, Model model, String wcd, String login, int age) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_age");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, vf.createLiteral(age));
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public int getUserAge(Repository repo, String login) {
		repo.initialize();
		int resultat = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?age \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_age ?age. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = Integer.valueOf(solution.getValue("age").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return resultat;
	}

	public List<String> getAllGenreFromDB(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?data \n";
			queryString += "WHERE { \n";
			queryString += "    ?data_iri rdf:type wcd:Genre. \n";
			queryString += "    ?data_iri foaf:name ?data. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("data").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}
	
	public void addGenre(Repository repo, ValueFactory vf, Model model, String wcd, String login, String genre) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String genre_formate = engine.formatCaseResource(genre);
		IRI genre_iri = vf.createIRI(wcd, genre_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_genre");
		model.add(login_iri, predicat_iri, genre_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeGenre(Repository repo, ValueFactory vf, Model model, String wcd, String login) {
		repo.initialize();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_genre");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
		} finally {
			repo.shutDown();
		}
	}

	public void updateGenre(Repository repo, ValueFactory vf, Model model, String wcd, String login, String genre) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String genre_formate = engine.formatCaseResource(genre);
		IRI genre_iri = vf.createIRI(wcd, genre_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_genre");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, genre_iri);
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public String getUserGenre(Repository repo, String login) {
		repo.initialize();
		String liste = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?genre \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_genre ?genre_iri. \n";
			queryString += "    ?genre_iri foaf:name ?genre. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste = solution.getValue("genre").stringValue();
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public List<String> getAllNiveauActiviteFromDB(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?na \n";
			queryString += "WHERE { \n";
			queryString += "    ?na_iri rdf:type wcd:NiveauActivite. \n";
			queryString += "    ?na_iri foaf:name ?na. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("na").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public String getUserNiveauActivite(Repository repo, String login) {
		repo.initialize();
		String liste = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?na \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_niveau_activite ?na_iri. \n";
			queryString += "    ?na_iri foaf:name ?na. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste = solution.getValue("na").stringValue();
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public void addNiveauActivite(Repository repo, ValueFactory vf, Model model, String wcd, String login, String na) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String na_formate = engine.formatCaseResource(na);
		IRI na_iri = vf.createIRI(wcd, na_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_niveau_activite");
		model.add(login_iri, predicat_iri, na_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeNiveauActivite(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String na) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String na_formate = engine.formatCaseResource(na);
		IRI na_iri = vf.createIRI(wcd, na_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_niveau_activite");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, na_iri);
		} finally {
			repo.shutDown();
		}
	}

	public void updateNiveauActivite(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String na) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String na_formate = engine.formatCaseResource(na);
		IRI na_iri = vf.createIRI(wcd, na_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_niveau_activite");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, na_iri);
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// en kg
	public void addPoids(Repository repo, ValueFactory vf, Model model, String wcd, String login, double poids) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_poids");
		model.add(login_iri, predicat_iri, vf.createLiteral(poids));

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removePoids(Repository repo, ValueFactory vf, Model model, String wcd, String login) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_poids");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
		} finally {
			repo.shutDown();
		}
	}

	public void updatePoids(Repository repo, ValueFactory vf, Model model, String wcd, String login, double poids) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_poids");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, vf.createLiteral(poids));
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public double getUserPoids(Repository repo, String login) {
		repo.initialize();
		double resultat = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?poids \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_poids ?poids. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = Double.valueOf(solution.getValue("poids").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return resultat;
	}

	// en m
	public void addTaille(Repository repo, ValueFactory vf, Model model, String wcd, String login, double taille) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_taille");
		model.add(login_iri, predicat_iri, vf.createLiteral(taille));

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void removeTaille(Repository repo, ValueFactory vf, Model model, String wcd, String login) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_taille");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
		} finally {
			repo.shutDown();
		}
	}

	public void updateTaille(Repository repo, ValueFactory vf, Model model, String wcd, String login, double taille) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_taille");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, vf.createLiteral(taille));
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public double getUserTaille(Repository repo, String login) {
		repo.initialize();
		double resultat = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?taille \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_taille ?taille. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = Double.valueOf(solution.getValue("taille").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return resultat;
	}

	public void updateBesoinCalorique(Repository repo, ValueFactory vf, Model model, String wcd, String login) {
		repo.initialize();
		Engine engine = new Engine();

		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		IRI predicat_iri = vf.createIRI(wcd, "a_pour_besoin_calorique");
		try (RepositoryConnection conn = repo.getConnection()) {
			double besoin_calorique = calculBesoinCalorique(repo, login);
			conn.remove(login_iri, predicat_iri, null);
			model.add(login_iri, predicat_iri, vf.createLiteral(besoin_calorique));
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}
	
	public double getUserBesoinCalorique(Repository repo, String login) {
		repo.initialize();
		double resultat = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?bc \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_besoin_calorique ?bc. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = Double.valueOf(solution.getValue("bc").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return resultat;
	}
	
	public double calculBesoinCalorique(Repository repo, String login){
		double result = 0.0;
		
		String genre = getUserGenre(repo, login);
		int age = getUserAge(repo, login);
		double taille = getUserTaille(repo, login);
		double poids = getUserPoids(repo, login);
		String niveau_activite = getUserNiveauActivite(repo, login);
		
		if((genre.equals(""))||(age==0)||(taille==0)||(poids==0)||(niveau_activite.equals(""))){
			return result;
		}
		
		double coef_poids = 0.0;
		double coef_taille = 0.0;
		double coef_age = 0.0;
		double coef = 0.0;
		
		if(genre.equals("Femme")){
			coef_poids = 9.5634;
			coef_taille = 184.96;
			coef_age = 4.6756;
			coef = 655.0955;
		}else{
			coef_poids = 13.7516;
			coef_taille = 500.33;
			coef_age = 6.755;
			coef = 66.473;
		}
		
		double coef_niveau_activite = 1;
		
		if(niveau_activite.equals("Actif")){
			coef_niveau_activite = 1.37;
		}else if(niveau_activite.equals("Tres actif")){
			coef_niveau_activite = 1.55;
		}else if(niveau_activite.equals("Extremement actif")){
			coef_niveau_activite = 1.8;
		}
		
		return ((coef_poids*poids)+(coef_taille*taille)-(coef_age*age)+coef)*coef_niveau_activite;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methodes sur l'historique
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addConsulteRecette(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String recette) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String recette_formate = engine.formatCaseResource(recette);
		IRI recette_iri = vf.createIRI(wcd, recette_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_consulte");
		model.add(login_iri, predicat_iri, recette_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getConsulteRecette(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_consulte ?recette_iri. \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("recette_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public void addAimeRecette(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String recette) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String recette_formate = engine.formatCaseResource(recette);
		IRI recette_iri = vf.createIRI(wcd, recette_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_aime");
		model.add(login_iri, predicat_iri, recette_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getAimeRecette(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_aime ?recette_iri. \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("recette_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public void addPasAimeRecette(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String recette) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String recette_formate = engine.formatCaseResource(recette);
		IRI recette_iri = vf.createIRI(wcd, recette_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_pas_aime");
		model.add(login_iri, predicat_iri, recette_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getPasAimeRecette(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pas_aime ?recette_iri. \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("recette_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	public void addFavoriRecette(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String recette) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String recette_formate = engine.formatCaseResource(recette);
		IRI recette_iri = vf.createIRI(wcd, recette_formate);
		IRI predicat_iri = vf.createIRI(wcd, "a_mis_en_favori");
		model.add(login_iri, predicat_iri, recette_iri);

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public List<String> getFavoriRecette(Repository repo, String login) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_mis_en_favori ?recette_iri. \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("recette_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

}
