package nl.hu.v1ipass.barapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nl.hu.v1ipass.barapp.model.Kassasysteem;

public class KassasysteemPostgressDaoImpl extends PostgresBaseDao{
	
	public Kassasysteem findByKassa(int kassa) throws SQLException{
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Kassasysteem WHERE kassanummer = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);

		ResultSet rs = pstmt.executeQuery();
		Kassasysteem k = null;
		while (rs.next()) {
			k = new Kassasysteem(kassa, rs.getString("naam"),rs.getString("locatie"),rs.getDouble("budget"));
			System.out.println(k);
		}
		
		conn.close();	
		return k;			
	}
	
	public boolean UpdateKassa(int kn, String naam, String locatie, double budget) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "UPDATE kassasysteem SET naam = ?, locatie =?, budget=? WHERE kassanummer = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setString(1, naam);
		pstmt.setString(2, locatie);
		pstmt.setDouble(3, budget);
		pstmt.setInt(4, kn);
		
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		return true;		
	}
}
