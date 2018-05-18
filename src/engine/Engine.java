package engine;

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

public class Engine {

	public Engine() {

	}
	
	public static String upperCaseFirst(String value) {
		char[] array = value.toCharArray();
		array[0] = Character.toUpperCase(array[0]);

		return new String(array);
	}
	
	public static String lowerCaseAll(String value) {
		char[] array = value.toCharArray();
		int i = 0;
		
		for(i=0; i<array.length;i++){
			array[i] = Character.toLowerCase(array[i]);
		}

		return new String(array);
	}
	
	public static String formatCaseResource(String entry){
		String result = null;
		
		result = lowerCaseAll(entry);
		result = upperCaseFirst(result);
		
		result = result.replace(' ', '_');
		
		return result;
	}
	
	public static String formatCaseLitteral(String entry){
		String result = null;
		
		result = lowerCaseAll(entry);
		result = upperCaseFirst(result);
		
		return result;
	}
	
	public static boolean goodFile(String fichier) {
		char[] array = fichier.toCharArray();
		if (array[0] == '.') {
			return false;
		} else {
			return true;
		}
	}
	
	// Affiche tous les statements de la base
	public static void getAllStatements(Repository repo, ValueFactory vf, Model model, String wcd) {
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

	
}
