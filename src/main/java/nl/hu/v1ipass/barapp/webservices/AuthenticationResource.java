package nl.hu.v1ipass.barapp.webservices;

import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;

import javax.crypto.SecretKey;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.Response;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.v1ipass.barapp.persistence.UserPostgressDaoImpl;

@Path("/authentication")
public class AuthenticationResource {
  final static public SecretKey key = MacProvider.generateKey();

  @POST
  @Produces("application/json")
  public Response authenticateUser( @FormParam("kassanummer") int kassa,
		  							@FormParam("username") int username, 
                                    @FormParam("password") String password) throws SQLException {
    try {
      // Authenticate the user against the database
      UserPostgressDaoImpl dao = new UserPostgressDaoImpl();
      String role = dao.findRoleForUser(kassa, username, password);
      
      if (role == null) { throw new IllegalArgumentException("No user found!");  } 
      
      String token = createToken(Integer.toString(username), role);

      SimpleEntry<String, String> JWT = new SimpleEntry<String, String>("JWT", token);
      return Response.ok(JWT).build();


    } catch (JwtException | IllegalArgumentException e) 
        { return Response.status(Response.Status.UNAUTHORIZED).build(); }
  }
  
  private String createToken(String username, String role) throws JwtException {
	    Calendar expiration = Calendar.getInstance();
	    expiration.add(Calendar.MINUTE, 30);
	  
	    return Jwts.builder()
	      .setSubject(username)
	      .setExpiration(expiration.getTime())
	      .claim("role", role)
	      .signWith(SignatureAlgorithm.HS512, key)
	      .compact();
	  }
} 

