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
		IRI maladie_desc_iri = vf.createIRI(wcd, "maladie_description");
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
				model.add(maladie_iri, maladie_desc_iri, vf.createLiteral(line));
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
			String aliment, float quantite) {
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
			String login, String aliment, float quantity) {
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
			queryString += "SELECT ?maladie \n";
			queryString += "WHERE { \n";
			queryString += "    ?maladie_iri rdf:type wcd:Maladie. \n";
			queryString += "    ?maladie_iri wcd:maladie_description ?maladie. \n";
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
			queryString += "SELECT ?maladie \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + formatCaseResource(login) + " wcd:a_pour_maladie ?maladie_iri. \n";
			queryString += "    ?maladie_iri wcd:maladie_description ?maladie. \n";
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

	public void removeMaladie(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String maladie) {
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

	public void addRegimeAlimentaire(Repository repo, ValueFactory vf, Model model, String wcd, String login, String regime){
		
	}
}
