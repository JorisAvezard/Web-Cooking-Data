package data;

import engine.Engine;
import engine.Extraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.json.simple.JSONObject;

public class Recette {

	public Recette() {

	}
	
	public List<String> getAliments(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?aliment_name \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_ingredient ?ingredient_iri. \n";
			queryString += "    ?ingredient_iri rdf:type wcd:Ingredient. \n";
			queryString += "    ?ingredient_iri wcd:aliment_respectif ?aliment_resource. \n";
			queryString += "    ?aliment_resource rdf:type wcd:Aliment. \n";
			queryString += "    ?aliment_resource foaf:name ?aliment_name. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("aliment_name").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
}

	public String getNameFromFile(String fichier) {
		String result = null;
		String[] tmp = fichier.split(".txt");
		String[] tmp2 = tmp[0].split("-");

		result = tmp2[0];

		if (tmp2.length > 2) {
			for (int i = 1; i < tmp2.length - 1; i++) {
				result += "-" + tmp2[i];
			}
		}

		return result;
	}
	
	public String getProcessFile(String fichier) {
		String[] tmp = fichier.split(".txt");
		String[] tmp2 = tmp[0].split("-");

		return tmp2[tmp2.length-1];
	}

	public String getNameFromPath(String fileName) {
		String[] tmp = fileName.split("/");
		String[] tmp2 = tmp[tmp.length - 1].split(".txt");
		String[] tmp3 = tmp2[0].split("-");

		return tmp3[0];
	}

