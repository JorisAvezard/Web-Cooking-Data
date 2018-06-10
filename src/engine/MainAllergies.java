package engine;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainAllergies extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel flow_panel = new JPanel();
	private JPanel flow_panel_2 = new JPanel();
	private JPanel flow_panel_3 = new JPanel();
	private JCheckBox checkbox1 = new JCheckBox("Fruits de mer");
	private JCheckBox checkbox2 = new JCheckBox("Arachide");
	private JCheckBox checkbox3 = new JCheckBox("Blé");
	private JCheckBox checkbox4 = new JCheckBox("Lactose");
	private JCheckBox checkbox5 = new JCheckBox("Moutarde");
	private JCheckBox checkbox6 = new JCheckBox("Noix");
	private JCheckBox checkbox7 = new JCheckBox("Oeuf");
	private JCheckBox checkbox8 = new JCheckBox("Soja");
	private JCheckBox checkbox9 = new JCheckBox("Grammes");
	private JCheckBox checkbox10 = new JCheckBox("mL");
	private JCheckBox checkbox11 = new JCheckBox("Entités");
	private JLabel label = new JLabel("...");
	private JButton button = new JButton("Démarrer");
	
	private FlowLayout flowLayout = new FlowLayout();
	private FlowLayout flowLayout_2 = new FlowLayout();
	private FlowLayout flowLayout_3 = new FlowLayout();
	private GridLayout gridLayout = new GridLayout(3,0);
	
	public String line = null;
	public String fileName = "./fichiers_test/aliments/donnees_nutritionnelles.csv";
	public int index = -1;
	public List<String> aliments = new ArrayList<String>();
	
	public MainAllergies() {
	
		this.setTitle("Allergies");
		this.setSize(350, 350);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		flow_panel.setLayout(flowLayout);
		flow_panel.add(checkbox1);
		flow_panel.add(checkbox2);
		flow_panel.add(checkbox3);
		flow_panel.add(checkbox4);
		flow_panel.add(checkbox5);
		flow_panel.add(checkbox6);
		flow_panel.add(checkbox7);
		flow_panel.add(checkbox8);
		flow_panel.add(checkbox9);
		flow_panel.add(checkbox10);
		flow_panel.add(checkbox11);
		
		flow_panel_2.setLayout(flowLayout_2);
		flow_panel_2.add(label);
		
		flow_panel_3.setLayout(flowLayout_3);
		flow_panel_3.add(button);
		
		this.setLayout(gridLayout);
		this.getContentPane().add(flow_panel);
		this.getContentPane().add(flow_panel_2);
		this.getContentPane().add(flow_panel_3);
		
		initActions();
		
		//////////////////////////////////////////////////////////
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
	
			while ((line = bufferedReader.readLine()) != null) {
				String[] fields = line.split(";");
				if(fields.length > 0)
					aliments.add(fields[0].trim());
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initActions() {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(index >= aliments.size()) {
					label.setText("Liste complétée");
					button.setEnabled(false);
				} else if(index != -1) {
					if(checkbox1.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_fruits_de_mer.txt", aliments.get(index));
						checkbox1.setSelected(false);
					}
					if(checkbox2.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_arachide.txt", aliments.get(index));
						checkbox2.setSelected(false);
					}
					if(checkbox3.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_blé.txt", aliments.get(index));
						checkbox3.setSelected(false);
					}
					if(checkbox4.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_lait.txt", aliments.get(index));
						checkbox4.setSelected(false);
					}
					if(checkbox5.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_moutarde.txt", aliments.get(index));
						checkbox5.setSelected(false);
					}
					if(checkbox6.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_noix.txt", aliments.get(index));
						checkbox6.setSelected(false);
					}
					if(checkbox7.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_oeuf.txt", aliments.get(index));
						checkbox7.setSelected(false);
					}
					if(checkbox8.isSelected()) {
						writeFile("./fichiers_test/aliments/allergie_soja.txt", aliments.get(index));
						checkbox8.setSelected(false);
					}
					if(checkbox9.isSelected()) {
						writeFileWithMeasurement("./fichiers_test/aliments/mesures.txt", aliments.get(index), "g");
						checkbox9.setSelected(false);
						
					}
					if(checkbox10.isSelected()) {
						writeFileWithMeasurement("./fichiers_test/aliments/mesures.txt", aliments.get(index), "mL");
						checkbox10.setSelected(false);
					}
					if(checkbox11.isSelected()) {
						writeFileWithMeasurement("./fichiers_test/aliments/mesures.txt", aliments.get(index), "entites");
						checkbox11.setSelected(false);
					}
					index++;
					label.setText(aliments.get(index));
				} else {
					button.setText("Valider");
					index++;
					label.setText(aliments.get(index));
				}
			}
		});
	}
	
	public void writeFile(String fileName, String expr) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
	
			bufferedWriter.write(expr);
			bufferedWriter.newLine();
		
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFileWithMeasurement(String fileName, String expr, String measurement) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
	
			bufferedWriter.write(expr + ";" + measurement);
			bufferedWriter.newLine();
		
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MainAllergies();
	}
}
