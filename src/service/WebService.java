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
import javax.ws.rs.POST;
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
		String state = user.checkConnexion(repo, login.toLowerCase(), password);
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
			@PathParam(value="taille") int taille, @PathParam(value="poids") double poids,
			@PathParam(value="activite") String activite, @PathParam(value="maladie") String maladie,
			@PathParam(value="regime") String regime, @PathParam(value="allergie") String allergie) {
		activite = activite.replaceAll("_", " ");
		maladie = maladie.replaceAll("_", " ");
		regime = regime.replaceAll("_", " ");
		allergie = allergie.replaceAll("_", " ");
		System.out.println("genre : " + genre);
		System.out.println("age : " + age);
		System.out.println("taille : " + taille);
		System.out.println("poids : " + poids);
		System.out.println("activite : " + activite);
		System.out.println("maladie : " + maladie);
		System.out.println("regime : " + regime);
		System.out.println("allergie : " + allergie);
		String state = user.processInscription(repo, login.toLowerCase(), password);
		String response = "";
		if(state.equals("Le login est deja pris.")) {
			response = "login pris";
		}
		else if(state.equals("Le mot de passe ne doit pas être vide")) {
			response = "password vide";
		} else if(state.equals("Insertion finie")) {
			response = "yes";
			user.addAge(repo, vf, model, wcd, login, age);
			if(!allergie.equals("Aucune"))
				user.addAllergie(repo, vf, model, wcd, login, allergie);
			user.addGenre(repo, vf, model, wcd, login, genre);
			user.addTaille(repo, vf, model, wcd, login, taille);
			user.addPoids(repo, vf, model, wcd, login, poids);
			user.addRegimeAlimentaire(repo, vf, model, wcd, login, regime);
			if(!maladie.equals("Aucune"))
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
		long debut = System.currentTimeMillis();
		List<String> result = recette.getNamesRecettesByKeyWord(repo, list);
		long fin = System.currentTimeMillis();
		System.out.println("[Recette par nom] temps : " + (fin - debut));
		
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
	@Path("/recetteParContenu/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListeRecette rechercheRecetteParContenu(@PathParam(value="login") String login) {
		List<String> aliments = user.getAllAlimentsFromGardeManger(repo, login);
		List<String> recettes = new ArrayList<String>();
		
		for(int i=0; i<aliments.size(); i++) {
			List<String> tmp = recette.getNamesRecettesByAliments(repo, aliments.get(i));
			
			for(int j=0; j<tmp.size(); j++) {
				if(!recettes.contains(tmp.get(j)))
					recettes.add(tmp.get(j));
			}
		}
		ListeRecette listRecette = new ListeRecette();
		listRecette.setRecettes(recettes);
		System.out.println("[RECHERCHE RECETTE PAR CONTENU]");
		return listRecette;
	}
	
	@GET
	@Path("/recetteCuisine/{rec}")
	@Produces(MediaType.APPLICATION_JSON)
	public RecetteCuisine getRecettesCuisine (@PathParam(value="rec") String nomRecette) {
		nomRecette = nomRecette.replaceAll("_", " ");
		System.out.println("Recette Cuisine : " + nomRecette);
		
		List<String> ingredients = recette.getIngredients(repo, nomRecette);
		System.out.println("[INGREDIENTS] " + ingredients);
		int personnes = recette.getNbPersonnes(repo, nomRecette);
		System.out.println("[PERSONNES] " + personnes);
		List<String> etapes = recette.getEtapes(repo, nomRecette);
		System.out.println("[ETAPES] " + etapes);
		List<String> auteur = recette.getAuteur(repo, nomRecette);
		System.out.println("[AUTEUR] " + auteur);
		List<String> tempsTotal = recette.getTempsTotal(repo, nomRecette);
		System.out.println("[TEMPS TOTAL] " + tempsTotal);
		List<String> tempsCuisson = recette.getTempsCuisson(repo, nomRecette);
		System.out.println("[TEMPS CUISSON] " + tempsCuisson);
		List<String> tempsPreparation = recette.getTempsPreparation(repo, nomRecette);
		System.out.println("[TEMPS PREP.] " + tempsPreparation);
		List<String> ustensiles = recette.getUstensiles(repo, nomRecette);
		System.out.println("[USTENSILES] " + ustensiles);
		String image = recette.getImage(repo, nomRecette);
		System.out.println("Recette :" + nomRecette);
		RecetteCuisine recetteCuisine = new RecetteCuisine(nomRecette, ingredients, personnes, etapes, auteur, tempsTotal, tempsCuisson, tempsPreparation, ustensiles, image);
		System.out.println("[RECUPERATION DONNEES RECETTE] ("+ nomRecette +")");
		return recetteCuisine;

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
	public Reponse infosUsers (@PathParam(value="login") String login) {
		String age = String.valueOf(user.getUserAge(repo, login));
		System.out.println("[GET AGE USER] " + age);
		String allergie = user.getUserAllergie(repo, login);
		System.out.println("[GET ALLERGIE USER] " + allergie);
		String genre = user.getUserGenre(repo, login);
		System.out.println("[GET GENRE USER] " + genre);
		String taille = String.valueOf(user.getUserTaille(repo, login));
		System.out.println("[GET TAILLE USER] " + taille);
		String poids = String.valueOf((int)user.getUserPoids(repo, login));
		System.out.println("[GET POIDS USER] " + poids);
		String regime = user.getUserRegimeAlimentaire(repo, login);
		System.out.println("[GET REGIME USER] " + regime);
		String maladie = user.getUserMaladie(repo, login);
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
		if(!allergie.equals("Aucun"))
			reponse.setAllergie(allergie);
		else
			reponse.setAllergie("Aucune");
		reponse.setRegime(regime);
		
		if(!maladie.equals("Aucun"))
			reponse.setMaladie(maladie);
		else
			reponse.setMaladie("Aucune");
		
		return reponse;
	}
	
	@GET
	@Path("/addFavori/{login}/{recette}")
	public void addFavori(@PathParam(value="login") String login, @PathParam(value="recette") String nameRecette) {
		List<String> favoris = user.getFavoriRecette(repo, login);
		boolean trouve = false;
		nameRecette = nameRecette.replaceAll("_", " ");
		for(int i=0; i<favoris.size(); i++) {
			if(nameRecette.equals(favoris.get(i))) {
				trouve = true;
			}
		}
		if(trouve == false) {
			user.addFavoriRecette(repo, vf, model, wcd, login, nameRecette);
		}
	}
	
	@GET
	@Path("/addNonAimeRecette/{login}/{recette}")
	public void addNonAimeRecette(@PathParam(value="login") String login, @PathParam(value="recette") String nameRecette) {
		List<String> nonAime = user.getPasAimeRecette(repo, login);
		boolean trouve = false;
		nameRecette = nameRecette.replaceAll("_", " ");
		for(int i=0; i<nonAime.size(); i++) {
			if(nameRecette.equals(nonAime.get(i))) {
				trouve = true;
			}
		}
		if(trouve == false) {
			user.addPasAimeRecette(repo, vf, model, wcd, login, nameRecette);
		}
	}
	
	@GET
	@Path("/addAimeRecette/{login}/{recette}")
	public void addAimeRecette(@PathParam(value="login") String login, @PathParam(value="recette") String nameRecette) {
		List<String> aime = user.getAimeRecette(repo, login);
		boolean trouve = false;
		nameRecette = nameRecette.replaceAll("_", " ");
		for(int i=0; i<aime.size(); i++) {
			if(nameRecette.equals(aime.get(i))) {
				trouve = true;
			}
		}
		if(trouve == false) {
			user.addAimeRecette(repo, vf, model, wcd, login, nameRecette);
		}
	}
	
	@GET
	@Path("/getFavori/{login}/")
	public ListeRecette getFavori(@PathParam(value="login") String login) {
		List<String> favoris = user.getAimeRecette(repo, login);
		ListeRecette listeRecette = new ListeRecette();
		listeRecette.setRecettes(favoris);
		return listeRecette;
	}
	
	@GET
	@Path("/addRecetteConsulte/{login}/{recette}")
	public void addRecetteConsulte(@PathParam(value="login") String login, @PathParam(value="recette") String nameRecette) {
		List<String> consultes = user.getConsulteRecette(repo, login);
		boolean trouve = false;
		nameRecette = nameRecette.replaceAll("_", " ");
		for(int i=0; i<consultes.size(); i++) {
			if(nameRecette.equals(consultes.get(i))) {
				trouve = true;
			}
		}
		if(trouve == false) {
			user.addConsulteRecette(repo, vf, model, wcd, login, nameRecette);
		}
	}
	
	@GET
	@Path("/getRecetteConsulte/{login}")
	public ListeRecette getRecetteConsulte(@PathParam(value="login") String login) {
		List<String> consultes = user.getConsulteRecette(repo, login);
		ListeRecette listeRecette = new ListeRecette();
		listeRecette.setRecettes(consultes);
		return listeRecette;
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
			System.out.println("En attente du serveur du garde manger ...");
			client.envoi();
			
			Thread.sleep(5000);
			
			gardeMangerIsOk = false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("FINI");
	}
	
	
	@GET
	@Path("/addAlimentBase/{aliment}/{quantite}/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public void addAlimentBase (@PathParam(value="aliment") String aliment, 
			@PathParam(value="quantite") String quantite, @PathParam(value="login") String login) {
		float quantite_fl = Float.parseFloat(quantite);
		aliment = aliment.replaceAll("_", " ");
		System.out.println("[Add aliment base] " + aliment + ", " + quantite + ", " + login);
		List<String> contenu = user.getAlimentFromNonCapteurGardeManger(repo, login);
		boolean trouve = false;
		for(int i=0; i<contenu.size(); i++) {
			if(aliment.equals(contenu.get(i))) {
				trouve = true;
				user.updateAlimentQuantityInNonCapteurGardeManger(repo, vf, model, wcd, login, aliment, quantite_fl);
				System.out.println("[UPDATE DONNEES ALIMENTS ("+ aliment +", "+ quantite +", "+ login +")]");
			}
		}
		
		if(trouve == false) {
			System.out.println("[AJOUT DONNEES ALIMENTS ("+ aliment +", "+ quantite +", "+ login +")]");
			user.addAlimentIntoNonCapteurGardeManger(repo, vf, model, wcd, login, aliment, quantite_fl);
		} 
	}
	
	@GET
	@Path("deleteAlimentBase/{aliment}/{login}")
	public void deleteAlimentBase(@PathParam(value="aliment") String aliment, @PathParam(value="login") String login) {
		user.removeAlimentInNonCapteurGardeManger(repo, vf, model, wcd, login, aliment);
		System.out.println("[deleteAlimentBase ("+ aliment + " de" + login +")]");
	}
	
	
	@GET
	@Path("/contenuGardeManger/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public GardeManger alimentGardeMangerEnBase (@PathParam(value="login") String login) {
		List<String> contenu = user.getAllAlimentsWithQuantityFromGardeManger(repo, login);
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
	@Path("/peser/{poids1}/{poids2}")
	@Produces(MediaType.APPLICATION_JSON)
	public void Peser(@PathParam (value="poids1")String poids1, @PathParam (value="poids2")String poids2) {
		System.out.println("Poids (en g): " + poids1 + " "+ poids2);
		float fl_poids1 = Float.parseFloat(poids1);
		float fl_poids2 = Float.parseFloat(poids2);
		String login = lg;
		String aliment1 = a1;
		String aliment2 = a2;
		
		List<String> contenu_1 = user.getAlimentWithQuantityFromCapteurOneGardeManger(repo, login);
		List<String> contenu_2 = user.getAlimentWithQuantityFromCapteurTwoGardeManger(repo, login);
		boolean trouve_aliment1 = false;
		boolean trouve_aliment2 = false;
		for(int i=0; i<contenu_1.size(); i+=2) {
			if(aliment1.equals(contenu_1.get(i))) {
				trouve_aliment1 = true;
				if(fl_poids1 == 0) {
					System.out.println("[REMOVE ALIMENT] DE "+ login +" : \n"+ aliment1);
					user.removeAlimentInCapteurOneGardeManger(repo, vf, model, wcd, login, aliment1);
				} else {
					user.updateAlimentQuantityInCapteurOneGardeManger(repo, vf, model, wcd, login, aliment1, fl_poids1);
					System.out.println("[UPDATE DONNEES ALIMENTS 1 ("+ aliment1 +", "+ poids1 +", "+ login +")]");
				}
			}
			if(aliment2.equals(contenu_2.get(i))) {
				trouve_aliment2 = true;
				if(fl_poids2 == 0) {
					System.out.println("[REMOVE ALIMENT] DE "+ login +" : \n"+ aliment2);
					user.removeAlimentInCapteurTwoGardeManger(repo, vf, model, wcd, login, aliment2);
				} else {
					user.updateAlimentQuantityInCapteurTwoGardeManger(repo, vf, model, wcd, login, aliment2, fl_poids2);
					System.out.println("[UPDATE DONNEES ALIMENTS 2 ("+ aliment2 +", "+ poids2 +", "+ login +")]");
				}
			}
		}
		
		if(trouve_aliment1 == false && aliment1 != "") {
			user.removeAlimentInCapteurOneGardeManger(repo, vf, model, wcd, login, aliment1);
			user.addAlimentIntoCapteurOneGardeManger(repo, vf, model, wcd, login, aliment1, fl_poids1);
			System.out.println("[AJOUT DONNEES ALIMENTS ("+ aliment1 +", "+ fl_poids1 +", "+ login +")]");
		}
		if(trouve_aliment2 == false && aliment2 != "") {
			user.removeAlimentInCapteurTwoGardeManger(repo, vf, model, wcd, login, aliment2);
			user.addAlimentIntoCapteurTwoGardeManger(repo, vf, model, wcd, login, aliment2, fl_poids2);
			System.out.println("[AJOUT DONNEES ALIMENTS ("+ aliment2 +", "+ fl_poids2 +", "+ login +")]");
		}
		
		lg = "";
		gardeMangerIsOk = true;
	}
	
	@GET
	@Path("/quantityOfAliment/{aliment}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reponse quantityOfAliment (@PathParam(value="aliment") String aliment) {
		String fileName = directory + "/fichiers_test/aliments/mesures.txt";
		String line = null;
		String[] fields = null;
		String value = "";
		boolean trouve = false;
		aliment = aliment.replaceAll("_", " ");
		System.out.println("[QUANTITY OF "+ aliment +"]");
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			while ((line = bufferedReader.readLine()) != null && trouve == false) {
				fields = line.split(";");
				if(fields[0].equals(aliment)) {
					value = fields[1];
					trouve = true;
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(value.equals("g")) {
			value = "grammes";
		}
		if(value.equals("entites")) {
			value = "entité(s)";
		}
		System.out.println("[Quantite of "+ aliment +"] : " + value);
		Reponse reponse = new Reponse(value);
		return reponse;
	}
	
	@GET
	@Path("/updateGardeManger/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public void updateGardeManger (@PathParam(value="login") String login) {
		try {
			List<String> list1 = user.getAlimentFromCapteurOneGardeManger(repo, login);
			List<String> list2 = user.getAlimentFromCapteurTwoGardeManger(repo, login);
			if(list1.size() > 0) {
				a1 = list1.get(0);
			}
			else {
				a1 = "";
			}
			if(list2.size() > 0) {
				a2 = list2.get(0);
			}
			else {
				a2 = "";
			}
			System.out.println("a1 : " + a1);
			System.out.println("a2 : " + a2);
			lg = login;
			System.out.println("En attente du serveur du garde manger ...");
			client.envoi();
			Thread.sleep(5000);
			gardeMangerIsOk = false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("FIN");
	}
}
