package data;

import java.util.ArrayList;
import java.util.List;

public class AlimentWS {
	
	List<String> aliments = new ArrayList<String>();
	
	public AlimentWS(List<String> aliments) {
		this.aliments = aliments;
	}

	public List<String> getAliments() {
		return aliments;
	}
	
	

}
