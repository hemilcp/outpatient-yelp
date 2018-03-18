package com.outpatient.yelper.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PTServices {

	public String findNearbyPT(String business, String location) throws IOException, ParseException{
	
		//  Create a baseURL from the input location
			String inputLoc = URLEncoder.encode(location, "UTF-8");
//			String businessTerm = URLEncoder.encode("Physical Therapists", "UTF-8");
			String businessTerm = URLEncoder.encode(business, "UTF-8");
		
			String baseUri = "https://api.yelp.com/v3/businesses/search?term="+businessTerm+"&location="+inputLoc+"&sort_by=rating&limit=50"; 
			
			//&limit=50&offset=100";
  		//  "https://api.yelp.com/v3/businesses/search?term=Physical%20Therapists&location=San%20Jose,Ca"			

		//Load Yelp Key from resource
			Properties prop = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream("application.properties");
			prop.load(in);
	
		//Build a HTTPClient to make request to the target API
			Client client = ClientBuilder.newClient();

			long size = 1, index = 1;
			
			JSONParser jsonParser = new JSONParser();
			JSONObject response = new JSONObject();
		
			for(int offset = 0; offset < size;){
			
				String newbaseUri = baseUri + "&offset=" +offset;
				
				String response2 =  client.
						target(newbaseUri)
						.request()
						.header("Authorization", "Bearer "+prop.getProperty("yelpkey"))
						.get(String.class);
				
				JSONObject responseObject = new JSONObject();
				responseObject = (JSONObject) jsonParser.parse(response2);
				
				size = (long) responseObject.get("total");
				
				JSONArray jArray = (JSONArray) responseObject.get("businesses");

				for(int i = 0; i < jArray.size(); i++){
				
					JSONObject temp = new JSONObject();
				
					JSONObject jObj = (JSONObject) jArray.get(i);
					temp.put("Name", jObj.get("name"));
					temp.put("Rating", jObj.get("rating"));
					temp.put("Number of Reviews", jObj.get("review_count"));
					temp.put("Address", jObj.get("location"));
					
					response.put( index, temp);
					index++;
				}
				offset = offset + 50;
			}
			
			
			
			return response.toString();
			
	}
	
	public Response findSummaryForBusinessNearBy(String business, String location) throws IOException, ParseException{
		
		//  Create a baseURL from the input location
		String inputLoc = URLEncoder.encode(location, "UTF-8");
//		String businessTerm = URLEncoder.encode("Physical Therapists", "UTF-8");
		String businessTerm = URLEncoder.encode(business, "UTF-8");
		String baseUri = "https://api.yelp.com/v3/businesses/search?term="+businessTerm+"&location="+inputLoc+"&sort_by=rating&limit=50"; 
		
	
	//Load Yelp Key from resource
		Properties prop = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("application.properties");
		prop.load(in);

	//Build a HTTPClient to make request to the target API
		Client client = ClientBuilder.newClient();

		long size = 1;
		
		long numberOfPT = 0, totalPTwithRating = 0, totalReviews = 0;
		double averageRating = 0.0;
		
		JSONParser jsonParser = new JSONParser();
		JSONObject response = new JSONObject();
	
		for(int offset = 0; offset < size;){
			
			String newbaseUri = baseUri + "&offset=" +offset;
			
			String response2 =  client.
					target(newbaseUri)
					.request()
					.header("Authorization", "Bearer "+prop.getProperty("yelpkey"))
					.get(String.class);
			
			JSONObject responseObject = new JSONObject();
			responseObject = (JSONObject) jsonParser.parse(response2);
			
			size = (long) responseObject.get("total");
			
			numberOfPT = size;
			
			
			JSONArray jArray = (JSONArray) responseObject.get("businesses");

			for(int i = 0; i < jArray.size(); i++){
			
		
				JSONObject jObj = (JSONObject) jArray.get(i);
				
	
				if(jObj.get("rating")!= null) totalPTwithRating++;
				averageRating += (double) jObj.get("rating");
				
				if(jObj.get("review_count") != null) totalReviews += (long) jObj.get("review_count");
				
			}
			offset = offset + 50;
		}
		
		response.put("Number of "+ business +" in area", numberOfPT);
		response.put("Total "+ business +" with rating", totalPTwithRating);

		DecimalFormat df = new DecimalFormat("#.##");      
		response.put("Avaerage rating for "+ business +" in area", Double.valueOf(df.format((double) (averageRating / totalPTwithRating))));
		response.put("Total number of reviews", totalReviews);
		
		return Response.ok(response.toString()).build();
	}
	
}
