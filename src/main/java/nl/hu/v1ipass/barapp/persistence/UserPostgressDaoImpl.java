package nl.hu.v1ipass.barapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPostgressDaoImpl extends PostgresBaseDao{
	public String findRoleForUser(int kassa, int mnr, String pass) throws SQLException {
		Connection conn = getConnection();
		String queryText = "SELECT * FROM public.Medewerker WHERE kassasysteem = ? AND mnr = ? AND wachtwoord LIKE ?";
		PreparedStatement pstmt = conn.prepareStatement(queryText);
		pstmt.setInt(1, kassa);
		pstmt.setInt(2,mnr);
		pstmt.setString(3,pass);
		ResultSet rs = pstmt.executeQuery();
		String role = null;
		while (rs.next()) {
			role = rs.getString("functie");
			System.out.println(rs.getString("functie"));
		}
		
		conn.close();
		return role;
	}
}
