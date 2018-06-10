package data;

import java.util.ArrayList;
import java.util.List;

public class GardeManger {

	public List<String> contenu = new ArrayList<String>();
	
	public GardeManger(List<String> contenu) {
		this.contenu = contenu;
	}

	@Override
	public String toString() {
		return "GardeManger [contenu=" + contenu + "]";
	}

	public List<String> getContenu() {
		return contenu;
	}
	
}
