package engine;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

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
		
		for(i=0; i<array.length;i++){
			array[i] = Character.toLowerCase(array[i]);
		}

		return new String(array);
	}
	
	public String formatCaseResource(String entry){
		String result = null;
		
		result = lowerCaseAll(entry);
		result = upperCaseFirst(result);
		
		result = result.replace(' ', '_');
		
		return result;
	}
	
	public String formatCaseLitteral(String entry){
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
	
	public String getValueFromPreparationAndCuissonRecetteFile(String line){
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
		IRI garde_manger_iri = vf.createIRI(wcd, "Recette");
		try (RepositoryConnection conn = repo.getConnection()) {
			// let's check that our data is actually in the database
			try (RepositoryResult<Statement> result = conn.getStatements(null, RDF.TYPE, FOAF.PERSON);) {
				while (result.hasNext()) {
					Statement st = result.next();
					System.out.println(st);
				}
			}
		} finally {
			repo.shutDown();
		}
	}


	
}
