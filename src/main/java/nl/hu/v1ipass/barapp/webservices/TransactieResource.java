package nl.hu.v1ipass.barapp.webservices;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.v1ipass.barapp.model.Product;
import nl.hu.v1ipass.barapp.model.Transactie;
import nl.hu.v1ipass.barapp.persistence.ProductPostgressDaoImpl;
import nl.hu.v1ipass.barapp.persistence.TransactiePostgressDaoImpl;

@Path("/transactie")
@Consumes("application/json")
public class TransactieResource {
	private TransactiePostgressDaoImpl tDao = new TransactiePostgressDaoImpl();
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public List<Transactie> getTransacties(@PathParam("id") int id) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		List<Transactie> ts = tDao.findAllByKassa(id);
		
		return ts;
	}
	
	@GET
	@Path("/byDate/{id}/{date}")
	@Produces("application/json")
	public List<Transactie> getTransactiesByDate(@PathParam("id") int id, @PathParam("date") Date datum) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		List<Transactie> ts = tDao.findAllByKassaAndDate(id, datum);
		
		return ts;
	}
	
	@GET
	@Path("/byMnr/{id}/{mnr}")
	@Produces("application/json")
	public List<Transactie> getTransactiesByMnr(@PathParam("id") int id, @PathParam("mnr") int mnr) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		List<Transactie> ts = tDao.findAllByKassaAndMedewerker(id, mnr);
		
		return ts;
	}

	@POST
	@RolesAllowed("Barhoofd")
	@Consumes("application/json")
	@Produces("application/json")
	public Response slaTransactieOp(Transactie transactie) throws SQLException {
		Date date = new Date(System.currentTimeMillis());  
		transactie.setDatum(date);
		
		int winst = 0;
		int i = 0;
		List<Product> producten = transactie.getMijnproducten();
		for (int p : transactie.getHoeveelheden()) {
			winst += p * ( producten.get(i).getPrijs() - producten.get(i).getInkoop());
			i += 1;
		}
		transactie.setWinst(winst);
		
		tDao.voegTransactietoe(transactie);
		System.out.println(transactie);
		return Response.ok(transactie).build();
	}
}
