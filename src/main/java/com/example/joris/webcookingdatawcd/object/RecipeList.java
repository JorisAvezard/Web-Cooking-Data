package com.example.joris.webcookingdatawcd.object;

import java.util.ArrayList;
import java.util.List;

public class RecipeList {

    public List<String> recipes = new ArrayList<String>();

    public RecipeList() {

    }

    public void addRecipe(String recipe) {
        if(!recipes.contains(recipe))
            recipes.add(recipe);
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }
}
