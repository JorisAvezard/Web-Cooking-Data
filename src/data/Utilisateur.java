package data;

public class Utilisateur {
	
	public String login;
	public String password;
	
	public Utilisateur(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
}
