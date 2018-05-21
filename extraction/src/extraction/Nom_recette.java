package extraction;

//cette class génére le fichier "nom_recettes".il contient les noms des recette à partir du quel on va 
//prendre des valeurs aléatoire pour les recette consulté/aimé


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Nom_recette {
	
	public static ArrayList<String> getPageParLettre(Document doc) {

		 ArrayList<String> lienLetrre= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("letters-list");
			Elements rows=logo.select("li");
			 lienLetrre.add("http://www.marmiton.org/recettes/index/ingredient") ;
			for(int i=0;i<rows.size();i++){
				 Element recette=logo.select("a").get(i);
				 String relHref = recette.attr("href");  
		//	String liens=rows.get(i).text();
			lienLetrre.add("http://www.marmiton.org"+relHref );
		}
			for (int i = 0; i < lienLetrre.size(); i++) {
			//System.out.println(lienLetrre.get(i)+"\n");
			}
		return lienLetrre;
	}
	public static ArrayList<String> getLienRecette(Document doc) {

		 ArrayList<String> lienLetrre= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("recipe-results fix-inline-block");
			
			//	Elements logo1 = doc.getElementsByClass("index-item-card");
				Elements rows=logo.select("a");
				 
				for(int i=0;i<rows.size();i++){
					 Element recette=logo.select("a").get(i);
					 String relHref0= recette.attr("href");  
					 String relHref = fileName(relHref0);  
			//	String liens=rows.get(i).text();
				lienLetrre.add(relHref );
			}
				for (int i = 0; i < lienLetrre.size(); i++) {
				System.out.println(lienLetrre.get(i)+"\n");
				 nom_recette_csv(lienLetrre.get(i));
				}
			return lienLetrre;
		}
	
	public static ArrayList<String> getRecetteIngredients(Document doc) {

		
		 ArrayList<String> lienLetrre= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("index-item-card");
		//	Elements logo1 = doc.getElementsByClass("index-item-card");
			Elements rows=logo.select("a");
			 
			for(int i=0;i<rows.size();i++){
				 Element recette=logo.select("a").get(i);
				 String relHref = recette.attr("href");  
		//	String liens=rows.get(i).text();
			lienLetrre.add("http://www.marmiton.org"+relHref );
		}
			for (int i = 0; i < lienLetrre.size(); i++) {
				//System.out.println(lienLetrre.get(i)+"\n");
			}
		return lienLetrre;
	}

	
	public static ArrayList<String> getPageSuivante(Document doc) {

		 ArrayList<String> lienLetrre= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("af-pagination");
			Elements rows=logo.select("li");
			 
			for(int i=0;i<rows.size();i++){
				 Element recette=logo.select("a").get(i);
				 String relHref = recette.attr("href");  
		//	String liens=rows.get(i).text();
			lienLetrre.add("http://www.marmiton.org"+relHref );
		}
			for (int i = 0; i < lienLetrre.size(); i++) {
				//System.out.println(lienLetrre.get(i)+"\n");
			}
			return lienLetrre;
			}

	public static String get_title(Document doc){
		Elements logo = doc.getElementsByClass("main-title ");
		String titre=logo.text();
		return titre;
	}
	
	public static String fileName(String liste){
		 String titre0=liste.replaceAll("/"," ");
		 String titre1=titre0.replaceAll(".aspx"," ");
		 String titre=titre1.replaceAll("recettes "," ");
		 String[] parts = titre.split(" ");
		 String part ="";
		 for (int i=0;i<parts.length;i++){
			 if (part.equals("")){
				 part =parts[i];  
			 }else {
		  part = part+" "+parts[i]; 
		
		 }
		// System.out.println("le resultat final----------------: "+part);
		 
	}
		return part;
		}
	
	
	public static void nom_recette_csv(String string ){
	String fileName="C:/Users/nassima/Desktop/fichier_extraction/nom_recettes.csv";
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

	public static void main(String[] args) throws IOException {
		 System.out.println("blaaa\n");
		 Document doc_index =Jsoup.connect("http://www.marmiton.org/recettes/index/ingredient").get();
		// System.out.println(doc_index);
		 
		 ArrayList<String> lienLetrre= new ArrayList<String>();
		 ArrayList<String> lienIngredient= new ArrayList<String>();
		 ArrayList<String> pageSuivante= new ArrayList<String>();
		 ArrayList<String> lienRecette= new ArrayList<String>();
		 ArrayList<String> lienRecettes= new ArrayList<String>();
		 Document doc_ingredient=null;
		 Document doc_recette=null;
		 Document doc=null;
		 lienLetrre=getPageParLettre(doc_index);
		  
		 for (int i = 0; i < lienLetrre.size(); i++) {
			try{
			 doc_ingredient=Jsoup.connect(lienLetrre.get(i)).get();
	
			 lienIngredient=getRecetteIngredients(doc_ingredient);
		
				}catch(Exception e) {}
			 for (int j= 0; j < lienIngredient.size(); j++) {
				try{
				 doc_recette=Jsoup.connect(lienIngredient.get(j)).get();
				 pageSuivante=getPageSuivante(doc_ingredient);
			   	lienRecette=getLienRecette(doc_recette);
			   	//lienRecettes=fileName(lienRecette);
				 System.out.println(lienRecettes.get(j)+"\n");
				}catch(Exception e) {}
	}
		
		 }
	}}
	