	// ajoute le nom et le type "Recette" de la recette dans la base
	public void addTypeAndName(Repository repo, ValueFactory vf, Model model, String wcd, String key) {
		repo.initialize();
		Engine engine = new Engine();
		String key_iri = engine.formatCaseResource(key);
		String rec_nom_litteral = engine.formatCaseLitteral(key);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI recette_objet = vf.createIRI(wcd, "Recette");
		model.add(recette_nom, RDF.TYPE, recette_objet);
		model.add(recette_nom, FOAF.NAME, vf.createLiteral(rec_nom_litteral));

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void addNote(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI cat_iri = vf.createIRI(wcd, "a_pour_note");

		String line = null;
		String[] line_split = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line_split = line.split(" ");
				model.add(recette_nom, cat_iri, vf.createLiteral(line_split[0]));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void addImage(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI cat_iri = vf.createIRI(wcd, "a_pour_image");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, cat_iri, vf.createLiteral(line));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void addCategorie(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI cat_iri = vf.createIRI(wcd, "a_pour_categorie");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, cat_iri, vf.createLiteral(line));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	public void addDifficulte(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI dif_iri = vf.createIRI(wcd, "a_pour_difficulte");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, dif_iri, vf.createLiteral(line));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute les ingredients d'une recette
	public void addIngredients(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		Extraction extraction = new Extraction();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_ingredient");
		IRI data_type = vf.createIRI(wcd, "Ingredient");
		IRI ing_desc = vf.createIRI(wcd, "a_pour_description");
		IRI link_ing_ali = vf.createIRI(wcd, "aliment_respectif");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace("\"", "");
				line = line.replaceAll("\\(", "");
				line = line.replaceAll("\\)", "");
				line = line.replaceAll("\\`", "");
				line = line.replaceAll("\\~", "");
				line = line.replaceAll("\\!", "");
				line = line.replaceAll("\\@", "");
				line = line.replaceAll("\\#", "");
				line = line.replaceAll("\\$", "");
				line = line.replaceAll("\\%", "");
				line = line.replaceAll("\\^", "");
				line = line.replaceAll("\\&", "");
				line = line.replaceAll("\\*", "");
				line = line.replaceAll("\\-", "");
				line = line.replaceAll("\\_", "");
				line = line.replaceAll("\\=", "");
				line = line.replaceAll("\\+", "");
				line = line.replaceAll("\\[", "");
				line = line.replaceAll("\\]", "");
				line = line.replaceAll("\\{", "");
				line = line.replaceAll("\\}", "");
				line = line.replaceAll("\\|", "");
				line = line.replaceAll("\\'", "");
				line = line.replaceAll("\\/", "");
				line = line.replaceAll("\\?", "");
				line = line.replaceAll("\\.", "");
				line = line.replaceAll("\\>", "");
				line = line.replaceAll("\\,", "");
				line = line.replaceAll("\\<", "");
				
				String ing_iri = engine.formatCaseResource(line);
				IRI ing = vf.createIRI(wcd, ing_iri);
				model.add(ing, RDF.TYPE, data_type);
				model.add(recette_nom, data_predicate, ing);
				String ing_desc_value = engine.formatCaseLitteral(line);
				model.add(ing, ing_desc, vf.createLiteral(ing_desc_value));

				String aliment_respectif = extraction.getAlimentFromIngredient(repo, line);
				// System.out.println("aliment_respectif :"+ aliment_respectif);
				if (!aliment_respectif.equals("")) {
					aliment_respectif = engine.formatCaseResource(aliment_respectif);
					IRI aliment_resource = vf.createIRI(wcd, aliment_respectif);
					model.add(ing, link_ing_ali, aliment_resource);
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute l'auteur de la recette
	public void addAuteur(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI auteur_type = vf.createIRI(wcd, "Auteur");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String aut_iri = line.replace(' ', '_');
				IRI per = vf.createIRI(wcd, aut_iri);
				model.add(per, RDF.TYPE, auteur_type);
				model.add(per, FOAF.NAME, vf.createLiteral(line));
				model.add(recette_nom, DC.CREATOR, per);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute les differentes etapes d'une recette
	public void addEtapes(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);

		String line = null;
		int ind = 1;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				IRI data_predicate = vf.createIRI(wcd, "a_pour_etape_" + Integer.toString(ind));
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
				ind++;
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute les ustensiles d'une recette
	public void addUstensiles(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_ustensile");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute le temps total d'une recette
	public void addTempsTotal(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_temps_total");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute le temps de preparation d'une recette
	public void addTempsPreparation(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_temps_preparation");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate,
						vf.createLiteral(engine.getValueFromPreparationAndCuissonRecetteFile(line)));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute le temps de cuisson d'une recette
	public void addTempsCuisson(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_temps_cuisson");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate,
						vf.createLiteral(engine.getValueFromPreparationAndCuissonRecetteFile(line)));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute le nb de personnes d'une recette
	public void addNbPersonnes(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();
		String rec_nom = getNameFromPath(fileName);
		String key_iri = engine.formatCaseResource(rec_nom);
		IRI recette_nom = vf.createIRI(wcd, key_iri);
		IRI data_predicate = vf.createIRI(wcd, "a_pour_nb_personnes");

		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				model.add(recette_nom, data_predicate, vf.createLiteral(line));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(model);
		} finally {
			repo.shutDown();
		}
	}

	// ajoute le nb de personnes d'une recette
	public void processInsertion(Repository repo, ValueFactory vf, Model model, String wcd, String path) {
		Engine engine = new Engine();
		String key = null;
		String process = null;
		int i = 0;
		File folder = new File(path);
		for (File fileEntry : folder.listFiles()) {
			String fichier = fileEntry.getName();
			if (engine.goodFile(fichier)) {
				System.out.println(i);
				key = getNameFromFile(fichier);
				process = getProcessFile(fichier);
				
				addTypeAndName(repo, vf, model, wcd, key);
				
				if(process.equals("note")){
					addNote(repo, vf, model, wcd, path + key + "-note.txt");
				}else if(process.equals("image")){
					addImage(repo, vf, model, wcd, path + key + "-image.txt");
				}else if(process.equals("categorie")){
					addCategorie(repo, vf, model, wcd, path + key + "-categorie.txt");
				}else if(process.equals("difficulte")){
					addDifficulte(repo, vf, model, wcd, path + key + "-difficulte.txt");
				}else if(process.equals("ingredients")){
					addIngredients(repo, vf, model, wcd, path + key + "-ingredients.txt");
				}else if(process.equals("auteur")){
					addAuteur(repo, vf, model, wcd, path + key + "-auteur.txt");
				}else if(process.equals("etapes")){
					addEtapes(repo, vf, model, wcd, path + key + "-etapes.txt");
				}else if(process.equals("ustensiles")){
					addUstensiles(repo, vf, model, wcd, path + key + "-ustensiles.txt");
				}else if(process.equals("tempsTotal")){
					addTempsTotal(repo, vf, model, wcd, path + key + "-tempsTotal.txt");
				}else if(process.equals("preparation")){
					addTempsPreparation(repo, vf, model, wcd, path + key + "-preparation.txt");
				}else if(process.equals("cuisson")){
					addTempsCuisson(repo, vf, model, wcd, path + key + "-cuisson.txt");
				}else if(process.equals("nbPersonne")){
					addNbPersonnes(repo, vf, model, wcd, path + key + "-nbPersonne.txt");
				}
			}
			i++;
		}
		System.out.println("End");
	}

	// retourne les ingredients etant donne une recette
	public List<String> getIngredients(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?ingredient_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "     ?recette_iri wcd:a_pour_ingredient ?ingredient_iri. \n";
			queryString += "    ?ingredient_iri wcd:a_pour_description ?ingredient_nom. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("ingredient_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public double getNote(Repository repo, String key) {
		repo.initialize();
		double resultat = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?note \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "     ?recette_iri wcd:a_pour_note ?note. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = Double.valueOf(solution.getValue("note").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return resultat;
	}

	public String getImage(Repository repo, String key) {
		repo.initialize();
		String resultat = null;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?image \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "     ?recette_iri wcd:a_pour_image ?image. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = solution.getValue("image").stringValue();
				}
			}
		} finally {
			repo.shutDown();
		}

		return resultat;
	}

	// retourne pour combien de personnes est destinee une recette
	public int getNbPersonnes(Repository repo, String key) {
		repo.initialize();
		int resultat = 0;

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?nombre \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "     ?recette_iri wcd:a_pour_nb_personnes ?nombre. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					resultat = Integer.valueOf(solution.getValue("nombre").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return resultat;
	}

	// retourne les etapes etant donne une recette
	public List<String> getEtapes(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		int indice = 1;
		int indice2 = 0;
		boolean cont = true;

		try (RepositoryConnection conn = repo.getConnection()) {
			while (cont) {
				String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
				queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
				queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
				queryString += "SELECT ?etape \n";
				queryString += "WHERE { \n";
				queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
				queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
				queryString += "    ?recette_iri wcd:a_pour_etape_" + indice + " ?etape. \n";
				queryString += "}";

				TupleQuery query = conn.prepareTupleQuery(queryString);
				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
						liste.add(solution.getValue("etape").stringValue());
						indice2++;
					}
				}
				if (indice2 == 0) {
					cont = false;
				}
				indice2 = 0;
				indice++;
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	// retourne l'auteur etant donne une recette
	public List<String> getAuteur(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "PREFIX dc: <" + DC.NAMESPACE + "> \n";
			queryString += "SELECT ?auteur \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri dc:creator ?auteur_iri. \n";
			queryString += "    ?auteur_iri foaf:name ?auteur. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("auteur").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getTempsTotal(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?temps \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_temps_total ?temps. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("temps").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getTempsCuisson(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?temps \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_temps_cuisson ?temps. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("temps").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getTempsPreparation(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?temps \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_temps_preparation ?temps. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("temps").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getUstensiles(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?ustensile \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_ustensile ?ustensile. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("ustensile").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getCategory(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?cat \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_categorie ?cat. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("cat").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getDifficulte(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?dif \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name \"" + key + "\". \n";
			queryString += "    ?recette_iri wcd:a_pour_difficulte ?dif. \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("dif").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getNamesRecettesByKeyWord(Repository repo, List<String> key_words) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();
		int i = 0;
		String key = "";

		try (RepositoryConnection conn = repo.getConnection()) {
			for (i = 0; i < key_words.size(); i++) {
				key = engine.lowerCaseAll(key_words.get(i));

				String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
				queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
				queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
				queryString += "SELECT ?recette_nom \n";
				queryString += "WHERE { \n";
				queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
				queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
				queryString += "   FILTER regex(?recette_nom, \"" + key + "\", \"i\") \n";
				queryString += "}";

				TupleQuery query = conn.prepareTupleQuery(queryString);
				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
						if (!liste.contains(solution.getValue("recette_nom").stringValue())) {
							liste.add(solution.getValue("recette_nom").stringValue());
						}
					}
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	// Entrée, plat, dessert
	// Végétarien, Sans gluten
	public List<String> getNamesRecettesByCategory(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();
		key = engine.lowerCaseAll(key);

		try (RepositoryConnection conn = repo.getConnection()) {

			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "    ?recette_iri wcd:a_pour_categorie ?cat. \n";
			queryString += "   FILTER regex(?cat, \"" + key + "\", \"i\") \n";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("recette_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getNamesRecettesByNote(Repository repo, double note) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		int indice = 0;

		try (RepositoryConnection conn = repo.getConnection()) {

			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "    ?recette_iri wcd:a_pour_note ?note. \n";
			queryString += "   FILTER (xsd:double(?note) >= " + note + ") \n";
			queryString += "}";
			queryString += "ORDER BY DESC(xsd:double(?note))";

			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					if(liste.size()<51)
						liste.add(solution.getValue("recette_nom").stringValue());
					else
						break;
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getNamesRecettesByDifficulte(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();
		key = engine.lowerCaseAll(key);

		try (RepositoryConnection conn = repo.getConnection()) {

			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_iri rdf:type wcd:Recette. \n";
			queryString += "    ?recette_iri foaf:name ?recette_nom. \n";
			queryString += "    ?recette_iri wcd:a_pour_difficulte ?dif. \n";
			queryString += "   FILTER regex(?dif, \"" + key + "\", \"i\") \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("recette_nom").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getNamesRecettesByAliments(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();
		key = engine.formatCaseResource(key);

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_resource rdf:type wcd:Recette. \n";
			queryString += "    ?recette_resource foaf:name ?recette_nom. \n";
			queryString += "    ?recette_resource wcd:a_pour_ingredient ?ingredient. \n";
			queryString += "    ?ingredient wcd:aliment_respectif wcd:" + key + ". \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					if (!liste.contains(solution.getValue("recette_nom").stringValue())) {
						liste.add(solution.getValue("recette_nom").stringValue());
					}
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}
	
	public List<String> getNamesRecettesByAllergie(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_resource rdf:type wcd:Recette. \n";
			queryString += "    ?recette_resource foaf:name ?recette_nom. \n";
			queryString += "    ?recette_resource wcd:a_pour_ingredient ?ingredient_iri. \n";
			queryString += "    ?ingredient_iri wcd:aliment_respectif ?aliment_iri. \n";
			queryString += "    ?aliment_iri wcd:a_pour_categorie_allergie ?allergie_iri. \n";
			queryString += "    ?allergie_iri foaf:name \"" + key + "\". \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					if (!liste.contains(solution.getValue("recette_nom").stringValue())) {
						liste.add(solution.getValue("recette_nom").stringValue());
					}
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

	public List<String> getAllNamesRecettes(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		Engine engine = new Engine();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?recette_nom \n";
			queryString += "WHERE { \n";
			queryString += "    ?recette_resource rdf:type wcd:Recette. \n";
			queryString += "    ?recette_resource foaf:name ?recette_nom. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					if (!liste.contains(solution.getValue("recette_nom").stringValue())) {
						liste.add(solution.getValue("recette_nom").stringValue());
					}
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;
	}

}
