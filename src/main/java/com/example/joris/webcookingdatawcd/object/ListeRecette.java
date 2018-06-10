package com.example.joris.webcookingdatawcd.object;

import java.util.ArrayList;
import java.util.List;

public class ListeRecette {

    public List<String> recettes = new ArrayList<String>();

    public ListeRecette() {

    }

    public void addRecipe(String recipe) {
        if(!recettes.contains(recipe))
            recettes.add(recipe);
    }

    public List<String> getRecettes() {
        return recettes;
    }

    public void setRecettes(List<String> recettes) {
        this.recettes = recettes;
    }
}
