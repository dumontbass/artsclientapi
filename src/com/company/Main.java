package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        // Parameters filter
        // Must be a valid API name
        if(args == null){
            System.out.println("You must enter an API parameter: -DApi=[movies | music] -DTitle=[title]");
            System.exit(0);
        }

        if(args.length != 2){
            System.out.println("Maybe you've forgot some parameter!");
            System.exit(0);
        }
        final String API = args[0].split("=")[1];
        final String TITLE = args[1].split("=")[1];

        String url = "http://www.omdbapi.com/?t=predator";

        if(API.equals("movies")){
            url = "http://www.omdbapi.com/?t="+TITLE;
        }else if(API.equals("music")){
            url = "http://api.musicgraph.com/api/v2/track/suggest?api_key=" +
                    "c8303e90962e3a5ebd5a1f260a69b138&prefix="+TITLE;
        }else{
            System.out.println("The API parameter must be either movies or music");
            System.exit(0);
        }

        // Parsing
        String raw = retrieveData(url);

        JSONParser parser = new JSONParser();

        Object jsonObj = parser.parse(raw);

        JSONObject jsonObject = (JSONObject) jsonObj;

        if(API.equals("movies")){
            String title = (String) jsonObject.get("Title");
            String year = (String) jsonObject.get("Year");
            String genre = (String) jsonObject.get("Genre");
            String country = (String) jsonObject.get("Country");
            System.out.println("===============================");
            System.out.println("Title: "+title);
            System.out.println("Year: "+year);
            System.out.println("Genre: "+genre);
            System.out.println("Country: "+country);
            System.out.println("===============================");
        }else if(API.equals("music")){
            JSONArray results = (JSONArray)jsonObject.get("data");
            JSONObject trackInfo  = (JSONObject) results.get(0);
            JSONArray composer = (JSONArray) trackInfo.get("composer");
            JSONObject name  = (JSONObject) composer.get(0);

            String artist = (String) name.get("name");
            String main_genre = (String) trackInfo.get("main_genre");
            Long year = (Long) trackInfo.get("original_release_year");
            String album_title = (String) trackInfo.get("album_title");

            System.out.println("===============================");
            System.out.println("Artist: "+artist);
            System.out.println("Year: "+year);
            System.out.println("Genre: "+main_genre);
            System.out.println("Albun Title: "+album_title);
            System.out.println("===============================");
        }


    }

    public static String retrieveData(String url) throws IOException {
        // URL API conn
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }


}
