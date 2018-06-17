package engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.repository.Repository;

import data.Aliment;

public class Extraction {

	public String enleve_parentheses(String expression) {
		int p_ouvr = -1;
		int p_ferm = -1;
		String sans_parentheses = "";
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				p_ouvr = i;
			}
			if (expression.charAt(i) == ')') {
				p_ferm = i;
			}
		}
		if (p_ouvr != -1 && p_ferm != -1) {
			for (int i = 0; i < expression.length(); i++) {
				if (i < p_ouvr || i > p_ferm) {
					sans_parentheses = sans_parentheses + expression.charAt(i);
				}
			}
		} else if (p_ouvr != -1 || p_ferm != -1) {
			if (p_ouvr != -1) {
				for (int i = 0; i < expression.length(); i++) {
					if (i != p_ouvr) {
						sans_parentheses = sans_parentheses + expression.charAt(i);
					}
				}
			}
			if (p_ferm != -1) {
				for (int i = 0; i < expression.length(); i++) {
					if (i != p_ferm) {
						sans_parentheses = sans_parentheses + expression.charAt(i);
					}
				}
			}
		} else
			sans_parentheses = expression;
		return sans_parentheses;
	}

	public void motsClesParLigne(Repository repo, String fileName) {
		String line;
		String fields[] = null;
		Aliment aliment = new Aliment();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line = enleve_parentheses(line);
				fields = line.split(" ");
				List<String> list = new ArrayList<String>();
				List<String> content = new ArrayList<String>();
				List<String> resultat = new ArrayList<String>();
				// Chocolat au lait donne Chocolat, lait
				for (int i = 0; i < fields.length; i++) {
					// Le mot doit strictement �tre superieure � 2 et sans s �
					// la fin
					if (fields[i].length() > 2) {
						int size = fields[i].length();
						if (fields[i].charAt(size - 1) == 's') {
							fields[i] = fields[i].substring(0, size - 1);
						}
						if (!list.contains(fields[i]))
							list.add(fields[i]);
					}
				}

				// for(int c=0; c<list.size(); c++) {
				// System.out.println("-> list : " + list.get(c));
				// }

				// pour chaque mot, on cherche en base ce que l'on trouve et on
				// le stocke
				for (int j = 0; j < list.size(); j++) {
					List<String> tmp = new ArrayList<String>();
					tmp = aliment.getAlimentsWithKeyWord(repo, list.get(j));
					for (int k = 0; k < tmp.size(); k++) {
						content.add(tmp.get(k));
					}

				}

				// for(int c=0; c<content.size(); c++) {
				// System.out.println("-> content : " + content.get(c));
				// }

				// On regarde la ou les lignes qui sont le plus r�p�t�s
				if (content.size() > 0) {
					int max = 0;
					int count = 0;
					for (int l = 0; l < content.size(); l++) {
						count = 1;
						String ingredient = content.get(l);
						for (int m = l + 1; m < content.size(); m++) {
							if (content.get(m).equals(ingredient)) {
								count++;
							}
						}
						if (count == max) {
							resultat.add(ingredient);
						}
						if (count > max) {
							max = count;
							resultat.clear();
							resultat.add(ingredient);
						}
					}

					String aliment_gagnant = "";
					int ind = 0;
					int size = resultat.get(0).length();
					if (resultat.size() > 1) {
						for (int y = 1; y < resultat.size(); y++) {
							if (resultat.get(y).length() < size) {
								ind = y;
								size = resultat.get(y).length();
							}
						}
					}
					aliment_gagnant = resultat.get(ind);

					// On affiche le r�sultat
					System.out.println("-> " + aliment_gagnant);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAlimentFromIngredient(Repository repo, String entry) {
//		System.out.println("entry :"+ entry);
		String aliment_gagnant = "";
		String fields[] = null;
		Aliment aliment = new Aliment();

		entry = enleve_parentheses(entry);
		fields = entry.split(" ");
		List<String> list = new ArrayList<String>();
		List<String> content = new ArrayList<String>();
		List<String> resultat = new ArrayList<String>();

		for (int i = 0; i < fields.length; i++) {
			if (fields[i].length() > 2) {
				int size = fields[i].length();
				if (fields[i].charAt(size - 1) == 's') {
					fields[i] = fields[i].substring(0, size - 1);
				}
				if (!list.contains(fields[i]))
					list.add(fields[i]);
			}
		}

		for (int j = 0; j < list.size(); j++) {
			List<String> tmp = new ArrayList<String>();
			tmp = aliment.getAlimentsWithKeyWordForExtraction(repo, list.get(j));
			for (int k = 0; k < tmp.size(); k++) {
				content.add(tmp.get(k));
			}

		}

		if (content.size() > 0) {
			int max = 0;
			int count = 0;
			for (int l = 0; l < content.size(); l++) {
				count = 1;
				String ingredient = content.get(l);
				for (int m = l + 1; m < content.size(); m++) {
					if (content.get(m).equals(ingredient)) {
						count++;
					}
				}
				if (count == max) {
					resultat.add(ingredient);
				}
				if (count > max) {
					max = count;
					resultat.clear();
					resultat.add(ingredient);
				}
			}

			int ind = 0;
			int size = resultat.get(0).length();
			if (resultat.size() > 1) {
				for (int y = 1; y < resultat.size(); y++) {
					if (resultat.get(y).length() < size) {
						ind = y;
						size = resultat.get(y).length();
					}
				}
			}
			aliment_gagnant = resultat.get(ind);
		}
		return aliment_gagnant;
	}

}