package com.example.joris.webcookingdatawcd.object;

import java.util.List;

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

    public RecetteCuisine() {

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
