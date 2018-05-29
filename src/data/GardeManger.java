package data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.repository.Repository;

public class GardeManger {

	public List<String> contenu = new ArrayList<String>();
	User user = new User();
	Repository repo;
	
	public GardeManger(Repository repo, String login) {
		contenu = user.getAlimentsFromGardeManger(repo, login);
	}

	@Override
	public String toString() {
		return "GardeManger [contenu=" + contenu + "]";
	}

	public List<String> getContenu() {
		return contenu;
	}
	
}
