package assign2.weatherforecast;

import java.util.List;
import java.util.*;
import java.util.Optional;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class WeatherForecast {

  WeatherService theService;

  public void setWeatherService(WeatherService weatherService) {
    theService = weatherService;
  }

  List<WeatherDetails> getSortedWeatherDetails(List<WeatherDetails> weatherData) {
    return weatherData.stream()
                      .sorted(comparing(WeatherDetails::getCity))
                      .collect(toList());
  }

  List<String> getHottestCities(List<WeatherDetails> weatherData) {
    double hottestTemperature = 
      weatherData.stream()
                 .mapToDouble(WeatherDetails::getTemperature)
                 .max()
                 .orElse(0.0);
    
    return weatherData.stream()
                      .filter(data -> data.getTemperature() == hottestTemperature)
                      .map(WeatherDetails::getCity)
                      .collect(toList());
  }

  List<String> getColdestCities(List<WeatherDetails> weatherData) {
    double coldestTemperature = 
      weatherData.stream()
                 .mapToDouble(WeatherDetails::getTemperature)
                 .min()
                 .orElse(0.0);
    
    return weatherData.stream()
                      .filter(data -> data.getTemperature() == coldestTemperature)
                      .map(WeatherDetails::getCity)
                      .collect(toList());      
  }

  private WeatherDetails getDataFromWeatherService(String city) {
    try { 
      return theService.getData(city); 
    }
    catch (Exception ex) { 
      return new WeatherDetails(city, ex); 
    }
  }

  public WeatherResults getWeatherData(List<String> cities) {

    List<WeatherDetails> weatherData = cities.stream()
                                             .map( this::getDataFromWeatherService )
                                             .collect(toList()); 

    List<WeatherDetails> weatherDataWithoutError = weatherData.stream()
                                                              .filter( cityWeatherData -> !cityWeatherData.isError())
                                                              .collect( toList() );
   
    List<WeatherDetails> weatherDataWithError = weatherData.stream()
                                                           .filter( WeatherDetails::isError )
                                                           .collect( toList() );

                                        
    return new WeatherResults( 
      getSortedWeatherDetails(weatherDataWithoutError),
      getHottestCities(weatherDataWithoutError),
      getColdestCities(weatherDataWithoutError),
      weatherDataWithError
    );
  }

}
