package extraction;

//cette class génére des utilisateurs pour le data mining (sous forme de 3 fichiers: profils,recette_consulte_par_users,recette_aime_par_users)
// avant de la lancer faut lancé en premier Nom_recette.java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

public class Generer_user {
	
	

    public static ArrayList<String> readFile(File file) throws IOException {

    	ArrayList<String> result = new ArrayList<String>();

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        for (String line = br.readLine(); line != null; line = br.readLine()) {
            result.add(line);
        }
        
        br.close();
        fr.close();
        for (int i = 0; i <  result.size(); i++) {
		//	System.out.println( result.get(i)+"\n");
		}
        return result;
    }
 
    
   public static  String aleatoire(ArrayList<String> result) throws IOException {

    	int indiceAuHasard = (int) (Math.random() * (result.size() - 1));
    	
        String val=result.get(indiceAuHasard);
       
		//System.out.println( "valeur aleatoire est : "+val+"\n");
		
        return val;
    }
 
    public static  int nombreAleatoire(int min,int max) throws IOException {
    	
    	int nombreAleatoire = min + (int)(Math.random() * ((max - min) + 1));
       
			//System.out.println( "valeur du poid : "+nombreAleatoire+"\n");
		
        return nombreAleatoire;
    }
    
    
    public static void ecrire_txt(String string ,String NameFile){
    	String fileName="C:/Users/nassima/Desktop/fichier_extraction/"+NameFile+".txt";
    		BufferedWriter bw = null;
    		FileWriter fw = null;
    	try {
    			fw = new FileWriter(fileName,true);
    			bw = new BufferedWriter(fw);
    			
    			bw.write(string+"\n");
               
    			System.out.println("Done");

    		} catch (IOException e) {

    			e.printStackTrace();

    		} finally {

    			try {

    				if (bw != null)
    					bw.close();

    				if (fw != null)
    					fw.close();

    			} catch (IOException ex) {

    				ex.printStackTrace();

    			}}
    	}
	
	private static ArrayList<String> generer_recette_consulter() throws IOException {
		ArrayList<String> liste_consulte = new ArrayList<String>();
		ArrayList<String> liste_aleatoire_consulte = new ArrayList<String>();
		String fileRecette="C:/Users/nassima/Desktop/fichier_extraction/nom_recettes.csv";
		File fileRecettes=new File(fileRecette);
		liste_consulte=readFile(fileRecettes);
		
		for(int i=0;i<20;i++){
		String recette_consult=aleatoire(liste_consulte);
		liste_aleatoire_consulte.add(recette_consult);
		}
		return liste_aleatoire_consulte;
	}
		


	public static void main(String[] args) throws IOException {
		String fileName_user = "profils";
		String fileName_rec_gen = "recette_consulte_par_users";
		String fileName_rec_aime = "recette_aime_par_users";
		ArrayList<String> liste_allergies = new ArrayList<String>();
		ArrayList<String> liste_maladies = new ArrayList<String>();
		//ArrayList<String> liste_recette_consulte = new ArrayList<String>();
		ArrayList<String> liste_Genres = new ArrayList<String>();
		
		String filename_allergies="C:/Users/nassima/Desktop/fichier_extraction/Allergies.csv";
		String file_maladie="C:/Users/nassima/Desktop/fichier_extraction/Maladies.csv";
		//String fileRecette="C:/Users/nassima/Desktop/fichier_extraction/nom_recettes.csv";
		String fileGenre="C:/Users/nassima/Desktop/fichier_extraction/genre.csv";
		
		File fileGenres=new File(fileGenre);
	//	File fileRecettes=new File(fileRecette);
		File fichier=new File(filename_allergies);
		File fich=new File(file_maladie);
		
		liste_Genres=readFile(fileGenres);
		liste_allergies=readFile(fichier);
		liste_maladies=readFile(fich);
	//	liste_recette_consulte=readFile(fileRecettes);
	 
		for(int i=1;i<40;i++){
		String alergie=aleatoire(liste_allergies);
		String maladie=aleatoire(liste_maladies);
		String genre=aleatoire(liste_Genres);
//		String recette_consult=aleatoire(liste_recette_consulte);
		int poid=nombreAleatoire(30,150);
		int besoin_calorique=nombreAleatoire(1000,2500);
		
		//le "i" c'est le login
		
		String user=i+";"+genre+";"+poid+";"+alergie+";"+maladie+";"+besoin_calorique;
		
		//System.out.println(user);
		ecrire_txt(user,fileName_user);
			}
		
		for(int j=1;j<40;j++){
			//sous la forme login;une recette consulter
			
			ArrayList<String> liste_recette_consulte = new ArrayList<String>();
			liste_recette_consulte=generer_recette_consulter();
			 for (int k = 0; k < liste_recette_consulte.size(); k++) {
				 String val=j+";"+liste_recette_consulte.get(k);
				 ecrire_txt(val,fileName_rec_gen);
				 
			 }
			
		}
		
		
		for(int j=1;j<20;j++){
			ArrayList<String> liste_recette_aime = new ArrayList<String>();
			liste_recette_aime=generer_recette_consulter();
			 for (int k = 0; k < liste_recette_aime.size(); k++) {
				 String val=j+";"+liste_recette_aime.get(k);
				 ecrire_txt(val,fileName_rec_aime);
				 
			 }
			
		}
		
	}

}
