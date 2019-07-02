package nl.hu.v1ipass.barapp.persistence;

import java.awt.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nl.hu.v1ipass.barapp.model.Product;
import nl.hu.v1ipass.barapp.model.Transactie;


public class TransactiePostgressDaoImpl extends PostgresBaseDao{
	public Boolean voegTransactietoe(Transactie transactie) throws SQLException {
		int transactienummer = 	getHoogsteNummer() + 1;	
		Connection conn = super.getConnection();
		
		String queryText = "INSERT INTO transactie(transactienummer,winst,datum,kassasysteem,medewerker) VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, transactienummer);
		pstmt.setDouble(2, transactie.getWinst());
		pstmt.setDate(3, transactie.getDatum());
		pstmt.setInt(4, transactie.getKassanummer().getKassanummer());
		pstmt.setInt(5, transactie.getMedewerker().getMnr());
				
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		int i = 0;
		java.util.List<Integer> hoeveelheden = transactie.getHoeveelheden();
		for (Product product : transactie.getMijnproducten()) {
			queryText = "INSERT INTO product_transactie(p_productnummer, p_transactienummer, hoeveelheid) VALUES(?,?,?)";
			pstmt = conn.prepareStatement(queryText);
			pstmt.setInt(1, product.getProductnummer());
			pstmt.setInt(2, transactienummer);
			pstmt.setInt(3, hoeveelheden.get(i));
			
			rs = pstmt.executeUpdate();
			System.out.println(rs);
			
			queryText = "update product set voorraad = (voorraad - ?) where productnummer = ?";
			pstmt = conn.prepareStatement(queryText);
			pstmt.setInt(1, hoeveelheden.get(i));
			pstmt.setInt(2, product.getProductnummer());
			
			rs = pstmt.executeUpdate();
			System.out.println(rs);
			
			queryText = "update kassasysteem set budget = (budget + ?) where kassanummer = ?";
			pstmt = conn.prepareStatement(queryText);
			double prijs = hoeveelheden.get(i) * product.getPrijs();
			pstmt.setDouble(1, prijs);
			pstmt.setInt(2, transactie.getKassanummer().getKassanummer());
			
			rs = pstmt.executeUpdate();
			System.out.println(rs);
			i += 1;
		}
		conn.close();
		return true;		
	}
	
	public ArrayList<Transactie> findAllByKassa(int kassa) throws SQLException{
		Connection conn = getConnection();
		String queryText = "select transactie.kassasysteem, transactienummer, winst, datum, medewerker, productnummer, hoeveelheid from transactie, product_transactie, product where transactienummer = p_transactienummer and productnummer = p_productnummer and transactie.kassasysteem = ? ORDER BY transactienummer DESC";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);

		ResultSet rs = pstmt.executeQuery();
		Transactie t = null;
		ArrayList<Transactie> ts = new ArrayList<Transactie>();
		int transactienummer = 999999999;
		while (rs.next()) {
			if(rs.getInt("transactienummer") == transactienummer) {
				t.addP(rs.getInt("productnummer"));
				t.addH(rs.getInt("hoeveelheid"));
			}
			else {	
				if (t != null) {
					ts.add(t);						
				}		
				transactienummer = rs.getInt("transactienummer");
				t = new Transactie(rs.getDouble("winst"),rs.getDate("datum"),kassa,rs.getInt("medewerker"),rs.getInt("productnummer"),rs.getInt("hoeveelheid"));			
			}
		}
		if (t != null) {
			ts.add(t);						
		}	
		
		conn.close();	
		return ts;		
	}
	
	public ArrayList<Transactie> findAllByKassaAndDate(int kassa, Date date) throws SQLException{
		Connection conn = getConnection();
		String queryText = "select transactie.kassasysteem, transactienummer, winst, datum, medewerker, productnummer, hoeveelheid from transactie, product_transactie, product where transactienummer = p_transactienummer and productnummer = p_productnummer and transactie.kassasysteem = ? and datum >= ? ORDER BY transactienummer DESC";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);
		pstmt.setDate(2, date);

		ResultSet rs = pstmt.executeQuery();
		Transactie t = null;
		ArrayList<Transactie> ts = new ArrayList<Transactie>();
		int transactienummer = 999999999;
		while (rs.next()) {
			if(rs.getInt("transactienummer") == transactienummer) {
				t.addP(rs.getInt("productnummer"));
				t.addH(rs.getInt("hoeveelheid"));
			}
			else {	
				if (t != null) {
					ts.add(t);						
				}		
				transactienummer = rs.getInt("transactienummer");
				t = new Transactie(rs.getDouble("winst"),rs.getDate("datum"),kassa,rs.getInt("medewerker"),rs.getInt("productnummer"),rs.getInt("hoeveelheid"));			
			}
		}
		if (t != null) {
			ts.add(t);						
		}	
		
		conn.close();	
		return ts;		
	}
	
	public ArrayList<Transactie> findAllByKassaAndMedewerker(int kassa, int mnr) throws SQLException{
		Connection conn = getConnection();
		String queryText = "select transactie.kassasysteem, transactienummer, winst, datum, medewerker, productnummer, hoeveelheid from transactie, product_transactie, product where transactienummer = p_transactienummer and productnummer = p_productnummer and transactie.kassasysteem = ? and medewerker = ? ORDER BY transactienummer DESC";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);
		pstmt.setInt(2, mnr);

		ResultSet rs = pstmt.executeQuery();
		Transactie t = null;
		ArrayList<Transactie> ts = new ArrayList<Transactie>();
		int transactienummer = 999999999;
		while (rs.next()) {
			if(rs.getInt("transactienummer") == transactienummer) {
				t.addP(rs.getInt("productnummer"));
				t.addH(rs.getInt("hoeveelheid"));
			}
			else {	
				if (t != null) {
					ts.add(t);						
				}		
				transactienummer = rs.getInt("transactienummer");
				t = new Transactie(rs.getDouble("winst"),rs.getDate("datum"),kassa,rs.getInt("medewerker"),rs.getInt("productnummer"),rs.getInt("hoeveelheid"));			
			}
		}
		if (t != null) {
			ts.add(t);						
		}	
		
		conn.close();	
		return ts;		
	}
	
	public int getHoogsteNummer() throws SQLException {
		Connection conn = super.getConnection();
		String queryText = "SELECT MAX(transactienummer) FROM transactie";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		ResultSet rs = pstmt.executeQuery();
		int nummer = 0;
		while(rs.next()) {
			nummer = rs.getInt("max");			
		}
		
		conn.close();
		return nummer;
	}
	
}
