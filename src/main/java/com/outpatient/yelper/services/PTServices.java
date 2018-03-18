package com.outpatient.yelper.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class PTServices {

	public String findNearbyPT(String location) throws IOException{
	
		//  Create a baseURL from the input location
			String inputLoc = URLEncoder.encode(location, "UTF-8");
			String businessTerm = URLEncoder.encode("Physical Therapists", "UTF-8");
			String baseUri = "https://api.yelp.com/v3/businesses/search?term="+businessTerm+"&location="+inputLoc;
  		//  "https://api.yelp.com/v3/businesses/search?term=Physical%20Therapists&location=San%20Jose,Ca"			

		//Load Yelp Key from resource
			Properties prop = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream("application.properties");
			prop.load(in);
	
		//Build a HTTPClient to make request to the target API
			Client client = ClientBuilder.newClient();
			String response =  client.
					target(baseUri)
					.request()
					.header("Authorization", "Bearer "+prop.getProperty("yelpkey"))
					.get(String.class);
			
//			JSONObject result = new JSONObject();
//			result.put("result", response);
			return response;
			
//			return Response.ok(response).build();
	}
	
	public Response findSummaryNearByPT(String location) throws IOException{
		
//		JSONObject jsonObject = (JSONObject) findNearbyPT(location).get("result");  
		
//		System.out.println(jsonObject);
		
		return null;
	}
	
}
