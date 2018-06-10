package com.example.joris.webcookingdatawcd.object;

import java.util.ArrayList;
import java.util.List;

public class RecetteCuisine {

    public String nom = "";
    public List<String> ingredients = new ArrayList<String>();
    public List<String> personnes = new ArrayList<String>();
    public List<String> etapes = new ArrayList<String>();
    public List<String> auteur = new ArrayList<String>();
    public List<String> tempsTotal = new ArrayList<String>();
    public List<String> tempsCuisson = new ArrayList<String>();
    public List<String> tempsPreparation = new ArrayList<String>();
    public List<String> ustensiles = new ArrayList<String>();

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
