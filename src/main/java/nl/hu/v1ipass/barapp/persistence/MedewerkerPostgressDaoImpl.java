package nl.hu.v1ipass.barapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nl.hu.v1ipass.barapp.model.Medewerker;

public class MedewerkerPostgressDaoImpl extends PostgresBaseDao{

	public Medewerker findbyMnr(int mnr) throws SQLException {
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Medewerker WHERE mnr = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, mnr);

		ResultSet rs = pstmt.executeQuery();
		Medewerker m = null;
		while (rs.next()) {
			m = new Medewerker(rs.getInt("mnr"),rs.getString("voorletters"),rs.getString("achternaam"),rs.getString("tussenvoegsel"),rs.getString("functie"));
		}
		
		conn.close();	
		return m;		
	}
	
	public List<Medewerker> findByKassa(int kassa) throws SQLException{
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Medewerker WHERE kassasysteem = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);

		ResultSet rs = pstmt.executeQuery();
		Medewerker m = null;
		List<Medewerker> ms = new ArrayList<Medewerker>();
		while (rs.next()) {
			m = new Medewerker(rs.getInt("mnr"),rs.getString("voorletters"),rs.getString("achternaam"),rs.getString("tussenvoegsel"),rs.getString("functie"));
			ms.add(m);
		}
		
		conn.close();	
		return ms;			
	}
	
	public boolean UpdateMedewerker(int mnr, String vl, String tv, String a, String f) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "UPDATE Medewerker SET voorletters = ?, tussenvoegsel =?, achternaam=?, functie = ? WHERE mnr = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setString(1, vl);
		pstmt.setString(2, tv);
		pstmt.setString(3, a);
		pstmt.setString(4, f);
		pstmt.setInt(5, mnr);

		
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		return true;		
	}
	
	public boolean PostMedewerker(int kassa, String vl, String tv, String a, String f, String ww) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "select MAX(mnr) from medewerker";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		ResultSet rs = pstmt.executeQuery();
		
		int mnr = 0;
		while (rs.next()) {
			mnr = rs.getInt("max") + 1;
		}
		
		queryText = "insert into medewerker (mnr, voorletters, tussenvoegsel, achternaam, functie, wachtwoord, kassasysteem) values (?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, mnr);
		pstmt.setString(2, vl);
		pstmt.setString(3, tv);
		pstmt.setString(4, a);
		pstmt.setString(5, f);
		pstmt.setString(6, ww);
		pstmt.setInt(7, kassa);
		int r = pstmt.executeUpdate();
		System.out.println(r);
		
		return true;		
	}
	
	public boolean DeleteMedewerker(int mnr) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "Delete from medewerker where mnr = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, mnr);

		
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		return true;		
	}
}
