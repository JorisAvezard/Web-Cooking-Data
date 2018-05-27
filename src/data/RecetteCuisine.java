package data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.repository.Repository;

public class RecetteCuisine {

	public String nom;
	public List<String> ingredients;
	public List<String> personnes;
	public List<String> etapes;
	public List<String> auteur;
	public List<String> tempsTotal;
	public List<String> tempsCuisson;
	public List<String> tempsPreparation;
	public List<String> ustensiles;
	
	public RecetteCuisine(String nom, List<String> ingredients, List<String> personnes, List<String> etapes, List<String> auteur, 
			List<String> tempsTotal, List<String> tempsCuisson, List<String> tempsPreparation, List<String> ustensiles) {
		
		this.nom = nom;
		this.ingredients = ingredients;
		this.personnes = personnes;
		this.etapes = etapes;
		this.auteur = auteur;
		this.tempsTotal = tempsTotal;
		this.tempsCuisson = tempsCuisson;
		this.tempsPreparation = tempsPreparation;
		this.ustensiles = ustensiles;
		
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

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public void setPersonnes(List<String> personnes) {
		this.personnes = personnes;
	}

	public void setEtapes(List<String> etapes) {
		this.etapes = etapes;
	}

	public void setAuteur(List<String> auteur) {
		this.auteur = auteur;
	}

	public void setTempsTotal(List<String> tempsTotal) {
		this.tempsTotal = tempsTotal;
	}

	public void setTempsCuisson(List<String> tempsCuisson) {
		this.tempsCuisson = tempsCuisson;
	}

	public void setTempsPreparation(List<String> tempsPreparation) {
		this.tempsPreparation = tempsPreparation;
	}

	public void setUstensiles(List<String> ustensiles) {
		this.ustensiles = ustensiles;
	}
}
