package com.example.joris.webcookingdatawcd.object;

import java.util.ArrayList;
import java.util.List;

public class AlimentsAvecValeursNutritionelles {

    List<String> intitules = new ArrayList<String>();
    List<String> aliments = new ArrayList<String>();
    List<List<String>> valeurs = new ArrayList<List<String>>();

    public AlimentsAvecValeursNutritionelles() {

    }

    public List<String> getIntitules() {
        return intitules;
    }

    public List<String> getAliments() {
        return aliments;
    }

    public List<List<String>> getValeurs() {
        return valeurs;
    }
}
