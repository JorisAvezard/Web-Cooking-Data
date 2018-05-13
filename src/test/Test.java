package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Test {
	
	public static String lowerCaseString(String value) {
		char[] array = value.toCharArray();
		int i = 0;
		
		for(i=0; i<array.length;i++){
			array[i] = Character.toLowerCase(array[i]);
		}

		return new String(array);
	}

	public static void readFile(String fileName) {

		String line = null;
		int i = 0;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				if(i==0){
					int j = 0;
					line = line.replace(' ', '_');
					String[] line_split = line.split(";");
					for (j=0;j<line_split.length;j++){
						System.out.println("Indice "+j+" : "+ line_split[j]);
					}
				}
				i++;
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void readFile2(String fileName) {

		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),""));

			String str;

			while ((str = in.readLine()) != null) {
				System.out.println(str);
			}

			in.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		readFile("./fichiers_test/aliment_attributes.csv");
	}

}
