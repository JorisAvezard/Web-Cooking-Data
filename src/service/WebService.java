package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
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
import data.Aliment;
import data.AlimentWS;
import data.CoucouObj;
import data.GardeManger;
import data.ListeRecette;
import data.Recette;
import data.RecetteCuisine;
import data.User;
import engine.ClientGardeManger;

@Path("/service")
public class WebService {
	
	public User user = new User();
	public Recette recette = new Recette();
	public Aliment aliment = new Aliment();
	public ClientGardeManger client = new ClientGardeManger();
	public static String a1 = "", a2 = "", lg = "";
	public static boolean gardeMangerIsOk = false;
	String directory = "C:/Users/Joris Avezard/Workspaces/EE/BigCookingData";
	File dataDir = new File(directory + "/db/");
	NativeStore ns = new NativeStore(dataDir);
	Repository repo = new SailRepository(ns);
	ValueFactory vf = SimpleValueFactory.getInstance();
	Model model = new TreeModel();
	String wcd = "http://m2bigcookingdata.org/";
	
	@GET
	@Path("/coucou/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public CoucouObj Coucou(@PathParam (value="name")String name) {
		System.out.println("[COUCOU] " + name);
		
		CoucouObj data = new CoucouObj(name);
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
	@Path("/inscription/{login}/{password}/{genre}/{age}/{taille}/{poids}/{activite}/{maladie}/{regime}/{allergie}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse inscription(@PathParam(value="login") String login, @PathParam(value="password") String password,
			@PathParam(value="genre") String genre, @PathParam(value="age") int age,
			@PathParam(value="taille") double taille, @PathParam(value="poids") double poids,
			@PathParam(value="activite") String activite, @PathParam(value="maladie") String maladie,
			@PathParam(value="regime") String regime, @PathParam(value="allergie") String allergie) {
		String state = user.processInscription(login.toLowerCase(), password);
		String response = "";
		if(state.equals("Le login est deja pris.")) {
			response = "login pris";
		}
		else if(state.equals("Le mot de passe ne doit pas être vide")) {
			response = "password vide";
		} else if(state.equals("Insertion finie")) {
			response = "yes";
			user.addAge(repo, vf, model, wcd, login, age);
			if(!allergie.equals(""))
			user.addAllergie(repo, vf, model, wcd, login, allergie);
			user.addGenre(repo, vf, model, wcd, login, genre);
			user.addTaille(repo, vf, model, wcd, login, taille);
			user.addPoids(repo, vf, model, wcd, login, poids);
			user.addRegimeAlimentaire(repo, vf, model, wcd, login, regime);
			if(!maladie.equals(""))
				user.addMaladie(repo, vf, model, wcd, login, maladie);
			user.addNiveauActivite(repo, vf, model, wcd, login, activite);
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
		String[] fields = expression.split("_");
		for(int i=0; i<fields.length; i++) {
			list.add(fields[i]);
		}
		List<String> result = recette.getNamesRecettesByKeyWord(repo, list);
		
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
	@Path("/recetteCuisine/{rec}")
	@Produces(MediaType.APPLICATION_JSON)
	public RecetteCuisine getRecettesCuisine (@PathParam(value="rec") String nomRecette) {
		System.out.println("Recette Cuisine : " + nomRecette);
		
		String nom = "";
		byte[] byteData = nomRecette.getBytes();
		for(int b=0; b<byteData.length; b++) {
			if(byteData[b] == 34 || byteData[b] == 44) {
				nom = nom + "\\" + nomRecette.charAt(b);
			}
			else {
				nom = nom + nomRecette.charAt(b);
			}
		}
		System.out.println("Recette Cuisine : " + nom);
		nom = nom.trim();
		
		List<String> ingredients = recette.getIngredients(repo, nom);
		System.out.println("[INGREDIENTS] " + ingredients);
		List<String> personnes = recette.getNbPersonnes(repo, nom);
		System.out.println("[PERSONNES] " + personnes);
		List<String> etapes = recette.getEtapes(repo, nom);
		System.out.println("[ETAPES] " + etapes);
		List<String> auteur = recette.getAuteur(repo, nom);
		System.out.println("[AUTEUR] " + auteur);
		List<String> tempsTotal = recette.getTempsTotal(repo, nom);
		System.out.println("[TEMPS TOTAL] " + tempsTotal);
		List<String> tempsCuisson = recette.getTempsCuisson(repo, nom);
		System.out.println("[TEMPS CUISSON] " + tempsCuisson);
		List<String> tempsPreparation = recette.getTempsPreparation(repo, nom);
		System.out.println("[TEMPS PREP.] " + tempsPreparation);
		List<String> ustensiles = recette.getUstensiles(repo, nom);
		System.out.println("[USTENSILES] " + ustensiles);
		nomRecette = nomRecette.replaceAll("_", " ");
		System.out.println("Recette :" + nomRecette);
		RecetteCuisine recetteCuisine = new RecetteCuisine(nomRecette, ingredients, personnes, etapes, auteur, tempsTotal, tempsCuisson, tempsPreparation, ustensiles);
		System.out.println("[RECUPERATION DONNEES RECETTE] ("+ nomRecette +")");
		return recetteCuisine;

	}
	
	@GET
	@Path("/contenuGardeManger/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public GardeManger alimentGardeMangerEnBase (@PathParam(value="login") String login) {
		List<String> contenu = user.getAlimentsWithQuantityFromGardeManger(repo, login);
		System.out.println("[RECUPERATION DONNEES GARDE MANGER] DE "+ login +" : \n"+ contenu);
		String fileName = directory + "/fichiers_test/aliments/mesures.txt";
		String line = null;
		
		for(int i=0; i<contenu.size(); i++) {
			String aliment = contenu.get(i);
			i = i+1;
			float quantite_fl = Float.parseFloat(contenu.get(i));
			int quantite = (int) quantite_fl;
			
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		
				while ((line = bufferedReader.readLine()) != null) {
					String[] fields = line.split(";");
					if(aliment.equals(fields[0])) {
						if(fields[1].equals("entites")) {
							contenu.set(i, quantite + "");
						} else {
							contenu.set(i, quantite + fields[1]);
						}
					}
				}
				bufferedReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		GardeManger gardeManger = new GardeManger(contenu);
		
		System.out.println("[RECUPERATION DONNEES GARDE MANGER] DE "+ login +" : \n"+ gardeManger.toString());
		return gardeManger;
	}
	
	@GET
	@Path("/alimentBase")
	@Produces(MediaType.APPLICATION_JSON)
	public AlimentWS alimentEnBase () {
		List<String> data = aliment.getAll(repo);
		AlimentWS reponse = new AlimentWS(data);
		System.out.println("[RECUPERATION DONNEES ALIMENTS ("+ data +")]");
		return reponse;
	}
	
	@GET
	@Path("/addAlimentBase/{aliment}/{quantite}/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public void addAlimentBase (@PathParam(value="aliment") String aliment, 
			@PathParam(value="quantite") String quantite, @PathParam(value="login") String login) {
		float quantite_fl = Float.parseFloat(quantite);
		aliment = aliment.replaceAll("_", " ");
		List<String> contenu = user.getAlimentsWithQuantityFromGardeManger(repo, login);
		boolean trouve = false;
		for(int i=0; i<contenu.size(); i+=2) {
			if(aliment.equals(contenu.get(i))) {
				trouve = true;
				user.updateAlimentQuantityInGardeManger(repo, vf, model, wcd, login, aliment, quantite_fl);
				System.out.println("[UPDATE DONNEES ALIMENTS ("+ aliment +", "+ quantite +", "+ login +")]");
			}
		}
		if(trouve == false) {
			user.addAlimentIntoGardeManger(repo, vf, model, wcd, login, aliment, quantite_fl);
			System.out.println("[AJOUT DONNEES ALIMENTS ("+ aliment +", "+ quantite +", "+ login +")]");
		}
	}
	
	@GET
	@Path("/getInfosUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse getInfosUsers () {
		List<String> activites = user.getAllNiveauActiviteFromDB(repo);
		System.out.println("[GET NIVEAUX D'ACTIVITES]");
		List<String> maladies = user.getAllMaladieFromDB(repo);
		System.out.println("[GET MALADIES]");
		List<String> regimes = user.getAllRegimeAlimentaireFromDB(repo);
		System.out.println("[GET REGIMES]");
		List<String> allergies = user.getAllAllergiesFromDB(repo);
		System.out.println("[GET ALLERGIES]");
		
		Reponse reponse = new Reponse();
		reponse.setActivites(activites);
		reponse.setRegimes(regimes);
		reponse.setMaladies(maladies);
		reponse.setAllergies(allergies);
		
		return reponse;
	}
	
	@GET
	@Path("/infosUsers/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse addInfosUsers (@PathParam(value="login") String login) {
		String age = String.valueOf(user.getUserAge(repo, login));
		System.out.println("[GET AGE USER] " + age);
		List<String> allergie = user.getUserAllergie(repo, login);
		System.out.println("[GET ALLERGIE USER] " + allergie);
		String genre = user.getUserGenre(repo, login);
		System.out.println("[GET GENRE USER] " + genre);
		String taille = String.valueOf(user.getUserTaille(repo, login));
		System.out.println("[GET TAILLE USER] " + taille);
		String poids = String.valueOf(user.getUserPoids(repo, login));
		System.out.println("[GET POIDS USER] " + poids);
		List<String> regime = user.getUserRegimeAlimentaire(repo, login);
		System.out.println("[GET REGIME USER] " + regime);
		List<String> maladie = user.getUserMaladie(repo, login);
		System.out.println("[GET MALADIE USER] " + maladie);
		String activite = user.getUserNiveauActivite(repo, login);
		System.out.println("[GET ACTIVITE USER] " + activite);
		String besoin = String.valueOf(user.calculBesoinCalorique(repo, login));
		System.out.println("[GET BESOIN USER] " + besoin);
		Reponse reponse = new Reponse();
		reponse.setAge(age);
		reponse.setSexe(genre);
		reponse.setTaille(taille);
		reponse.setPoids(poids);
		reponse.setActivite(activite);
		reponse.setBesoin(besoin);
		if(allergie.size()>0)
			reponse.setAllergie(allergie.get(0));
		else
			reponse.setAllergie("Non disponible");
		if(regime.size()>0)
			reponse.setRegime(regime.get(0));
		else
			reponse.setRegime("Non disponible");
		
		if(maladie.size()>0)
			reponse.setMaladie(maladie.get(0));
		else
			reponse.setMaladie("Non disponible");
		
		return reponse;
	}
	
	
	////////////////////////////////////////////////////////////////////
	@GET
	@Path("/chargerContenuGardeManger/{login}/{aliment1}/{aliment2}")
	@Produces(MediaType.APPLICATION_JSON)
	public void loadAlimentGardeMangerConnecte (@PathParam(value="login") String login, 
			@PathParam(value="aliment1") String aliment1, @PathParam(value="aliment2") String aliment2) {
		try {
			a1 = aliment1.replaceAll("_", " ");
			a2 = aliment2.replaceAll("_", " ");
			lg = login;
			client.envoi();
			System.out.println("En attente du serveur du garde manger ...");
			while(gardeMangerIsOk != true) {
				//System.out.println("-");
			}
			gardeMangerIsOk = false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("/peser/{poids1}/{poids2}")
	@Produces(MediaType.APPLICATION_JSON)
	public String Peser(@PathParam (value="poids1")String poids1, @PathParam (value="poids2")String poids2) {
		System.out.println("Poids (en g): " + poids1 + " "+ poids2);
		float fl_poids1 = Float.parseFloat(poids1);
		float fl_poids2 = Float.parseFloat(poids2);
		String login = lg;
		String aliment1 = a1;
		String aliment2 = a2;
		
		List<String> contenu = user.getAlimentsWithQuantityFromGardeManger(repo, login);
		boolean trouve_aliment1 = false;
		boolean trouve_aliment2 = false;
		for(int i=0; i<contenu.size(); i+=2) {
			if(aliment1.equals(contenu.get(i))) {
				trouve_aliment1 = true;
				user.updateAlimentQuantityInGardeManger(repo, vf, model, wcd, login, aliment1, fl_poids1);
				System.out.println("[UPDATE DONNEES ALIMENTS ("+ aliment +", "+ poids1 +", "+ login +")]");
			}
			else if(aliment2.equals(contenu.get(i))) {
				trouve_aliment2 = true;
				user.updateAlimentQuantityInGardeManger(repo, vf, model, wcd, login, aliment2, fl_poids2);
				System.out.println("[UPDATE DONNEES ALIMENTS ("+ aliment +", "+ poids2 +", "+ login +")]");
			}
		}
		
		if(trouve_aliment1 == false) {
			user.addAlimentIntoGardeManger(repo, vf, model, wcd, login, aliment1, fl_poids1);
			System.out.println("[AJOUT DONNEES ALIMENTS ("+ aliment1 +", "+ fl_poids1 +", "+ login +")]");
		}
		if(trouve_aliment2 == false) {
			user.addAlimentIntoGardeManger(repo, vf, model, wcd, login, aliment2, fl_poids2);
			System.out.println("[AJOUT DONNEES ALIMENTS ("+ aliment2 +", "+ fl_poids2 +", "+ login +")]");
		}
		
		lg = "";
		gardeMangerIsOk = true;
		return "Ok";
	}
}
