package biblioteka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CRUDoperacije {

	////////////////////////
	// UČITAJ BIBLIOTEKU //
	//////////////////////
	public static void ucitajBiblioteku(HashMap<String, Integer> bibliotekeMap, JComboBox<String> bibliotekeComboBox,
			JTextField poljePIB, JTextField poljeNaziv, JTextField poljeUlica, JTextField poljeBroj,
			JTextField poljeMesto, JTextField poljeTelefon) {
		String selectedNaziv = (String) bibliotekeComboBox.getSelectedItem();
		if (selectedNaziv != null && bibliotekeMap.containsKey(selectedNaziv)) {
			int pib = bibliotekeMap.get(selectedNaziv);

			try {
				String url = "jdbc:mysql://localhost:3306/info_centar";
				String username = "root";
				String pass = "";

				Connection conn = DriverManager.getConnection(url, username, pass);
				String sqlSelect = "SELECT * FROM Biblioteke WHERE PIB = ?";
				PreparedStatement ps = conn.prepareStatement(sqlSelect);
				ps.setInt(1, pib);
				ResultSet resultSet = ps.executeQuery();

				if (resultSet.next()) {
					poljePIB.setText(String.valueOf(resultSet.getInt("PIB")));
					poljeNaziv.setText(resultSet.getString("Naziv"));
					poljeUlica.setText(resultSet.getString("Ulica"));
					poljeBroj.setText(resultSet.getString("Broj"));
					poljeMesto.setText(resultSet.getString("Mesto"));
					poljeTelefon.setText(resultSet.getString("Telefon"));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} else {
			poljePIB.setText("");
			poljeNaziv.setText("");
			poljeUlica.setText("");
			poljeBroj.setText("");
			poljeMesto.setText("");
			poljeTelefon.setText("");
		}
	}

	//////////////////
	// DODAJ dugme //
	/////////////////
	public static void addActionListenerDodajButton(JFrame okvir, JTextField poljePIB, JTextField poljeNaziv,
			JTextField poljeUlica, JTextField poljeBroj, JTextField poljeMesto, JTextField poljeTelefon,
			HashMap<String, Integer> mapaBiblioteka, JComboBox<String> kombinovaniOkvirBiblioteka) {
		// Dohvati podatke iz tekstualnih polja
		String pib = poljePIB.getText();
		String naziv = poljeNaziv.getText();
		String ulica = poljeUlica.getText();
		String broj = poljeBroj.getText();
		String mesto = poljeMesto.getText();
		String telefon = poljeTelefon.getText();

		// Proveri da li su sva polja popunjena
		if (pib.trim().isEmpty() || naziv.trim().isEmpty() || ulica.trim().isEmpty() || broj.trim().isEmpty()
				|| mesto.trim().isEmpty() || telefon.trim().isEmpty()) {
			JOptionPane.showMessageDialog(okvir, "Morate popuniti sva obavezna polja.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Proveri da li biblioteka sa istim PIB-om već postoji
		try {
			String url = "jdbc:mysql://localhost:3306/info_centar";
			String korisnickoIme = "root";
			String lozinka = "";

			Connection konekcija = DriverManager.getConnection(url, korisnickoIme, lozinka);
			String sqlProvera = "SELECT * FROM Biblioteke WHERE PIB = ?";
			PreparedStatement psProvera = konekcija.prepareStatement(sqlProvera);
			psProvera.setInt(1, Integer.parseInt(pib));
			ResultSet resultSet = psProvera.executeQuery();

			// Ako postoji, prikaži poruku o grešci i prekini izvršavanje metode
			if (resultSet.next()) {
				JOptionPane.showMessageDialog(okvir, "Biblioteka sa unetim PIB-om već postoji.", "Greška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Unesi novu biblioteku u bazu podataka
			String sqlUnos = "INSERT INTO Biblioteke (PIB, Naziv, Ulica, Broj, Mesto, Telefon) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement psUnos = konekcija.prepareStatement(sqlUnos);

			psUnos.setInt(1, Integer.parseInt(pib));
			psUnos.setString(2, naziv);
			psUnos.setString(3, ulica);
			psUnos.setString(4, broj);
			psUnos.setString(5, mesto);
			psUnos.setString(6, telefon);

			int uneseniRedovi = psUnos.executeUpdate();

			// Ako je biblioteka uspešno dodata, ažuriraj JComboBox i prikaži poruku
			if (uneseniRedovi > 0) {
				JOptionPane.showMessageDialog(okvir, "Biblioteka uspešno dodata", "Uspeh",
						JOptionPane.INFORMATION_MESSAGE);

				// Ažuriraj JComboBox sa novom bibliotekom
				mapaBiblioteka.put(naziv, Integer.parseInt(pib));
				kombinovaniOkvirBiblioteka.addItem(naziv);

			} else {
				JOptionPane.showMessageDialog(okvir, "Dodavanje biblioteke nije uspelo", "Greška",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException ex) {
			// U slučaju greške, obradi izuzetak
			ex.printStackTrace();
		}
	}

	//////////////////
	// IZMENI dugme //
	//////////////////

	public static void addActionListenerIzmeniButton(JFrame okvir, JTextField poljePIB, JTextField poljeNaziv,
			JTextField poljeUlica, JTextField poljeBroj, JTextField poljeMesto, JTextField poljeTelefon,
			HashMap<String, Integer> mapaBiblioteka, JComboBox<String> kombinovaniOkvirBiblioteka) {
		// Dohvati podatke iz tekstualnih polja
		String pib = poljePIB.getText();
		String naziv = poljeNaziv.getText();
		String ulica = poljeUlica.getText();
		String broj = poljeBroj.getText();
		String mesto = poljeMesto.getText();
		String telefon = poljeTelefon.getText();

		// Proveri da li je izabrana opcija podrazumevanih opcija
		String izabraniNaziv = (String) kombinovaniOkvirBiblioteka.getSelectedItem();
		if (izabraniNaziv.equals("Dodaj Biblioteku")) {
			JOptionPane.showMessageDialog(okvir, "Molimo izaberite biblioteku za ažuriranje.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int izabraniPIB = mapaBiblioteka.get(izabraniNaziv);

		// Ažuriraj izabranu biblioteku u bazi podataka
		try {
			String url = "jdbc:mysql://localhost:3306/info_centar";
			String korisnickoIme = "root";
			String lozinka = "";

			Connection konekcija = DriverManager.getConnection(url, korisnickoIme, lozinka);
			String sqlAzuriranje = "UPDATE Biblioteke SET PIB = ?, Naziv = ?, Ulica = ?, Broj = ?, Mesto = ?, Telefon = ? WHERE PIB = ?";
			PreparedStatement ps = konekcija.prepareStatement(sqlAzuriranje);

			ps.setInt(1, Integer.parseInt(pib));
			ps.setString(2, naziv);
			ps.setString(3, ulica);
			ps.setString(4, broj);
			ps.setString(5, mesto);
			ps.setString(6, telefon);
			ps.setInt(7, izabraniPIB);

			int azuriraniRedovi = ps.executeUpdate();

			if (azuriraniRedovi > 0) {
				JOptionPane.showMessageDialog(okvir, "Biblioteka uspešno ažurirana", "Uspeh",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(okvir, "Ažuriranje biblioteke nije uspelo", "Greška",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	//////////////////
	// OBRIŠI dugme //
	//////////////////
	public static void addActionListenerObrisiButton(JFrame okvir, HashMap<String, Integer> mapaBiblioteka,
			JComboBox<String> kombinovaniOkvirBiblioteka) {
		String izabraniNaziv = (String) kombinovaniOkvirBiblioteka.getSelectedItem();

		// Proveri da li je izabrana opcija podrazumevanih opcija
		if (izabraniNaziv.equals("Dodaj Biblioteku")) {
			JOptionPane.showMessageDialog(okvir, "Molimo izaberite biblioteku za brisanje.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int izabraniPIB = mapaBiblioteka.get(izabraniNaziv);

		// Izvrši SQL DELETE naredbu da bi se uklonila biblioteka iz baze podataka
		try {
			String url = "jdbc:mysql://localhost:3306/info_centar";
			String korisnickoIme = "root";
			String lozinka = "";

			Connection konekcija = DriverManager.getConnection(url, korisnickoIme, lozinka);
			String sqlBrisanje = "DELETE FROM Biblioteke WHERE PIB = ?";
			PreparedStatement ps = konekcija.prepareStatement(sqlBrisanje);
			ps.setInt(1, izabraniPIB);

			int obrisaniRedovi = ps.executeUpdate();

			if (obrisaniRedovi > 0) {
				JOptionPane.showMessageDialog(okvir, "Biblioteka uspešno obrisana", "Uspeh",
						JOptionPane.INFORMATION_MESSAGE);

				// Ažuriraj kombinovani okvir i HashMap da se ukloni obrisana biblioteka
				mapaBiblioteka.remove(izabraniNaziv);
				kombinovaniOkvirBiblioteka.removeItem(izabraniNaziv);
			} else {
				JOptionPane.showMessageDialog(okvir, "Brisanje biblioteke nije uspelo", "Greška",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	//////////////////////////////////
	// Popunjavanje mape BIBLIOTEKA //
	//////////////////////////////////
	public static void popuniBibliotekeComboBox(HashMap<String, Integer> mapaBiblioteka,
			JComboBox<String> comboBoxBiblioteke) {
		try {
			String url = "jdbc:mysql://localhost:3306/info_centar";
			String korisnickoIme = "root";
			String lozinka = "";

			Connection konekcija = DriverManager.getConnection(url, korisnickoIme, lozinka);
			String sqlSelect = "SELECT * FROM Biblioteke";
			PreparedStatement ps = konekcija.prepareStatement(sqlSelect);
			ResultSet rezultat = ps.executeQuery();

			// Petlja kroz rezultate upita i dodavanje naziva biblioteka u comboBox
			while (rezultat.next()) {
				String naziv = rezultat.getString("Naziv");
				int pib = rezultat.getInt("PIB");

				// Dodavanje naziva i pib-a biblioteke u mapu
				mapaBiblioteka.put(naziv, pib);

				// Dodavanje naziva biblioteke u comboBox
				comboBoxBiblioteke.addItem(naziv);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}