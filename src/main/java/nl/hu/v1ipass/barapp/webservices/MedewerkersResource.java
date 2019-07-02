package nl.hu.v1ipass.barapp.webservices;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.v1ipass.barapp.model.Medewerker;
import nl.hu.v1ipass.barapp.persistence.MedewerkerPostgressDaoImpl;
import nl.hu.v1ipass.barapp.persistence.ProductPostgressDaoImpl;

@Path("/medewerker")
public class MedewerkersResource {
	private MedewerkerPostgressDaoImpl mDao = new MedewerkerPostgressDaoImpl();
	
	@GET
	@Path("{mnr}")
	@Produces("application/json")
	public Medewerker getMedewerker(@PathParam("mnr") int mnr) throws SQLException {
	Medewerker medewerker = mDao.findbyMnr(mnr);	
	System.out.println(medewerker);	
	return medewerker;
	}

	
	@GET
	@Path("byKassa/{id}")
	@Produces("application/json")
	public List<Medewerker> getMedewerkerByKassa(@PathParam("id") int id) throws SQLException {
	List<Medewerker> medewerkers = mDao.findByKassa(id);	
	System.out.println(medewerkers);	
	return medewerkers;
	}
	
	@GET
	public String gimmeSomethingLikeOkay() {
		return "okay";
	}
	
	@PUT
	@RolesAllowed("Barhoofd")
	@Path("{id}")
	@Produces("application/json")
	public  Response putMedewerker(@PathParam("id") int mnr,
							  @FormParam("VL") String vl,
							  @FormParam("TV") String tv,
							  @FormParam("A") String a,
							  @FormParam("F") String f)throws SQLException {
		
	    return Response.ok(mDao.UpdateMedewerker(mnr,vl,tv,a,f)).build();
	}
	
	@POST
	@RolesAllowed("Barhoofd")
	@Path("{id}")
	@Produces("application/json")
	public  Response postMedewerker(@PathParam("id") int kassa,
							  @FormParam("VL") String vl,
							  @FormParam("TV") String tv,
							  @FormParam("A") String a,
							  @FormParam("F") String f,
							  @FormParam("WW") String ww)throws SQLException {
		
	    return Response.ok(mDao.PostMedewerker(kassa,vl,tv,a,f,ww)).build();
	}
	
	@DELETE
	@RolesAllowed("Barhoofd")
	@Path("{id}")
	@Produces("application/json")
	public  Response deleteMedewerker(@PathParam("id") int mnr) throws SQLException {
		
	    return Response.ok(mDao.DeleteMedewerker(mnr)).build();
	}
}
