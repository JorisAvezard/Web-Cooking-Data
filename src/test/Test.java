package test;

import data.Recette;

public class Test {
	
	public static int arrondirDizaine(int entry){
		int result = 0;
		result = (int) (Math.floor(entry / 10) * 10);

		return result;
	}
	
	public static int arrondirCentaine(int entry){
		int result = 0;
		result = (int) (Math.floor(entry / 500) * 500);

		return result;
	}

	public static void main(String[] args) {
		Recette rec = new Recette();
		String line = "Aiguillette de-poulet sauce douce-image.txt";
		String process = rec.getProcessFile(line);
		System.out.println(process);
	}

}
