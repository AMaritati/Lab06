package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE localita = ? AND Data LIKE ?";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, localita);
			
			if (mese <10) {
				st.setString(2, "2013-0"+mese+"-%");
			}
			else {
				st.setString(2, "2013-"+mese+"-%");
			}
			
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

public String getMediaLocalitaMese(int mese) {
		
		final String sql = "SELECT Localita, AVG(Umidita) AS M FROM situazione WHERE Data LIKE ? GROUP BY Localita";

		String finale = "";
		//List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			if (mese <10) {
				st.setString(1, "2013-0"+mese+"-%");
			}
			else {
				st.setString(1, "2013-"+mese+"-%");
			}
			
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				finale += rs.getString("Localita") + "\t" + rs.getDouble("M") + "\n";
				//Rilevamento r = new Rilevamento(rs.getString("Localita"),rs.getDouble("M"));
				//rilevamenti.add(r);
			}

			conn.close();
			return finale;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
