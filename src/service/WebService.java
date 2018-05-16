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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data.Recette;
import data.User;

@Path("/service")
public class WebService {
	
	@GET
	@Path("/coucou/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String Coucou(@PathParam (value="name")String name) {
		return "Coucou " + name + " !";
	}
	
	@GET
	@Path("/recherche/{recette}")
	@Produces(MediaType.APPLICATION_JSON)
	public void recherche_recette(@PathParam (value="recette")String recette) {
	
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/connexion/{login}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject connexion(@PathParam(value="login") String login, @PathParam(value="password") String password) {
		File dataDir = new File("./db/");
		Repository repository = new SailRepository(new NativeStore(dataDir));
		ValueFactory vf = SimpleValueFactory.getInstance();
		Model model = new TreeModel();
		String state = User.checkConnexion(repository, vf, model, login.toLowerCase(), password);
		String response = "";
		JSONObject jsonObject = new JSONObject();
		if(state.equals("succes")) {
			response = "yes";
		}
		else if(state.equals("echec")) {
			response = "no";
		}
		if(response.equals(""))
			response = "no";
		jsonObject.put("success", response);
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/inscription/{login}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject inscription(@PathParam(value="login") String login, @PathParam(value="password") String password) {
		File dataDir = new File("./db/");
		Repository repository = new SailRepository(new NativeStore(dataDir));
		ValueFactory vf = SimpleValueFactory.getInstance();
		Model model = new TreeModel();
		String wcd = "http://m2bigcookingdata.org/";
		String state = User.processInscription(repository, vf, model, wcd, login.toLowerCase(), password);
		String response = "";
		JSONObject jsonObject = new JSONObject();
		if(state.equals("Le login est déjà pris.")) {
			response = "login pris";
		}
		else if(state.equals("Le mot de passe ne doit pas être vide")) {
			response = "password vide";
		} else if(state.equals("Insertion finie")) {
			response = "yes";
		}
		if(response.equals(""))
			response = "no";
		jsonObject.put("success", response);
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/rechercheRecette/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject rechercheRecette(@PathParam(value="expr") String expression) {
		String fields[];
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		File dataDir = new File("./db/");
		Repository repository = new SailRepository(new NativeStore(dataDir));
		ValueFactory vf = SimpleValueFactory.getInstance();
		Model model = new TreeModel();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		fields = expression.split(" ");
		for(int i=0; i<fields.length; i++) {
			if(fields[i].length() > 2) {
				list.add(fields[i].trim());
			}
		}
		if(list.size()>0) {
			result = Recette.getNamesRecettesByKeyWord(repository, vf, model, list);
		}
		if(result.size()>0) {
			for(int j=0; j<result.size(); j++) {
				jsonArray.add(result.get(j));
			}
		}
		jsonObject.put("recette", jsonArray);
		return jsonObject;
	}
}
