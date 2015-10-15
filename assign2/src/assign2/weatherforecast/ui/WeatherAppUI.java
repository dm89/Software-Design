package assign2.weatherforecast.ui;

import java.util.*;
import java.io.*;
import assign2.weatherforecast.*;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

public class WeatherAppUI {

  public static void main(String[] args) {
    WeatherWebService weatherService = new WeatherWebService();
    WeatherForecast forecast = new WeatherForecast();
    forecast.setWeatherService(weatherService);

    List<String> cityList = readInput();
    WeatherResults weatherResults = forecast.getWeatherData(cityList);
    displayResults(weatherResults);
  }

  private static List<String> readInput() {
    List<String> cities = new ArrayList<String>();    
    try {
      cities = Files.readAllLines(Paths.get("input.txt"), Charset.defaultCharset());

    } catch (IOException e) {
      e.printStackTrace();
    }
    return cities;
  }

  private static void displayResults(WeatherResults results) {
    
    List<WeatherDetails> exceptionCities = results.getExceptionCities();

    results.getSortedResults().forEach( details -> 
      System.out.println( details.getCity() 
        + "\t\t" + details.getTemperature() 
        + "\u00b0 F \t " + details.getCondition() )
    );
    
    System.out.println("Hottest Cities: ");
    results.getHottestCities().forEach( city -> System.out.println(city) );
    
    System.out.println("Coldest Cities: ");
    results.getColdestCities().forEach( city -> System.out.println(city) );

    results.getExceptionCities().forEach( details -> System.out.println("Error : " + details.getCity() 
        + " " + details.getExceptionError().get().getMessage() ));
  }



}