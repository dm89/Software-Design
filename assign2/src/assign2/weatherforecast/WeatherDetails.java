package assign2.weatherforecast;

import java.util.Optional;

public class WeatherDetails {
    public final String city;
    public final double temperature;
    public final String condition;
    public final Optional<Exception> exception;

  
    WeatherDetails(String theCity, Exception ex) {
      city = theCity;
      exception = Optional.of(ex);
      temperature = 0.0;
      condition = "";
    }

    WeatherDetails( String theCity, double theTemperature, String theCondition) {
      city = theCity;
      temperature = theTemperature;
      condition = theCondition;
      exception = Optional.empty();
    }

    public String getCity() {
      return city;
    }
    
    public double getTemperature() {
      return temperature;
    }

    public Optional<Exception> getExceptionError() {
      return exception;
    }

    public String getCondition() {
      return condition;
    }

    boolean isError() {
      return exception.isPresent();
    }
 }
