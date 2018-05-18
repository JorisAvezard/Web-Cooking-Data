package service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.Reponse;
import data.ListeRecette;
import data.Recette;
import data.User;

@Path("/service")
public class WebService {
	
	public User user = new User();
	public Recette recette = new Recette();
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
	@Path("/rechercheRecette/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecette(@PathParam(value="expr") String expression) {
		String fields[];
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		fields = expression.split(" ");
		for(int i=0; i<fields.length; i++) {
			if(fields[i].length() > 2) {
				list.add(fields[i].trim());
			}
		}
		if(list.size()>0) {
			result = recette.getNamesRecettesByKeyWord(list);
		}
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(result);
		System.out.println("[RECHERCHE RECETTE]");
		return listRecette;
	}

}
