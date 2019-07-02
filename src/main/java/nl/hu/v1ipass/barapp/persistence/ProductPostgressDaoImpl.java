package nl.hu.v1ipass.barapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import nl.hu.v1ipass.barapp.model.Product;

public class ProductPostgressDaoImpl extends PostgresBaseDao{
	
	public List<Product> findByKassa(int kassa) throws SQLException{
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Product WHERE kassasysteem = ? ORDER BY naam ASC";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);

		ResultSet rs = pstmt.executeQuery();
		Product p = null;
		List<Product> ps = new ArrayList<Product>();
		while (rs.next()) {
			p = new Product(rs.getInt("productnummer"),rs.getString("naam"),rs.getDouble("prijs"),rs.getInt("minimalevoorraad"), rs.getString("categorie"), rs.getInt("voorraad"),rs.getDouble("inkoop"));
			ps.add(p);
		}
		
		conn.close();	
		return ps;			
	}
	
	public Product findByProduct(int prodId) throws SQLException{
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Product WHERE productnummer = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, prodId);

		ResultSet rs = pstmt.executeQuery();
		Product p = null;
		while (rs.next()) {
			p = new Product(rs.getInt("productnummer"),rs.getString("naam"),rs.getDouble("prijs"),rs.getInt("minimalevoorraad"), rs.getString("categorie"), rs.getInt("voorraad"),rs.getDouble("inkoop"));
		}
		
		conn.close();	
		return p;			
	}
	
	public List<Product> findByCategorie(String cat) throws SQLException{
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Product WHERE categorie = ? ORDER BY naam ASC";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setString(1, cat);

		ResultSet rs = pstmt.executeQuery();
		Product p = null;
		List<Product> ps = new ArrayList<Product>();
		while (rs.next()) {
			p = new Product(rs.getInt("productnummer"),rs.getString("naam"),rs.getDouble("prijs"),rs.getInt("minimalevoorraad"), rs.getString("categorie"), rs.getInt("voorraad"),rs.getDouble("inkoop"));
			ps.add(p);
		}
		
		conn.close();	
		return ps;			
	}
	
	public String findBestelLijst(int kassa) throws SQLException{
		Connection conn = getConnection();
		String queryText = "select productnummer, naam, (minimalevoorraad - voorraad) as aantal, (inkoop * (minimalevoorraad - voorraad)) as kosten from product where kassasysteem = ? ORDER BY naam ASC";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);

		ResultSet rs = pstmt.executeQuery();
		Product p = null;
		JsonArrayBuilder jab = Json.createArrayBuilder();  
		while (rs.next()) {
			JsonObjectBuilder product = Json.createObjectBuilder();
		    product.add("naam", rs.getString("naam"));
		    product.add("aantal", rs.getInt("aantal"));
		    product.add("kosten", rs.getDouble("kosten"));
			jab.add(product);
		}
		JsonArray array = jab.build();
		
		conn.close();	
		return array.toString();			
	}
	
	public String bevestig(int kassa, double totaal) throws SQLException{
		Connection conn = getConnection();
		String queryText = "update product set voorraad = minimalevoorraad where kassasysteem=?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		System.out.println(rs);
		
		queryText = "update kassasysteem set budget = (budget - ?) where kassanummer = 1234;	";
		pstmt = conn.prepareStatement(queryText);
		pstmt.setDouble(1, totaal);
		rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		
		conn.close();	
		return "OK";			
	}
	
	public boolean UpdateProduct(int pn, String naam, double prijs, double inkoop, int minVoorraad, int voorraad, String cat) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "UPDATE product SET naam = ?, prijs =?, inkoop=?, minimaleVoorraad = ?, voorraad = ?, categorie=? WHERE productnummer = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setString(1, naam);
		pstmt.setDouble(2, prijs);
		pstmt.setDouble(3, inkoop);
		pstmt.setInt(4, minVoorraad);
		pstmt.setInt(5, voorraad);
		pstmt.setString(6, cat);
		pstmt.setInt(7, pn);
		
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		return true;		
	}
	
	public boolean PostProduct(int kassa, String naam, double prijs, double inkoop, int minVoorraad, int voorraad, String cat) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "select MAX(productnummer) from product";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		ResultSet rs = pstmt.executeQuery();
		
		int prodNummer = 0;
		while (rs.next()) {
			prodNummer = rs.getInt("max") + 1;
		}
		
		queryText = "INSERT INTO public.product(productnummer, naam, prijs, minimalevoorraad, categorie, kassasysteem, voorraad, inkoop) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, prodNummer);
		pstmt.setString(2, naam);
		pstmt.setDouble(3, prijs);
		pstmt.setInt(4, minVoorraad);
		pstmt.setString(5, cat);
		pstmt.setInt(6, kassa);
		pstmt.setInt(7, voorraad);
		pstmt.setDouble(8, inkoop);
		
		int s = pstmt.executeUpdate();
		System.out.println(s);
		
		return true;		
	}
	
	public boolean DeleteProduct(int pn) throws SQLException {
		Connection conn = getConnection();
		
		String queryText = "Delete from product where productnummer = ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, pn);
		
		int rs = pstmt.executeUpdate();
		System.out.println(rs);
		
		return true;		
	}
}
