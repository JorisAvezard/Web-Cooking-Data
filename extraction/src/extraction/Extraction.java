package extraction;

//cette class sert a extraire  les donnes du site marmiton


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Extraction {
	
	//cette methode renvoie une liste qui contient les etapes de la recette 
	
	public static ArrayList<String> getEtapes(Document doc) {
		ArrayList<String> etapes= new ArrayList<String>();
		Elements logo = doc.getElementsByTag("ol");
		 Elements rows=logo.get(0).select("li");
		 
		 for(int i=0;i<rows.size();i++){
			 Elements etape=rows.get(i).getElementsByTag("li");
			 String val=etape.text();
			 etapes.add(val);
		 }
		for (int i = 0; i < etapes.size(); i++) {
			System.out.println(etapes.get(i)+"\n");
		}
		return etapes;
	}
	
	public static ArrayList<String> getLienRecette(Document doc) {

		 ArrayList<String> lienLetrre= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("recipe-results fix-inline-block");
			
			//	Elements logo1 = doc.getElementsByClass("index-item-card");
				Elements rows=logo.select("a");
				 
				for(int i=0;i<rows.size();i++){
					 Element recette=logo.select("a").get(i);
					 String relHref = recette.attr("href");  
			//	String liens=rows.get(i).text();
				lienLetrre.add("http://www.marmiton.org"+relHref );
			}
				for (int i = 0; i < lienLetrre.size(); i++) {
					System.out.println(lienLetrre.get(i)+"\n");
				}
			return lienLetrre;
		}
	//sur la page index,cette methode va renvoyer la page suivante dans la selection ingredients 

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
				System.out.println(lienLetrre.get(i)+"\n");
			}
			return lienLetrre;
			}
			

	
	public static ArrayList<String> getIngredients(Document doc) {

		 ArrayList<String> ingredients= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("recipe-ingredients__list");
			Elements rows=logo.select("li");
			
			for(int i=0;i<rows.size();i++){
			
			String ingredient=rows.get(i).text();
			ingredients.add(ingredient);
		}
			for (int i = 0; i < ingredients.size(); i++) {
				System.out.println(ingredients.get(i)+"\n");
			}
		return ingredients;
	}
	public static ArrayList<String> getUstensiles(Document doc) {

		 ArrayList<String> utensils= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("recipe-tabs__tab-content recipe-utensils-tab");
			Elements rows=logo.select("li");
			
			for(int i=0;i<rows.size();i++){
			
			String ingredient=rows.get(i).text();
			utensils.add(ingredient);
		}
			for (int i = 0; i < utensils.size(); i++) {
				System.out.println(utensils.get(i)+"\n");
			}
		return utensils;
	}
	
	
	public static ArrayList<String> getCategorie(Document doc) {

		 ArrayList<String> types= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("mrtn-tags-list");
			Elements rows=logo.select("li");
			
			for(int i=0;i<rows.size();i++){
			
			String type=rows.get(i).text();
			types.add(type);
		}
			for (int i = 0; i < types.size(); i++) {
				System.out.println(types.get(i)+"\n");
			}
		return types;
	}
	
	
	public static String getNbPersonne(Document doc) {
		 String nb = null;
			Elements logo = doc.getElementsByClass("recipe-ingredients__qt-counter__value title-5");
			//System.out.println(logo+"\n");
			 nb=logo.select("input").attr("value");
			System.out.println("nombre de personne: "+nb+"\n");
		return nb;
	}
	
	public static String getTempsPreparation(Document doc) {
		Elements logo = doc.getElementsByClass("recipe-infos__timmings__preparation");
		String tempsPreparation=logo.text();
		System.out.println(tempsPreparation+"\n");
		return tempsPreparation;
	}
	public static String getTempsCuisson(Document doc) {
		Elements logo2 = doc.getElementsByClass("recipe-infos__timmings__cooking");
		String tempsCuisson=logo2.text();
		System.out.println(tempsCuisson+"\n");
		return tempsCuisson;
	}
	public static String getTempsTotal(Document doc) {
		Elements logo = doc.getElementsByClass("title-2 recipe-infos__total-time__value");
		String tempsTotal=logo.text();
		System.out.println(tempsTotal+"\n");
		return tempsTotal;
	}
	public static String getAuteur(Document doc) {
		Elements logo = doc.getElementsByClass("recipe-author__name");
		String auteur=logo.text();
		System.out.println(auteur+"\n");
		return auteur;
	}
	
	public static String getdifficulte(Document doc) {
		 String difficulte = null;
		 Elements logo = doc.getElementsByClass("recipe-infos__level");
		 Elements diff=logo.get(0).getElementsByTag("span");
		 difficulte=diff.text();
	      System.out.println(difficulte+"\n");
		 return difficulte;
		 
	}
	
	
	public static String getBudget(Document doc) {
		 String cout = null;
		 Elements logo = doc.getElementsByClass("recipe-infos__budget");
		 Elements budget=logo.get(0).getElementsByTag("span");
		 cout=budget.text();
	      System.out.println(cout+"\n");
		 return cout;
		 
	}
	
	public static String getRecettePrecedente(Document doc) {
		 Element logo = doc.getElementById("content");
		 Element recette=logo.select("a").first();
		 String relHref = recette.attr("href");  
		 
		 String lien="http://www.marmiton.org"+relHref;
		System.out.println("RECETTE PRECEDENTE : "+lien+"\n");
		return lien;
	}
	public static String getRecetteSuivante(Document doc) {
		 Element logo = doc.getElementById("content");
		 Element recette=logo.select("a").get(1);
		 String relHref = recette.attr("href");  
		 
		 String lien="http://www.marmiton.org"+relHref;
		System.out.println("RECETTE suivante : "+lien+"\n");
		return lien;
	}
	
	public static String getAutreRecette(Document doc) {
		 Elements logo = doc.getElementsByClass("recipe-brand-natif__content");
		 Element recette=logo.select("a").first();
		 String relHref = recette.attr("href");  
		 
		 String lien="http://www.marmiton.org"+relHref;
		System.out.println("AUTRE RECETTE : "+lien+"\n");
		return lien;
	}
	
	public static String get_title(Document doc){
		Elements logo = doc.getElementsByClass("main-title ");
		String titre=logo.text();
		return titre;
	}
	

	
	public static String fileName(Document doc){
	 String titre0=get_title(doc);
	 String titre=titre0.replaceAll("/"," ");
	 String[] parts = titre.split(" ");
	 String part ="";
	 for (int i=0;i<parts.length;i++){
		 if (part.equals("")){
			 part =parts[i];  
		 }else {
	  part = part+" "+parts[i]; 
	// System.out.println(part);
	 }
	 }
	 System.out.println("le resultat final: "+part+".txt");
	 return part;
	}
	
	

	
	public static void IngredientsFile(Document doc){
		String fileName= fileName(doc)+"-ingredients.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		BufferedWriter bw = null;
		FileWriter fw = null;
		ArrayList<String> ingredients= new ArrayList<String>();
		try {
            ingredients=getIngredients(doc);
			
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
            for (int i=0;i<ingredients.size();i++){
			
			bw.write(ingredients.get(i)+"\n");
            }
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
	
	
	public static void etapesFile(Document doc){
		String fileName= fileName(doc)+"-etapes.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		ArrayList<String> ingredients= new ArrayList<String>();
		try {
            ingredients=getEtapes(doc);
			String content = "This is the content to write into file\n";
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
            for (int i=0;i<ingredients.size();i++){
			
			
			bw.write(ingredients.get(i)+"\n");
            }
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
	
	
	public static void ustensilesFile(Document doc){
		String fileName= fileName(doc)+"-ustensiles.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		ArrayList<String> ingredients= new ArrayList<String>();
		try {
            ingredients=getUstensiles(doc);
			String content = "This is the content to write into file\n";
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
            for (int i=0;i<ingredients.size();i++){
			
			
			bw.write(ingredients.get(i)+"\n");
            }
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

			}}}
	
	public static void categorieFile(Document doc){
		String fileName= fileName(doc)+"-categorie.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		ArrayList<String> categories= new ArrayList<String>();
		try {
            categories=getCategorie(doc);
			String content = "This is the content to write into file\n";
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
            for (int i=0;i<categories.size();i++){
			
			
			bw.write(categories.get(i)+"\n");
            }
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

			}}}
	
	public static void CuissonFile(Document doc){
		String fileName= fileName(doc)+"-cuisson.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		String tempsCuisson= null;
		try {
			tempsCuisson= getTempsCuisson(doc);
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(tempsCuisson+"\n");
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
	
	
	public static void PreparationFile(Document doc){
		String fileName= fileName(doc)+"-preparation.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		String tempsPreparation= null;
		try {
			tempsPreparation= getTempsPreparation(doc);
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(tempsPreparation+"\n");
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
	
	public static void auteurFile(Document doc){
		String fileName= fileName(doc)+"-auteur.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		String auteur= null;
		try {
			auteur= getAuteur(doc);
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(auteur+"\n");
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
	public static void difficulteFile(Document doc){
		String fileName= fileName(doc)+"-difficulte.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		String diff= null;
		try {
			diff= getdifficulte(doc);
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(diff+"\n");
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
	
	
	
	public static void budgetFile(Document doc){
		String fileName= fileName(doc)+"-budget.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		String budget= null;
		try {
			budget= getBudget(doc);
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(budget+"\n");
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
	
	public static void TempsTotalFile(Document doc){
		String fileName= fileName(doc)+"-tempsTotal.txt";
		String path="C:/ide/workspace/recette_generer/"+fileName;
		File file = new File(path);
		BufferedWriter bw = null;
		FileWriter fw = null;
		String tempsTotal= null;
		try {
			tempsTotal= getTempsTotal(doc);
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(tempsTotal+"\n");
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
					System.out.println(lienLetrre.get(i)+"\n");
				}
			return lienLetrre;
		}
	

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
				System.out.println(lienLetrre.get(i)+"\n");
			}
		return lienLetrre;
	}
	
	

	public static ArrayList<String> getLienAlphab(Document doc) {

		 ArrayList<String> lienLetrre= new ArrayList<String>();
			Elements logo = doc.getElementsByClass("ingredient-index");
			Elements rows=logo.select("li");
			 
			for(int i=0;i<rows.size();i++){
				 Element recette=logo.select("a").get(i);
				 String relHref = recette.attr("href");  
		//	String liens=rows.get(i).text();
			lienLetrre.add(relHref );
		}
			for (int i = 0; i < lienLetrre.size(); i++) {
				System.out.println(lienLetrre.get(i)+"\n");
			}
		return lienLetrre;
	}

	
	 public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		 System.out.println("blaaa\n");
		 Document doc_index =Jsoup.connect("http://www.marmiton.org/recettes/index/ingredient").get();
		// System.out.println(doc_index);
		 
		 ArrayList<String> lienLetrre= new ArrayList<String>();
		 ArrayList<String> lienIngredient= new ArrayList<String>();
		 ArrayList<String> pageSuivante= new ArrayList<String>();
		 ArrayList<String> lienRecette= new ArrayList<String>();
		
		 Document doc_ingredient=null;
		 Document doc_recette=null;
		 Document doc=null;
		 lienLetrre=getPageParLettre(doc_index);
		  
		 for (int i = 0; i < lienLetrre.size(); i++) {
			try{
			 doc_ingredient=Jsoup.connect(lienLetrre.get(i)).get();
		//	 System.out.println(doc_ingredient);
			 lienIngredient=getRecetteIngredients(doc_ingredient);
		
				}catch(Exception e) {}
			 for (int j= 0; j < lienIngredient.size(); j++) {
				try{
				 doc_recette=Jsoup.connect(lienIngredient.get(j)).get();
				 pageSuivante=getPageSuivante(doc_ingredient);
				 lienRecette=getLienRecette(doc_recette);
				 //for (int t= 0; t <pageSuivante.size(); t++) {
				// System.out.println(pageSuivante.get(t)+"--------------------------------------\n");
			    //}
				 System.out.println(lienRecette.get(j)+"\n");
				}catch(Exception e) {}
				
					
				 for (int k= 0; k < lienRecette.size(); k++) {
				//	for (int t= 0; t <pageSuivante.size(); t++) {
					try{
				 doc=Jsoup.connect(lienRecette.get(k)).get();
				
					 PreparationFile(doc);
					 TempsTotalFile(doc);
					 IngredientsFile(doc);
					 etapesFile(doc);
					 ustensilesFile(doc);
					 auteurFile(doc);
					 difficulteFile(doc);
					 categorieFile(doc);
					 budgetFile(doc);
					CuissonFile(doc);
					getNbPersonne(doc);
					  PreparationFile(doc);
				      TempsTotalFile(doc);
				      Thread.sleep(7000);
					}catch(Exception e) { continue; }
				 }}
		 System.out.println("blabla2\n");		
	 }}
	 
}
				

//		Document doc_recette =Jsoup.connect("http://www.marmiton.org/recettes/recettes-index.aspx?ingredient=abricot").get();
//	 	  //getEtapes(doc);
//		  //getIngredients(doc);
//	 	  //getNbPersonne(doc);
//		  //getUstensiles(doc);
//		  //getTempsPreparation(doc);
//		  //getTempsCuisson(doc);
//		  //getTempsTotal(doc);
//		  //get_title(doc);
//		  //getRecettePrecedente( doc);
//		  //getRecetteSuivante(doc);
//		  //getAutreRecette( doc);
//		getPageSuivante(doc_recette);
//		
////       recette.setEtapes(etapes)=getIngredients(doc);
////		 for (int i=0;i<20;i++){
////		 CuissonFile(doc);
////	     PreparationFile(doc);
////		 TempsTotalFile(doc);
////		 IngredientsFile(doc);
////		 etapesFile(doc);
////		 ustensilesFile(doc);
////		 auteurFile(doc);
////		 difficulteFile(doc);
////		 categorieFile(doc);
////		 budgetFile(doc);
////		//CuissonFile(doc);
////		  //PreparationFile(doc);
////	      //TempsTotalFile(doc);
////		
////		 recetteSuivante=getRecetteSuivante(doc);
////		
////		 if (recetteSuivante.equals("http://www.marmiton.org/recettes/")){
////			 Autrerecette=getAutreRecette(doc);
////			 doc =Jsoup.connect(Autrerecette).get();
////		 }else{
////		 doc =Jsoup.connect(recetteSuivante).get();
////		 Thread.sleep(4000);
////	 }}
////		 
////		// getPageParLettre(doc_index);
////	//	getRecetteIngredients(doc_index);
////		//getLienRecette(doc_recette);
////		
	 

	 
	