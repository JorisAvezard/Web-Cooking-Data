package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import data.Reponse;
import data.ListeRecette;
import data.Recette;
import data.User;

@Path("/service")
public class WebService {
	
	public User user = new User();
	public Recette recette = new Recette();
	File dataDir = new File("./db/");
	NativeStore ns = new NativeStore(dataDir);
	Repository repo = new SailRepository(ns);
	
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
	
/*	@GET
	@Path("/recetteParNom/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParNom(@PathParam(value="expr") String expression) {
		String fields[];
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		fields = expression.split("_");
		for(int i=0; i<fields.length; i++) {
			if(fields[i].length() > 2) {
				list.add(fields[i]);
			}
		}
		
		if(list.size()>0) {
			result = recette.getNamesRecettesByKeyWord(list);
		}
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE PAR NOM] " + listRecette.getRecettes().toString());
		return listRecette;
	}
*/	
	@GET
	@Path("/recetteParNom/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParNom(@PathParam(value="expr") String expression) {
		List<String> test = new ArrayList<String>();
		test.add("Mousse");
		List<String> result = recette.getNamesRecettesByKeyWord(repo, test);
		
		for(int i=0; i<result.size();i++){
			System.out.println(result.get(i));
		}
		
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE PAR NOM] " + listRecette.getRecettes().toString());
		return listRecette;
	}
	
	@GET
	@Path("/recetteParDifficulte/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParDifficulte(@PathParam(value="expr") String expression) {
		List<String> result = new ArrayList<String>();
		result = recette.getNamesRecettesByDifficulte(expression);
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
		result = recette.getNamesRecettesByNote(note);
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
		result = recette.getNamesRecettesByCategory(expression);
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE PAR CATEGORIE]");
		return listRecette;
	}

}
