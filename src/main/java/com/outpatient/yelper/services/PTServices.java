package com.outpatient.yelper.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PTServices {

	public String findNearbyPT(String location) throws IOException, ParseException{
	
		//  Create a baseURL from the input location
			String inputLoc = URLEncoder.encode(location, "UTF-8");
			String businessTerm = URLEncoder.encode("Physical Therapists", "UTF-8");
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
				System.out.println("Offset: "+offset);
				System.out.println("size: " + size);
				
				String newbaseUri = baseUri + "&offset=" +offset;
				
				String response2 =  client.
						target(newbaseUri)
						.request()
						.header("Authorization", "Bearer "+prop.getProperty("yelpkey"))
						.get(String.class);
				
				System.out.println("response executed");
				JSONObject responseObject = new JSONObject();
				System.out.println("parsing executed");	
				responseObject = (JSONObject) jsonParser.parse(response2);
				
				size = (long) responseObject.get("total");
				
				JSONArray jArray = (JSONArray) responseObject.get("businesses");
				System.out.println("businesses executed");

				for(int i = 0; i < jArray.size(); i++){
				
					JSONObject temp = new JSONObject();
				
					JSONObject jObj = (JSONObject) jArray.get(i);
					temp.put("name", jObj.get("name"));
					temp.put("rating", jObj.get("rating"));
					temp.put("Number of Reviews", jObj.get("review_count"));
					temp.put("Address", jObj.get("location"));
					
					response.put( index, temp);
					index++;
				}
				offset = offset + 50;
			}
			
			
			
			return response.toString();
			
	}
	
	public Response findSummaryNearByPT(String location) throws IOException, ParseException{
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(findNearbyPT(location));
	
//		System.out.println(jsonObject.toJSONString());
		
		return Response.ok("OK").build();
	}
	
}
