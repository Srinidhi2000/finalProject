package com.example.android.project;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.project.places.Place;
import com.example.android.project.specificvenue.VenueDetails;
import com.example.android.project.specificvenue.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
//To make HTTP requests and parse JSON
public final class retrieve_json {
private retrieve_json(){}
//To create a URL from a given string
    private static URL createUrl(String stringurl)
    { URL url=null;
        try{
            url=new URL(stringurl);

        }catch (MalformedURLException e)
        {Log.e("Retrieve_json","Problem in building URL",e);
        }
        return url;
    }
    //To make HTTP request to the given URL
    private static String makeHttpRequest(URL url)throws IOException
    {
        String jsonResponse="";
        if(url==null)
        {return jsonResponse;}
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try{
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(100000);
            urlConnection.setConnectTimeout(100000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                jsonResponse=readInputStream(inputStream);
            }else{
                Log.e("Retrieve_JSON","Error response code:"+urlConnection.getResponseCode());
            }
        }catch (IOException e)
        {Log.e("Retrieve_JSON","Problem retrieving Info",e);}
        finally {
            if(urlConnection!=null)
            {urlConnection.disconnect();}
            if(inputStream!=null)
            {inputStream.close();}
        }
        return jsonResponse;
    }
    //To retrieve the JSON from the URL
    private static String readInputStream (InputStream inputStream)throws IOException
    {StringBuilder finalstring=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String currentline=reader.readLine();
            while(currentline!=null)
            {
                finalstring.append(currentline);
                currentline=reader.readLine();
            }
        }
        return finalstring.toString();
    }
    //type:1
    private static List<Place> extractJSON(String jsonResponse)
    {
        if (TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        List<Place> places=new ArrayList<>();
        try{
            JSONObject root=new JSONObject(jsonResponse);
            JSONObject response=root.getJSONObject("response");
            JSONArray groups=response.getJSONArray("groups");
            JSONObject details=groups.getJSONObject(0);
                JSONArray items=details.getJSONArray("items");
                for(int i=0;i<items.length();i++)
                {
                    JSONObject place_item=items.getJSONObject(i);
                    JSONObject venue=place_item.getJSONObject("venue");
                    String id=venue.getString("id");
                    String isOpen="Timing NA",rating="NA";
                        String name = venue.getString("name");
                        JSONObject location = venue.getJSONObject("location");
                        JSONArray address = location.getJSONArray("formattedAddress");
                        StringBuilder formattedAdress= new StringBuilder();
                        for (int j = 0; j < address.length(); j++) {
                            if(j!=0)
                                formattedAdress.append(" , ").append(address.getString(j));
                            else
                                formattedAdress.append(address.getString(j));
                        }
                        JSONArray categories = venue.getJSONArray("categories");
                        JSONObject obj = categories.getJSONObject(0);
                        String namecat = obj.getString("name");

                        Place current = new Place(id,name, formattedAdress.toString(),namecat,isOpen,rating );
                        places.add(current);
                    }



        }catch(JSONException e)
        {Log.e("Retrieve_json","Problem in parsing",e);}
        return places;

    }
    public static List<Place> fetchdata(String requestedurl)
    {
        URL url =createUrl(requestedurl);
        String jsonResponse=null;
        try{ jsonResponse=makeHttpRequest(url);

        }catch(IOException e){
            Log.e("Retrieve_json","Problem making HTTP request",e);
        }
        List<Place> places=extractJSON(jsonResponse);
        return places;
    }
    //Type 2:
    private static VenueDetails ExtractDetails(String jsonResponse)
    {
        if(TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        VenueDetails current=null;
        try{
            JSONObject root=new JSONObject(jsonResponse);
            JSONObject response=root.getJSONObject("response");
            JSONObject venue=response.getJSONObject("venue");
            String name=venue.getString("name");
            JSONObject contact=venue.getJSONObject("contact");
            String phone="-";
            if(contact.has("phone"))
            { phone=contact.getString("phone");
            }
            JSONObject location=venue.getJSONObject("location");
            JSONArray address = location.getJSONArray("formattedAddress");
            StringBuilder formattedAdress= new StringBuilder();
            for (int j = 0; j < address.length(); j++) {
                if(j!=0)
                formattedAdress.append(" , ").append(address.getString(j));
            else
                    formattedAdress.append(address.getString(j));
            }
            String lat=location.getString("lat");
            String lng=location.getString("lng");
            JSONArray category=venue.getJSONArray("categories");
            JSONObject type=category.getJSONObject(0);
            String categoryname=type.getString("name");
            String rating="NA";
            if(venue.has("rating"))
            {rating=venue.getString("rating");
            }
            String description="";
            if(venue.has("description"))
            {description=venue.getString("description");}
            String url="";
            if(venue.has("url"))
            {url=venue.getString("url");}
            String status="Timing NA";
            if(venue.has("hours"))
            {
                JSONObject hours=venue.getJSONObject("hours");
                 status=hours.getString("status");
            }
current=new VenueDetails(name,phone,lat,lng, formattedAdress.toString(),categoryname,rating,description,status,url);
        } catch (JSONException e) {
            Log.e("Retrieve_json","Problem parsing",e);
        }
    return current;
    }
public static VenueDetails fetchDetails(String requiredUrl)
{
    URL url=createUrl(requiredUrl);
   String json=null;
   try{ json=makeHttpRequest(url);


   } catch (IOException e) {
       Log.e("Retrieve_json","Problem making HTTP request",e);
          }
VenueDetails details=ExtractDetails(json);
   return details;
}
//Type 3:
private static Weather ExtractWeather(String jsonResponse)
{ if (TextUtils.isEmpty(jsonResponse))
{
    return null;
}
    Weather current=null;
try{
    JSONObject root=new JSONObject(jsonResponse);
    JSONArray weather=root.getJSONArray("weather");
    JSONObject des=weather.getJSONObject(0);
    String desc=des.getString("description");
  String description=desc.substring(0,1).toUpperCase()+desc.substring(1);
    JSONObject main=root.getJSONObject("main");
    double temp=Double.parseDouble(main.getString("temp"));
    temp=temp-273.15;
    temp=Math.round(temp*100.0)/100.0;
    String temperature=temp+"\u2103";
    current=new Weather(temperature,description);
} catch (JSONException e) {
    Log.e("Retrieve_json","Problem making HTTP request",e);
}
return current;
}
public static Weather fetchWeather(String requestedUrl)
{URL url =createUrl(requestedUrl);
    String jsonResponse=null;
    try{ jsonResponse=makeHttpRequest(url);

    }catch(IOException e){
        Log.e("Connect","Problem making HTTP request",e);
    }
    Weather report=ExtractWeather(jsonResponse);
    return  report;
}
}
