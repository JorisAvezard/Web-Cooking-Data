package data;

import engine.Engine;

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
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class Aliment {

	public Aliment() {

	}
	
	public List<String> getAlimentFicheNutritionnelle(Repository repo, String aliment) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?enKJ ?enCal ?pro ?glu ?lip ?suc ?cho ?fer ?vE ?vC ?vB1 ?vB2 ?vB3 ?vB5 ?vB6 \n";
			queryString += "WHERE { \n";
			queryString += "    ?aliment_resource wcd:a_pour_energie_kJ\\/100g ?enKJ. \n";
			queryString += "    ?aliment_resource wcd:a_pour_energie_kcal\\/100g ?enCal. \n";
			queryString += "    ?aliment_resource wcd:a_pour_proteines_g\\/100g ?pro. \n";
			queryString += "    ?aliment_resource wcd:a_pour_glucides_g\\/100g ?glu. \n";
			queryString += "    ?aliment_resource wcd:a_pour_lipides_g\\/100g ?lip. \n";
			queryString += "    ?aliment_resource wcd:a_pour_sucres_g\\/100g ?suc. \n";
			queryString += "    ?aliment_resource wcd:a_pour_cholesterol_mg\\/100g ?cho. \n";
			queryString += "    ?aliment_resource wcd:a_pour_fer_mg\\/100g ?fer. \n";
//			queryString += "    ?aliment_resource wcd:a_pour_vitamine_D_�g\\/100g ?vD. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_E_mg\\/100g ?vE. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_C_mg\\/100g ?vC. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B1_mg\\/100g ?vB1. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B2_mg\\/100g ?vB2. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B3_mg\\/100g ?vB3. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B5_mg\\/100g ?vB5. \n";
			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B6_mg\\/100g ?vB6. \n";
//			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B9_�g\\/100g ?vB9. \n";
//			queryString += "    ?aliment_resource wcd:a_pour_vitamine_B12_�g\\/100g ?vB12. \n";
			queryString += "    ?aliment_resource foaf:name \"" + aliment + "\". \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("enKJ").stringValue());
					liste.add(solution.getValue("enCal").stringValue());
					liste.add(solution.getValue("pro").stringValue());
					liste.add(solution.getValue("glu").stringValue());
					liste.add(solution.getValue("lip").stringValue());
					liste.add(solution.getValue("suc").stringValue());
					liste.add(solution.getValue("cho").stringValue());
					liste.add(solution.getValue("fer").stringValue());
//					liste.add(solution.getValue("vD").stringValue());
					liste.add(solution.getValue("vE").stringValue());
					liste.add(solution.getValue("vC").stringValue());
					liste.add(solution.getValue("vB1").stringValue());
					liste.add(solution.getValue("vB2").stringValue());
					liste.add(solution.getValue("vB3").stringValue());
					liste.add(solution.getValue("vB5").stringValue());
					liste.add(solution.getValue("vB6").stringValue());
//					liste.add(solution.getValue("vB9").stringValue());
//					liste.add(solution.getValue("vB12").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}
		return liste;
	}

	// Insere les donnees de type Aliment dans la base depuis le fichier csv
	public static void addAll(Repository repo, ValueFactory vf, Model model, String wcd, String fileName) {
		repo.initialize();
		Engine engine = new Engine();

		IRI aliment_type_litteral = vf.createIRI(wcd, "Aliment");
		IRI property_energy_j = vf.createIRI(wcd, "a_pour_energie_kJ/100g");
		IRI property_energy_cal = vf.createIRI(wcd, "a_pour_energie_kcal/100g");
		IRI property_proteine = vf.createIRI(wcd, "a_pour_proteines_g/100g");
		IRI property_glucide = vf.createIRI(wcd, "a_pour_glucides_g/100g");
		IRI property_lipide = vf.createIRI(wcd, "a_pour_lipides_g/100g");
		IRI property_sucre = vf.createIRI(wcd, "a_pour_sucres_g/100g");
		IRI property_cholesterol = vf.createIRI(wcd, "a_pour_cholesterol_mg/100g");
		IRI property_fer = vf.createIRI(wcd, "a_pour_fer_mg/100g");
		IRI property_vitamineD = vf.createIRI(wcd, "a_pour_vitamine_D_µg/100g");
		IRI property_vitamineE = vf.createIRI(wcd, "a_pour_vitamine_E_mg/100g");
		IRI property_vitamineC = vf.createIRI(wcd, "a_pour_vitamine_C_mg/100g");
		IRI property_vitamineB1 = vf.createIRI(wcd, "a_pour_vitamine_B1_mg/100g");
		IRI property_vitamineB2 = vf.createIRI(wcd, "a_pour_vitamine_B2_mg/100g");
		IRI property_vitamineB3 = vf.createIRI(wcd, "a_pour_vitamine_B3_mg/100g");
		IRI property_vitamineB5 = vf.createIRI(wcd, "a_pour_vitamine_B5_mg/100g");
		IRI property_vitamineB6 = vf.createIRI(wcd, "a_pour_vitamine_B6_mg/100g");
		IRI property_vitamineB9 = vf.createIRI(wcd, "a_pour_vitamine_B9_µg/100g");
		IRI property_vitamineB12 = vf.createIRI(wcd, "a_pour_vitamine_B12_µg/100g");

		String line = null;
		try {
			// ./fichiers_test/donnees_nutritionnelles.csv
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {

				String[] line_split = line.split(";");
				String resource = engine.formatCaseResource(line_split[0]);
				System.out.println(line_split[0]);
				IRI aliment_resource = vf.createIRI(wcd, resource);

				model.add(aliment_resource, RDF.TYPE, aliment_type_litteral);

				String aliment_litteral = engine.formatCaseLitteral(line_split[0]);
				model.add(aliment_resource, FOAF.NAME, vf.createLiteral(aliment_litteral));
				model.add(aliment_resource, property_energy_j, vf.createLiteral(line_split[1]));
				model.add(aliment_resource, property_energy_cal, vf.createLiteral(line_split[2]));
				model.add(aliment_resource, property_proteine, vf.createLiteral(line_split[3]));
				model.add(aliment_resource, property_glucide, vf.createLiteral(line_split[4]));
				model.add(aliment_resource, property_lipide, vf.createLiteral(line_split[5]));
				model.add(aliment_resource, property_sucre, vf.createLiteral(line_split[6]));
				model.add(aliment_resource, property_cholesterol, vf.createLiteral(line_split[7]));
				model.add(aliment_resource, property_fer, vf.createLiteral(line_split[8]));
				model.add(aliment_resource, property_vitamineD, vf.createLiteral(line_split[9]));
				model.add(aliment_resource, property_vitamineE, vf.createLiteral(line_split[10]));
				model.add(aliment_resource, property_vitamineC, vf.createLiteral(line_split[11]));
				model.add(aliment_resource, property_vitamineB1, vf.createLiteral(line_split[12]));
				model.add(aliment_resource, property_vitamineB2, vf.createLiteral(line_split[13]));
				model.add(aliment_resource, property_vitamineB3, vf.createLiteral(line_split[14]));
				model.add(aliment_resource, property_vitamineB5, vf.createLiteral(line_split[15]));
				model.add(aliment_resource, property_vitamineB6, vf.createLiteral(line_split[16]));
				model.add(aliment_resource, property_vitamineB9, vf.createLiteral(line_split[17]));
				model.add(aliment_resource, property_vitamineB12, vf.createLiteral(line_split[18]));

			}
			System.out.println("Success");
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

	// retourne les aliments contenus dans la base
	public List<String> getAll(Repository repo) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?aliment \n";
			queryString += "WHERE { \n";
			queryString += "    ?aliment_iri rdf:type wcd:Aliment. \n";
			queryString += "    ?aliment_iri foaf:name ?aliment. \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("aliment").stringValue());
				}
			}
		} finally {
			repo.shutDown();
		}

		return liste;

	}
	
	public List<String> getAlimentsWithKeyWordForExtraction(Repository repo, String key) {
		List<String> liste = new ArrayList<String>();
		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?aliment_name \n";
			queryString += "WHERE { \n";
			queryString += "    ?aliment_resource rdf:type wcd:Aliment. \n";
			queryString += "    ?aliment_resource foaf:name ?aliment_name. \n";
			queryString += "   FILTER regex(?aliment_name, \"" + key + "\", \"i\") \n";
			queryString += "}";
			TupleQuery query = conn.prepareTupleQuery(queryString);
			try (TupleQueryResult result = query.evaluate()) {
				while (result.hasNext()) {
					BindingSet solution = result.next();
					liste.add(solution.getValue("aliment_name").stringValue());
				}
			}
		}
		return liste;
	}

	public List<String> getAlimentsWithKeyWord(Repository repo, String key) {
		repo.initialize();
		List<String> liste = new ArrayList<String>();
		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = "PREFIX wcd: <http://m2bigcookingdata.org/> \n";
			queryString += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
			queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
			queryString += "SELECT ?aliment_name \n";
			queryString += "WHERE { \n";
			queryString += "    ?aliment_resource rdf:type wcd:Aliment. \n";
			queryString += "    ?aliment_resource foaf:name ?aliment_name. \n";
			queryString += "   FILTER regex(?aliment_name, \"" + key + "\", \"i\") \n";
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
}
