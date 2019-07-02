package nl.hu.v1ipass.barapp.webservices;

import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.v1ipass.barapp.model.Kassasysteem;
import nl.hu.v1ipass.barapp.persistence.KassasysteemPostgressDaoImpl;
import nl.hu.v1ipass.barapp.persistence.ProductPostgressDaoImpl;

@Path("/kassa")
public class KassaResource {
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Kassasysteem getKassa(@PathParam("id") int id) throws SQLException {
		KassasysteemPostgressDaoImpl kDao = new KassasysteemPostgressDaoImpl();
		
		Kassasysteem k = kDao.findByKassa(id);
		
		return k;
		}
	
	@PUT
	@Path("{id}")
	@Produces("application/json")
	public  Response putMedewerker(@PathParam("id") int kn,
							  @FormParam("kNaam") String naam,
							  @FormParam("kLocatie") String locatie,
							  @FormParam("kBudget") double budget)throws SQLException {

		KassasysteemPostgressDaoImpl kDao = new KassasysteemPostgressDaoImpl();
		
	    return Response.ok(kDao.UpdateKassa(kn, naam, locatie, budget)).build();
	}
}
