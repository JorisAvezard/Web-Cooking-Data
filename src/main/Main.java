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
		File dataDir = new File("./db/"); // db_test_ia_01  / db_demo
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

		//////////////////////////
		// Remplissage de la base
		//////////////////////////
		
//		aliment.addAll(repo, vf, model, wcd, "./fichiers_test/aliments/donnees_nutritionnelles.csv");
//		recette.processInsertion(repo, vf, model, wcd, "./fichiers/recettes_decoupe_02_00000001/"); // recettes_decoupe_01 fait
//		user.insertAllDataMaladieIntoDB(repo, vf, model, wcd, "./fichiers_test/users/maladies.txt");
//		user.insertAllDataRegimeAlimentaireIntoDB(repo, vf, model, wcd, "./fichiers_test/users/regimes.txt");
//		user.insertAllDataGenreIntoDB(repo, vf, model, wcd);
//		user.insertAllDataNiveauActiviteIntoDB(repo, vf, model, wcd, "./fichiers_test/users/niveau_activite.txt");
//		user.insertAllDataAllergieIntoDB(repo, vf, model, wcd); // changer le
																// nom des
																// fichiers dans
																// la methode

		//////////////////////////////
		// Tests unitaire sur la base
		//////////////////////////////

//		 engine.getAllStatements(repo, vf, model, wcd);
		// engine.removeAllStatementsIRI(repo, vf, model, wcd);
		// engine.removeAllUserAimeRecette(repo, vf, model, wcd);
//		 engine.getAllStatementsIRI(repo, vf, model, wcd);

//		 List<String> result = recette.getIngredients(repo, "Burger bun de riz");
		// System.out.println(result);

//		 List<String> result = aliment.getAll(repo);
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
		// test.add("Riz");
		// test.add("thon");
		// List<String> result = recette.getNamesRecettesByKeyWord(repo, test);
		// List<String> result = recette.getNamesRecettesByDifficulte(repo, vf,
		// model, "inter");

//		 List<String> result = recette.getNamesRecettesByAliments(repo,
//		 "saumon");
		// List<String> result = aliment.getAll(repo, vf, model);
//		 List<Stxring> result = recette.getAllNamesRecettes(repo);
		// engine.writeFile(result);

//		 List<String> result = user.getAllMaladieFromDB(repo);
		// user.addMaladie(repo, vf, model, wcd, "user1", "Cholestérol");
		// List<String> result = user.getAllRegimeAlimentaireFromDB(repo);
		// user.updateMaladie(repo, vf, model, wcd, "user1", "Diabétique");
		// user.removeMaladie(repo, vf, model, wcd, "user1", "Cholestérol");
		// List<String> result = user.getUserMaladie(repo, "user1");
//		 List<String> result = user.getAllRegimeAlimentaireFromDB(repo);
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
		// user.addGenre(repo, vf, model, wcd, "mallauriep", "Femme");
		// user.removeGenre(repo, vf, model, wcd, "user1");
		// List<String> result = user.getUserGenre(repo, "user1");
		// user.addNiveauActivite(repo, vf, model, wcd, "mallauriep", "Tres
		// actif");
		// user.addPoids(repo, vf, model, wcd, "mallauriep", 50);
		// user.addTaille(repo, vf, model, wcd, "mallauriep", 1.6);
		// double result = user.calculBesoinCalorique(repo, "mallauriep");
//		List<String> result = user.getAllNiveauActiviteFromDB(repo);
//		 List<String> result = user.getAllAlimentsFromAllergie(repo, "Oeuf");
//		List<String> qwerty = recette.getCategory(repo, "Abricots gratinés à la cannelle");
//		System.out.println(qwerty);
//		List<String> result = recette.getNamesRecettesByCategory(repo, "sans gluten");
//		List<String> result = recette.getNamesRecettesByAllergie(repo, "Fruits de mer");
//		System.out.println(result);

		
//		ia.createProfilsAleatoire(repo);
		// ia.cleanData(repo,
		// "fichiers_test/ia/profils.txt","fichiers_test/ia/profils_nettoyes.txt");
		// ia.creerDonneesUserAimeRecette(repo, vf, model, wcd);
		// ml.save_cluster("fichiers_test/ia/profils_nettoyes.txt", "fichiers_test/ia/resultat_cluster.txt");
		// "25.0,136.6961201117902,1.5894170109599939,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0"
		// List<String> result = ia.suggestionRecetteParCluster(repo,
		// "fichiers_test/ia/profils_nettoyes.txt",
		// "fichiers_test/ia/resultat_cluster.txt",
		// "56.0,137.4507296494346,1.5666020829090357,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0");
		
//		String result = user.getUserNiveauActivite(repo, "jorisa");
//		int result = user.getUserAge(repo, "jorisa");
//		ia.insertProfilIntoDB(repo, vf, model, wcd, "fichiers_test/ia/profils.txt");
//		ia.processOffline(repo, "fichiers_test/ia/profils_nettoyes.txt", "fichiers_test/ia/resultat_cluster.txt");
//		List<String> result = ia.processOnline(repo, "fichiers_test/ia/profils_nettoyes.txt", "fichiers_test/ia/resultat_cluster.txt", "mallauriep");
//		System.out.println(result);
	}

}
