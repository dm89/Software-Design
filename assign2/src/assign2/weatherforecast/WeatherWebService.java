package assign2.weatherforecast;

import java.io.*;
import java.net.*;
import org.json.*;
import java.util.Scanner;

public class WeatherWebService implements WeatherService {
  
  protected InputStream obtainURLforAPI(String city) throws IOException {
    String url = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial";
    return new URL(String.format(url, city)).openStream();
  }

  protected String invokeWebService(String city) throws IOException {
    InputStream cityDataStream = obtainURLforAPI(city);
    Scanner scanner = new Scanner(cityDataStream);
    return scanner.nextLine();
  }

  protected WeatherDetails parseJSONString(String weatherJsonString ) throws IOException {
    
    JSONObject weatherJSONobj = new JSONObject(weatherJsonString);
    JSONArray weatherJSONArray = weatherJSONobj.getJSONArray("weather");
    JSONObject conditionJSONObject = weatherJSONArray.getJSONObject(0);
  
    return new WeatherDetails(
      weatherJSONobj.getString("name"), 
      weatherJSONobj.getJSONObject("main").getDouble("temp"),
      conditionJSONObject.getString("description")
    );
  } 

  public WeatherDetails getData(String city) {

    WeatherDetails weatherDetails;
    try {
      String jsonString = invokeWebService(city);
      weatherDetails = parseJSONString(jsonString);

    } catch(Exception ex) {
      weatherDetails = new WeatherDetails(city, ex);
    }
    return weatherDetails;
  }
}