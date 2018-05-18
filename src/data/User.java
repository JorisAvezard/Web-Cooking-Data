package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import engine.Engine;

public class User {

	public User() {

	}

	public static void addDataTestConnexion(Repository repo, ValueFactory vf, Model model, String wcd,
			String fileName) {
		repo.initialize();

		IRI iri_mdp = vf.createIRI(wcd, "a_pour_mdp");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {

				String[] line_split = line.split(";");
				IRI user_resource = vf.createIRI(wcd, line_split[0]);

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

	public static void getMDP(Repository repo, ValueFactory vf, Model model) {
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

	public static String checkConnexion(Repository repo, ValueFactory vf, Model model, String login_entry,
			String password_entry) {
		repo.initialize();
		String mdp_true = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "    wcd:" + login_entry + " wcd:a_pour_mdp ?i. \n";
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

	public static String checkLoginAlreadyExists(Repository repo, ValueFactory vf, Model model, String login_entry) {
		repo.initialize();
		String login_stored = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?i \n";
			queryString += "WHERE { \n";
			queryString += "wcd:" + login_entry + " rdf:type foaf:Person. \n";
			queryString += "wcd:" + login_entry + " foaf:name ?i. \n";
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

	public static String processInscription(Repository repo, ValueFactory vf, Model model, String wcd,
			String login_entry, String mdp_entry) {
		repo.initialize();

		if (checkLoginAlreadyExists(repo, vf, model, login_entry).equals("yes")) {
			return "Le login est déjà pris.";
		} else if ((mdp_entry.equals(null)) || (mdp_entry.equals(""))) {
			return "Le mot de passe ne doit pas être vide";
		}
		IRI user_resource = vf.createIRI(wcd, login_entry);
		IRI iri_mdp = vf.createIRI(wcd, "a_pour_mdp");

		model.add(user_resource, RDF.TYPE, FOAF.PERSON);
		model.add(user_resource, FOAF.NAME, vf.createLiteral(login_entry));
		model.add(user_resource, iri_mdp, vf.createLiteral(mdp_entry));

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
			return "Insertion finie";
		} finally {
			repo.shutDown();
		}
	}
	
//	public static void addAlimentGardeManger(ValueFactory vf, Model model, String wcd, String login, String aliment) {
//		Engine engine = new Engine();
//		String key_iri = engine.formatCaseResource(key);
//		IRI recette_nom = vf.createIRI(wcd, key_iri);
//		IRI note_iri = vf.createIRI(wcd, "a_pour_note");
//		model.add(recette_nom, note_iri, vf.createLiteral(Float.valueOf("0")));
//	}

}
