package nl.hu.v1ipass.barapp.webservices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.v1ipass.barapp.model.Product;
import nl.hu.v1ipass.barapp.persistence.ProductPostgressDaoImpl;

@Path("/product")
public class ProductenResource {
	@GET
	@Path("{id}")
	@Produces("application/json")
	public List<Product> getProducten(@PathParam("id") int id) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		List<Product> ps = pDao.findByKassa(id);
		
		return ps;
		}
	
	@GET
	@Path("/byId/{id}")
	@Produces("application/json")
	public Product getProductbyId(@PathParam("id") int id) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		Product p = pDao.findByProduct(id);
		
		return p;
		}
	
	@GET
	@Path("/byCat/{id}")
	@Produces("application/json")
	public  List<Product> getProductbyCat(@PathParam("id") String cat) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		List<Product> ps = pDao.findByCategorie(cat);
		
		return ps;
	}
	
	@GET
	@Path("/bestel/{id}")
	@Produces("application/json")
	public  String getBestel(@PathParam("id") int kassa) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
	    return pDao.findBestelLijst(kassa);

	}
	
	@POST
	@RolesAllowed("Barhoofd")
	@Path("/bevestig/{id}/{totaal}")
	@Produces("application/json")
	public  Response updateBevestig(@PathParam("id") int kassa, @PathParam("totaal") double totaal) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		return Response.ok(pDao.bevestig(kassa, totaal)).build();
	}
	
	@PUT
	@RolesAllowed("Barhoofd")
	@Path("{id}")
	@Produces("application/json")
	public  Response putProduct(@PathParam("id") int pn,
							  @FormParam("naam") String naam,
							  @FormParam("prijs") double prijs,
							  @FormParam("inkoopPrijs") double inkoop,
							  @FormParam("minVoorraad") int minV,
							  @FormParam("voorraad") int v,
							  @FormParam("categorie") String cat)throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
	    return Response.ok(pDao.UpdateProduct(pn, naam, prijs, inkoop, minV, v, cat)).build();
	}
	
	@POST
	@RolesAllowed("Barhoofd")
	@Path("{id}")
	@Produces("application/json")
	public  Response postProduct(@PathParam("id") int kassa,
							  @FormParam("naam") String naam,
							  @FormParam("prijs") double prijs,
							  @FormParam("inkoopPrijs") double inkoop,
							  @FormParam("minVoorraad") int minV,
							  @FormParam("voorraad") int v,
							  @FormParam("categorie") String cat)throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
	    return Response.ok(pDao.PostProduct(kassa, naam, prijs, inkoop, minV, v, cat)).build();
	}
	
	@DELETE
	@RolesAllowed("Barhoofd")
	@Path("{id}")
	@Produces("application/json")
	public  Response deleteProduct(@PathParam("id") int pn) throws SQLException {
		ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
		
		return Response.ok(pDao.DeleteProduct(pn)).build();
	}
}
