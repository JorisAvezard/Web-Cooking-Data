package data;

import java.util.ArrayList;
import java.util.List;

public class AlimentsAvecValeursNutritionelles {
	
	List<String> intitules = new ArrayList<String>();
	List<String> aliments = new ArrayList<String>();
	List<List<String>> valeurs = new ArrayList<List<String>>();
	
	public AlimentsAvecValeursNutritionelles(List<String> aliments, List<List<String>> valeurs) {
		this.aliments = aliments;
		this.valeurs = valeurs;
		intitules.add("Energie (kJ/100g)");
		intitules.add("Energie (kcal/100g)");
		intitules.add("Proteines (g/100g)");
		intitules.add("Glucides (g/100g)");
		intitules.add("Lipides (g/100g)");
		intitules.add("Sucres (g/100g)");
		intitules.add("Cholesterol (mg/100g)");
		intitules.add("Fer (mg/100g)");
		intitules.add("Vit.E (mg/100g)");
		intitules.add("Vit.C (mg/100g)");
		intitules.add("Vit.B1 (mg/100g)");
		intitules.add("Vit.B2 (mg/100g)");
		intitules.add("Vit.B3 (mg/100g)");
		intitules.add("Vit.B5 (mg/100g)");
		intitules.add("Vit.B6 (mg/100g)");
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
