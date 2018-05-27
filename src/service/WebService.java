package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import data.Reponse;
import data.GardeManger;
import data.ListeRecette;
import data.Recette;
import data.RecetteCuisine;
import data.User;

@Path("/service")
public class WebService {
	
	public User user = new User();
	public Recette recette = new Recette();
	File dataDir = new File("./db/");
	NativeStore ns = new NativeStore(dataDir);
	Repository repo = new SailRepository(ns);
	ValueFactory vf = SimpleValueFactory.getInstance();
	Model model = new TreeModel();
	String wcd = "http://m2bigcookingdata.org/";
	
	@GET
	@Path("/coucou/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse Coucou(@PathParam (value="name")String name) {
		Reponse data = new Reponse("Coucou " + name);
		System.out.println("[COUCOU] " + data.getData());
		return data;
	}
	
	@GET
	@Path("/connexion/{login}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse connexion(@PathParam(value="login") String login, @PathParam(value="password") String password) {
		String state = user.checkConnexion(login.toLowerCase(), password);
		String response = "";
		if(state.equals("succes")) {
			response = "yes";
		}
		else if(state.equals("echec")) {
			response = "no";
		}
		if(response.equals(""))
			response = "no";
		System.out.println("[CONNEXION] " + response);
		Reponse data = new Reponse(response);
		return data;
	}
	
	@GET
	@Path("/inscription/{login}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse inscription(@PathParam(value="login") String login, @PathParam(value="password") String password) {
		String state = user.processInscription(login.toLowerCase(), password);
		String response = "";
		if(state.equals("Le login est deja pris.")) {
			response = "login pris";
		}
		else if(state.equals("Le mot de passe ne doit pas être vide")) {
			response = "password vide";
		} else if(state.equals("Insertion finie")) {
			response = "yes";
		}
		if(response.equals(""))
			response = "no";
		System.out.println("[INSCRIPTION] " + response);
		Reponse data = new Reponse(response);
		return data;
	}
	
	@GET
	@Path("/recetteParNom/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParNom(@PathParam(value="expr") String expression) {
		List<String> list = new ArrayList<String>();
		list.add(expression);
		List<String> result = recette.getNamesRecettesByKeyWord(repo, list);
		
		for(int i=0; i<result.size();i++){
			System.out.println(result.get(i));
		}
		
		ListeRecette listRecette = new ListeRecette();
		System.out.println("[RECHERCHE RECETTE PAR NOM] " + listRecette.getRecettes().toString());
		return listRecette;
	}
	
	@GET
	@Path("/recetteParDifficulte/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParDifficulte(@PathParam(value="expr") String expression) {
		List<String> result = new ArrayList<String>();
		result = recette.getNamesRecettesByDifficulte(repo, expression);
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE PAR DIFFICULTE]");
		return listRecette;
	}
	
	@GET
	@Path("/recetteParNote/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParNote(@PathParam(value="expr") String expression) {
		float note = Float.parseFloat(expression);
		List<String> result = new ArrayList<String>();
		result = recette.getNamesRecettesByNote(repo, note);
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE PAR NOTE]");
		return listRecette;
	}
	
	@GET
	@Path("/recetteParCategorie/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParCategorie(@PathParam(value="expr") String expression) {
		List<String> result = new ArrayList<String>();
		result = recette.getNamesRecettesByCategory(repo, expression);
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE PAR CATEGORIE]");
		return listRecette;
	}
	
	@GET
	@Path("/recette/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public RecetteCuisine recette (@PathParam(value="expr") String nomRecette) {
		List<String> ingredients = recette.getIngredients(repo, nomRecette);
		List<String> personnes = recette.getNbPersonnes(repo, nomRecette);
		List<String> etapes = recette.getEtapes(repo, nomRecette);
		List<String> auteur = recette.getEtapes(repo, nomRecette);
		List<String> tempsTotal = recette.getTempsTotal(repo, nomRecette);
		List<String> tempsCuisson = recette.getTempsCuisson(repo, nomRecette);
		List<String> tempsPreparation = recette.getTempsPreparation(repo, nomRecette);
		List<String> ustensiles = recette.getUstensiles(repo, nomRecette);
		RecetteCuisine recetteCuisine = new RecetteCuisine(nomRecette, ingredients, personnes, etapes, auteur, tempsTotal, tempsCuisson, tempsPreparation, ustensiles);
		System.out.println("[RECUPERATION DONNEES RECETTE ("+ nomRecette +")]");
		return recetteCuisine;
	}
/*	
	@GET
	@Path("/alimentGardeManger/{login}/{aliment}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse addAlimentGardeManger (@PathParam(value="login") String login, @PathParam(value="aliment") String aliment) {
		String data = user.addAlimentIntoGardeManger(repo, vf, model, wcd, login, aliment);
		Reponse reponse = new Reponse(data);
		System.out.println("[RECUPERATION DONNEES RECETTE ("+ data +")]");
		return reponse;
	}
*/
	
	@GET
	@Path("/contenuGardeManger/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public GardeManger addAlimentGardeManger (@PathParam(value="login") String login) {
		List<String> contenu = user.getAlimentsFromGardeManger(repo, login);
		GardeManger gardeManger = new GardeManger(contenu);
		System.out.println("[RECUPERATION DONNEES GARDE MANGER] DE "+ login +" : \n"+ gardeManger.toString());
		return gardeManger;
	}
}
