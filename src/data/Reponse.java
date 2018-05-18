package data;

public class Reponse {
	
	public String data;
	
	public Reponse(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Data [data=" + data + "]";
	}
	
	public String getData() {
		return data;
	}
	
	

}
