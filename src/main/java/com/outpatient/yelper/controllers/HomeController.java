package com.outpatient.yelper.controllers;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;

import com.outpatient.yelper.services.PTServices;

/**
 * Root resource exposed at "/search" path
 * @author hemil
 *
 */

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HomeController {

	  	@GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response welcome() {
	  		return Response.ok("Welcome to Outpatient APIs").build();
	    }
	  	
	  	@Path("/{business}/{location}")
	  	@GET
	  	public Response findPT(@PathParam(value = "location") String location,@PathParam(value = "business") String business ) throws IOException, ParseException{
	  		
	  		PTServices ptServices = new PTServices();
	  		String jsonObject = ptServices.findNearbyPT(business, location);
	  		return Response.ok(jsonObject).build();
	  	}
	  	
	  	@Path("/{business}/summary/{location}")
	  	@GET	
	  	public Response getSummaryPT(@PathParam(value = "location") String location, @PathParam(value = "business") String business) throws IOException, ParseException{
	  		
	  		PTServices ptServices = new PTServices();
  		
	  		return ptServices.findSummaryForBusinessNearBy(business, location);
	  		
	  	}
	  	
	  	
}
