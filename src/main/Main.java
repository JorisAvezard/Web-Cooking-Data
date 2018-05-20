package main;

import data.*;
import engine.*;

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
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import com.google.common.cache.CacheBuilder;
import org.json.simple.JSONObject;

public class Main {

	public static void main(String[] args) {
		// Initialisation des variables propre à la base RDF
		File dataDir = new File("./db/");
		NativeStore ns = new NativeStore(dataDir);
		Repository repo = new SailRepository(ns);
		ValueFactory vf = SimpleValueFactory.getInstance();
		Model model = new TreeModel();
		String wcd = "http://m2bigcookingdata.org/";

		//Initialisation des variables propre à Lucene
//		String lucene_index = "./lucene/";
//		Sail baseSail = ns;
//		LuceneSail lucenesail = new LuceneSail();
//		lucenesail.setParameter(LuceneSail.LUCENE_DIR_KEY, lucene_index);
//		lucenesail.setBaseSail(ns);
//		SailRepository repo_lucene = new SailRepository(lucenesail);
		
		Recette recette = new Recette();
		Aliment aliment = new Aliment();
		Engine engine = new Engine();
		User user = new User();
		
//		recette.processInsertion(repo, vf, model, wcd, "./fichiers_test/recettes/");

//		String key = "crêPes";
//		JSONObject j = recette.setJson(repo, vf, model, key);
//		engine.getAllStatements(repo, vf, model, wcd);
//		engine.getAllStatementsIRI(repo, vf, model, wcd);
//		aliment.addAll(repo, vf, model, wcd, "./fichiers_test/aliments/donnees_nutritionnelles.csv");
//		aliment.getAll(repo, vf, model);
		user.addAlimentIntoGardeManger(repo, vf, model, wcd, "totO", "Pistache");
		
//		user.addDataTestConnexion(repo, vf, model, wcd, "./fichiers_test/users/connexion.txt");
//		user.getMDP(repo, vf, model);
//		String result = user.checkConnexion(repo, vf, model, "toto", "toto");
//		System.out.println(result);
//		String result = user.processInscription(repo, vf, model, wcd, "Tata", "grr");
//		System.out.println(result);
//		List<String> test = new ArrayList<String>();
//		test.add("lait");
//		test.add("cr");
//		List<String> result = recette.getNamesRecettesByKeyWord(repo, vf, model, test);
//		List<String> result = recette.getNamesRecettesByDifficulte(repo, vf, model, "inter");
//		List<String> result = aliment.getAll(repo, vf, model);
		List<String> result = user.getAlimentsFromGardeManger(repo, vf, model, "toto");
		for(int i=0; i<result.size();i++){
			System.out.println(result.get(i));
		}
	}

}
