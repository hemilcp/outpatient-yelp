package com.outpatient.yelper.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource exposed at "/search" path
 * @author hemil
 *
 */

@Path("/search")
public class HomeController {

	  	@GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response welcome() {
	  		return Response.ok("Welcome to Outpatient APIs").build();
	    }
	  	
	  	@Path("/pt/{location}")
	  	@GET
	  	@Produces(MediaType.APPLICATION_JSON)
	  	@Consumes(MediaType.APPLICATION_JSON)
	  	public Response findPT(@PathParam(value = "location") String location) throws IOException{
	  		
	  		//  Create a baseURL from the input location
	  			String inputLoc = URLEncoder.encode(location, "UTF-8");
	  			String businessTerm = URLEncoder.encode("Physical Therapists", "UTF-8");
	  			String baseUri = "https://api.yelp.com/v3/businesses/search?term="+businessTerm+"&location="+inputLoc;
	  		
	  		//Load Yelp Key from resource
	  			Properties prop = new Properties();
	  			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	  			InputStream in = classLoader.getResourceAsStream("application.properties");
	  			prop.load(in);
//	  			"https://api.yelp.com/v3/businesses/search?term=Physical%20Therapists&location=San%20Jose,Ca"			
	  	
	  		//Build a HTTPClient to make request to the target API
	  			Client client = ClientBuilder.newClient();
	  			String response =  client.
	  					target(baseUri)
	  					.request()
	  					.header("Authorization", "Bearer "+prop.getProperty("yelpkey"))
	  					.get(String.class);
	  			
	  			return Response.ok(response).build();
	  	}
}
