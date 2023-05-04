package biblioteka;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ManagerGUI {
	public static void main(String[] args) {
		// Kreiranje glavnog prozora aplikacije "Biblioteka, by Dejan Bajović"
		JFrame frame = new JFrame("Biblioteka, by Dejan Bajović");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(450, 500);

		// Centriranje prozora na ekranu
		frame.setLocationRelativeTo(null);

		// Postavljanje BorderLayout za glavni kontejner
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());

		// Dodavanje logotipa na vrh
		try {
			BufferedImage logo = ImageIO.read(new File("libLogo.png"));
			JLabel logoLabel = new JLabel(new ImageIcon(logo));
			logoLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add an empty border around logoLabel
			pane.add(logoLabel, BorderLayout.NORTH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Kreiranje JPanel-a sa BoxLayout-om za elemente forme
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		pane.add(formPanel, BorderLayout.CENTER);

		// Dodavanje elemenata forme u formPanel
		HashMap<String, Integer> bibliotekeMap = new HashMap<>();
		JComboBox<String> bibliotekeComboBox = new JComboBox<>();

		// Dodavanje podrazumevane opcije u JComboBox
		bibliotekeComboBox.addItem("Dodaj Biblioteku");

		// Unos podataka u bibliotekeComboBox
		CRUDoperacije.popuniBibliotekeComboBox(bibliotekeMap, bibliotekeComboBox);

		// Dodavanje JComboBox-a na formPanel
		formPanel.add(bibliotekeComboBox);

		// Dodavanje JLabel-a i JTextField-a za unos PIB-a
		formPanel.add(new JLabel("PIB:"));
		JTextField poljePIB = new JTextField();
		poljePIB.setHorizontalAlignment(JTextField.CENTER);
		formPanel.add(poljePIB);

		// Dodavanje JLabel-a i JTextField-a za unos Naziva
		formPanel.add(new JLabel("Naziv:"));
		JTextField poljeNaziv = new JTextField();
		poljeNaziv.setHorizontalAlignment(JTextField.CENTER);
		formPanel.add(poljeNaziv);

		// Dodavanje JLabel-a i JTextField-a za unos Ulice
		formPanel.add(new JLabel("Ulica:"));
		JTextField poljeUlica = new JTextField();
		poljeUlica.setHorizontalAlignment(JTextField.CENTER);
		formPanel.add(poljeUlica);

		// Dodavanje JLabel-a i JTextField-a za unos Broja
		formPanel.add(new JLabel("Broj:"));
		JTextField poljeBroj = new JTextField();
		poljeBroj.setHorizontalAlignment(JTextField.CENTER);
		formPanel.add(poljeBroj);

		// Dodavanje JLabel-a i JTextField-a za unos Mesta
		formPanel.add(new JLabel("Mesto:"));
		JTextField poljeMesto = new JTextField();
		poljeMesto.setHorizontalAlignment(JTextField.CENTER);
		formPanel.add(poljeMesto);

		// Dodavanje JLabel-a i JTextField-a za unos Telefona
		formPanel.add(new JLabel("Telefon:"));
		JTextField poljeTelefon = new JTextField();
		poljeTelefon.setHorizontalAlignment(JTextField.CENTER);
		formPanel.add(poljeTelefon);

		// Kreiranje JPanel-a sa FlowLayout za dugmice
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		pane.add(buttonPanel, BorderLayout.SOUTH);

		// Dodavanje dugmica u buttonPanel
		JButton dodajButton = new JButton("Dodaj");
		JButton izmeniButton = new JButton("Izmeni");
		JButton obrisiButton = new JButton("Obriši");

		buttonPanel.add(dodajButton);
		buttonPanel.add(izmeniButton);
		buttonPanel.add(obrisiButton);

		////////////////////////////
		// Učitavanje BIBLIOTEKE //
		//////////////////////////
		bibliotekeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CRUDoperacije.ucitajBiblioteku(bibliotekeMap, bibliotekeComboBox, poljePIB, poljeNaziv, poljeUlica,
						poljeBroj, poljeMesto, poljeTelefon);

			}

		});

		//////////////////
		// DODAJ DUGME //
		/////////////////
		dodajButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CRUDoperacije.addActionListenerDodajButton(frame, poljePIB, poljeNaziv, poljeUlica, poljeBroj,
						poljeMesto, poljeTelefon, bibliotekeMap, bibliotekeComboBox);

			}
		});

		///////////////////
		// IZMENI DUGME //
		//////////////////
		izmeniButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CRUDoperacije.addActionListenerIzmeniButton(frame, poljePIB, poljeNaziv, poljeUlica, poljeBroj,
						poljeMesto, poljeTelefon, bibliotekeMap, bibliotekeComboBox);
			}
		});

		///////////////////
		// OBRIŠI DUGME //
		//////////////////
		obrisiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CRUDoperacije.addActionListenerObrisiButton(frame, bibliotekeMap, bibliotekeComboBox);
			}
		});

		// Prikaz programa
		frame.setVisible(true);
	}
}