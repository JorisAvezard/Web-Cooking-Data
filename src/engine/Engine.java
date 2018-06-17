package engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import data.Aliment;

public class Engine {

	public Engine() {

	}

	public String upperCaseFirst(String value) {
		char[] array = value.toCharArray();
		array[0] = Character.toUpperCase(array[0]);

		return new String(array);
	}

	public String lowerCaseAll(String value) {
		char[] array = value.toCharArray();
		int i = 0;

		for (i = 0; i < array.length; i++) {
			array[i] = Character.toLowerCase(array[i]);
		}

		return new String(array);
	}

	public String formatCaseResource(String entry) {
		String result = null;

		result = lowerCaseAll(entry);
		result = upperCaseFirst(result);

		result = result.replace(' ', '_');

		return result;
	}

	public String formatCaseLitteral(String entry) {
		String result = null;

		result = lowerCaseAll(entry);
		result = upperCaseFirst(result);

		return result;
	}

	public boolean goodFile(String fichier) {
		char[] array = fichier.toCharArray();
		if (array[0] == '.') {
			return false;
		} else {
			return true;
		}
	}

	public String getValueFromPreparationAndCuissonRecetteFile(String line) {
		String[] line_split = line.split(": ");

		return line_split[line_split.length - 1];
	}

	// Affiche tous les statements de la base
	public void getAllStatements(Repository repo, ValueFactory vf, Model model, String wcd) {
		repo.initialize();
		try (RepositoryConnection conn = repo.getConnection()) {
			// let's check that our data is actually in the database
			try (RepositoryResult<Statement> result = conn.getStatements(null, null, null);) {
				while (result.hasNext()) {
					Statement st = result.next();
					System.out.println(st);
				}
			}
		} finally {
			repo.shutDown();
		}
	}

	public void getAllStatementsIRI(Repository repo, ValueFactory vf, Model model, String wcd) {
		repo.initialize();
		IRI sujet_iri = vf.createIRI(wcd, "user5128");
		IRI predicat_iri = vf.createIRI(wcd, "Allergie");
		int i = 0;
		try (RepositoryConnection conn = repo.getConnection()) {
			// let's check that our data is actually in the database
			try (RepositoryResult<Statement> result = conn.getStatements(null, null, predicat_iri);) {
				while (result.hasNext()) {
					Statement st = result.next();
					i++;
					System.out.println(st);
				}
				System.out.println(i);
			}
		} finally {
			repo.shutDown();
		}
	}

	public void removeAllAllergie(Repository repo, ValueFactory vf, Model model, String wcd) {
		Aliment al = new Aliment();
		List<String> allergies = al.getAll(repo);
		repo.initialize();
		IRI predicat_iri = vf.createIRI(wcd, "Allergie");

		for (int i = 0; i < allergies.size(); i++) {

			try (RepositoryConnection conn = repo.getConnection()) {
				IRI sujet_iri = vf.createIRI(wcd, formatCaseResource(allergies.get(i)));
				conn.remove(sujet_iri, null, predicat_iri);
			}
		}

		repo.shutDown();

	}

	public void removeAllUserAimeRecette(Repository repo, ValueFactory vf, Model model, String wcd) {
		repo.initialize();
		int i = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			for (i = 5000; i < 5129; i++) {
				IRI sujet_iri = vf.createIRI(wcd, "user"+String.valueOf(i));
				conn.remove(sujet_iri, null, null);
			}
		}

		repo.shutDown();

	}

	public void removeAllStatementsIRI(Repository repo, ValueFactory vf, Model model, String wcd) {
		repo.initialize();
		IRI sujet_iri = vf.createIRI(wcd, "user0");
		IRI predicat_iri = vf.createIRI(wcd, "a_aime");
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(sujet_iri, predicat_iri, null);
		} finally {
			repo.shutDown();
		}
	}

	public void writeFile(List<String> entry, String fileName) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);
			bw = new BufferedWriter(fw);

			for (int i = 0; i < entry.size(); i++) {
				bw.write(entry.get(i) + "\n");
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

}
