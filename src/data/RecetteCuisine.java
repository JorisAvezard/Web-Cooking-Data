package data;

import java.util.List;

public class RecetteCuisine {

	public String nom;
	public List<String> ingredients;
	public int personnes;
	public List<String> etapes;
	public List<String> auteur;
	public List<String> tempsTotal;
	public List<String> tempsCuisson;
	public List<String> tempsPreparation;
	public List<String> ustensiles;
	public String image;
	public double note;
	
	public RecetteCuisine() {
		
		
	}
	
	public RecetteCuisine(String nom, List<String> ingredients, int personnes, List<String> etapes, List<String> auteur, 
			List<String> tempsTotal, List<String> tempsCuisson, List<String> tempsPreparation, List<String> ustensiles, String image, double note) {
		
		this.nom = nom;
		this.ingredients = ingredients;
		this.personnes = personnes;
		this.etapes = etapes;
		this.auteur = auteur;
		this.tempsTotal = tempsTotal;
		this.tempsCuisson = tempsCuisson;
		this.tempsPreparation = tempsPreparation;
		this.ustensiles = ustensiles;
		this.image = image;
		this.note = note;
		
	}

	public String getNom() {
		return nom;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public int getPersonnes() {
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
	
	public double getNote() {
		return note;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public void setPersonnes(int personnes) {
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
	
	public void setNote(double note) {
		this.note = note;
	}
}
