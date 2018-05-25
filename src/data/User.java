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
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class User {

	public User() {

	}

	public String formatCaseResource(String login) {
		String result = login.replace(' ', '_');
		return result;
	}

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

	public void addAlimentIntoGardeManger(Repository repo, ValueFactory vf, Model model, String wcd, String login,
			String aliment) {
		repo.initialize();
		Engine engine = new Engine();
		IRI login_iri = vf.createIRI(wcd, formatCaseResource(login));
		String aliment_resource = engine.formatCaseResource(aliment);
		IRI aliment_iri = vf.createIRI(wcd, aliment_resource);
		IRI garde_manger_iri = vf.createIRI(wcd, "contenu_garde_manger");
		model.add(login_iri, garde_manger_iri, aliment_iri);
		
		try (RepositoryConnection conn = repo.getConnection()) {
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
			queryString += "    wcd:"+formatCaseResource(login)+" wcd:contenu_garde_manger ?aliment. \n";
			queryString += "    ?aliment foaf:name ?aliment_name. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
//					 System.out.println(solution.getValue("aliment_name").stringValue());
					liste.add(solution.getValue("aliment_name").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;

	}

}
