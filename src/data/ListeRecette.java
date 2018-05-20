package data;

import java.util.ArrayList;
import java.util.List;

public class ListeRecette {
	
	public List<String> recettes = new ArrayList<String>();
	
	public ListeRecette() {
		
	}
	
	public void addRecette(String recette) {
		if(!recettes.contains(recette))
			recettes.add(recette);
	}

	public List<String> getRecettes() {
		return recettes;
	}

	public void setRecettes(List<String> recettes) {
		this.recettes = recettes;
	}
}
