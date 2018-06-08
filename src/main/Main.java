package main;

import data.*;
import engine.*;
import ia.*;

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
//import com.google.common.cache.CacheBuilder;
import org.json.simple.JSONObject;

public class Main {

	public static void main(String[] args) throws Exception {
		// Initialisation des variables propre à la base RDF
		File dataDir = new File("./db/");
		NativeStore ns = new NativeStore(dataDir);
		Repository repo = new SailRepository(ns);
		ValueFactory vf = SimpleValueFactory.getInstance();
		Model model = new TreeModel();
		String wcd = "http://m2bigcookingdata.org/";

		Recette recette = new Recette();
		Aliment aliment = new Aliment();
		Engine engine = new Engine();
		User user = new User();
		IA ia = new IA();
		MachineLearning ml = new MachineLearning();

		// recette.processInsertion(repo, vf, model, wcd,
		// "./fichiers_test/recettes/");

		// String key = "crêPes";
		// JSONObject j = recette.setJson(repo, vf, model, key);
		// engine.getAllStatements(repo, vf, model, wcd);
		// engine.removeAllStatementsIRI(repo, vf, model, wcd);
		// engine.removeAllUserAimeRecette(repo, vf, model, wcd);
		// engine.getAllStatementsIRI(repo, vf, model, wcd);
		// aliment.addAll(repo, vf, model, wcd,
		// "./fichiers_test/aliments/donnees_nutritionnelles.csv");
		// List<String> result = aliment.getAll(repo);
		// engine.writeFile(result, "./fichiers_test/ia/allergies.txt");
		// user.addAlimentIntoGardeManger(repo, vf, model, wcd, "user3",
		// "agneau",750);
		// user.removeAlimentInGardeManger(repo, vf, model, wcd, "user3",
		// "agneau");
		// user.updateAlimentQuantityInGardeManger(repo, vf, model, wcd,
		// "user2", "agneau", 750);

		// user.addDataTestConnexion(repo, vf, model, wcd,
		// "./fichiers_test/users/connexion.txt");
		// user.getMDP(repo, vf, model);
		// String result = user.checkConnexion(repo, vf, model, "toto", "toto");
		// String result = user.processInscription(repo, vf, model, wcd, "Tata",
		// "grr");
		// List<String> test = new ArrayList<String>();
		// test.add("d'écrevisse");
		// List<String> result = recette.getNamesRecettesByKeyWord(repo, test);
		// List<String> result = recette.getNamesRecettesByDifficulte(repo, vf,
		// model, "inter");
		// List<String> result = recette.getIngredients(repo, "Accras
		// d'écrevisses");
		// List<String> result = aliment.getAll(repo, vf, model);
		// List<String> result = user.getAlimentsFromGardeManger(repo, "user1");
		// List <String> result =
		// user.getAlimentsWithQuantityFromGardeManger(repo, "user2");
		// List<String> result = recette.getAllNamesRecettes(repo);
		// engine.writeFile(result);

		// user.insertAllDataMaladieIntoDB(repo, vf, model, wcd,
		// "./fichiers_test/users/maladies.txt");
		// user.insertAllDataAllergieIntoDB(repo, vf, model, wcd,
		// "./fichiers_test/ia/allergies.txt");
		// List<String> result = user.getAllMaladieFromDB(repo);
		// user.addMaladie(repo, vf, model, wcd, "user1", "Cholestérol");
		// List<String> result = user.getAllRegimeAlimentaireFromDB(repo);
		// user.updateMaladie(repo, vf, model, wcd, "user1", "Diabétique"); //
		// [Cholestérol, Diabétique]
		// user.removeMaladie(repo, vf, model, wcd, "user1", "Cholestérol");
		// List<String> result = user.getUserMaladie(repo, "user1");
		// user.insertAllDataRegimeAlimentaireIntoDB(repo, vf, model, wcd,
		// "./fichiers_test/users/regimes.txt");
		// List<String> result = user.getAllRegimeAlimentaireFromDB(repo);
		// user.addRegimeAlimentaire(repo, vf, model, wcd, "user1",
		// "Végétarien");
		// user.removeRegimeAlimentaire(repo, vf, model, wcd, "user1",
		// "Végétarien");
		// List<String> result = user.getUserRegimeAlimentaire(repo, "user1");
		// user.addAllergie(repo, vf, model, wcd, "user1", "Poire");
		// user.removeAllergie(repo, vf, model, wcd, "user1", "Poire");
		// List<String> result = user.getUserAllergie(repo, "user1");
		// user.addAge(repo, vf, model, wcd, "mallauriep", 23);
		// user.removeAge(repo, vf, model, wcd, "user1");
		// int result = user.getUserAge(repo, "user1");
		// user.insertAllDataGenreIntoDB(repo, vf, model, wcd);
		// user.addGenre(repo, vf, model, wcd, "mallauriep", "Femme");
		// user.removeGenre(repo, vf, model, wcd, "user1");
		// List<String> result = user.getUserGenre(repo, "user1");
		// user.addNiveauActivite(repo, vf, model, wcd, "mallauriep", "Tres
		// actif");
		// user.addPoids(repo, vf, model, wcd, "mallauriep", 50);
		// user.addTaille(repo, vf, model, wcd, "mallauriep", 1.6);
		// user.insertAllDataNiveauActiviteIntoDB(repo, vf, model, wcd,
		// "./fichiers_test/users/niveau_activite.txt");
		// double result = user.calculBesoinCalorique(repo, "mallauriep");
		// user.insertAllDataAllergieIntoDB(repo, vf, model, wcd);
		// List<String> result = user.getAllAllergiesFromDB(repo);

		// ia.processCluster();
		// ia.createProfil(repo);
//		ia.cleanData(repo, "fichiers_test/ia/profils.txt", "fichiers_test/ia/profils_nettoyes.txt");
		// ia.creerDonneesUserAimeRecette(repo, vf, model, wcd);
//		ml.save_cluster("fichiers_test/ia/profils_nettoyes.txt", "fichiers_test/ia/resultat_cluster.txt");
		// "25.0,136.6961201117902,1.5894170109599939,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0"
		List<String> result = ia.suggestionRecetteParCluster(repo,
				"fichiers_test/ia/profils_nettoyes.txt",
				"fichiers_test/ia/resultat_cluster.txt",
				"56.0,137.4507296494346,1.5666020829090357,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0");
		System.out.println(result);
	}

}
