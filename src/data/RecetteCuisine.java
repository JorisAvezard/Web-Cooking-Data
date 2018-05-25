package data;

import java.util.List;

import org.eclipse.rdf4j.repository.Repository;

public class RecetteCuisine {

	public Recette recette = new Recette();
	Repository repo;
	public String nom;
	public List<String> ingredients;
	public List<String> personnes;
	public List<String> etapes;
	public List<String> auteur;
	public List<String> tempsTotal;
	public List<String> tempsCuisson;
	public List<String> tempsPreparation;
	public List<String> ustensiles;
	
	public RecetteCuisine(String nom, Repository repo) {
		this.nom = nom;
		this.repo = repo;
		ingredients = recette.getIngredients(repo, nom);
		personnes = recette.getNbPersonnes(repo, nom);
		etapes = recette.getEtapes(repo, nom);
		auteur = recette.getEtapes(repo, nom);
		tempsTotal = recette.getTempsTotal(repo, nom);
		tempsCuisson = recette.getTempsCuisson(repo, nom);
		tempsPreparation = recette.getTempsPreparation(repo, nom);
		ustensiles = recette.getUstensiles(repo, nom);
	}

	public Recette getRecette() {
		return recette;
	}

	public Repository getRepo() {
		return repo;
	}

	public String getNom() {
		return nom;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public List<String> getPersonnes() {
		return personnes;
	}

	public List<String> getEtapes() {
		return etapes;
	}

	public List<String> getAuteur() {
		return auteur;
	}

	public List<String> getTempsTotal() {
		return tempsTotal;
	}

	public List<String> getTempsCuisson() {
		return tempsCuisson;
	}

	public List<String> getTempsPreparation() {
		return tempsPreparation;
	}

	public List<String> getUstensiles() {
		return ustensiles;
	}
}
