# outpatient-yelp

1)To execute, import the project into your IDE. (optional)

2)Create a new file, application.properties under src/main/resources which would have the following detail:
yelpkey=############your_Key#####

3) Build project and run over the server.

4) To see the list of PTs in the location, the end point would be:
http://localhost:8080/yelper/webapi/search/{profession_type}/{location}
example,
http://localhost:8080/yelper/webapi/search/Physical Therapists/San Jose,CA
or
http://localhost:8080/yelper/webapi/search/gynecologist/San Jose,CA

5) To see the summary of PT in the given location, the end point would be:
http://localhost:8080/yelper/webapi/search/{profession_type}/{location}
example,
http://localhost:8080/yelper/webapi/search/summary/Physical Therapists/San Jose,CA
or
http://localhost:8080/yelper/webapi/search/summary/gynecologist/San Jose,CA


# Question
What would you change to allow other type of medical professionals, not only PTs?

To allow, other type of medical professionals, you would be changing the type in the URL:

http://localhost:8080/yelper/webapi/search/{type}/{location}
or
http://localhost:8080/yelper/webapi/search/summary/{type}/{location}


