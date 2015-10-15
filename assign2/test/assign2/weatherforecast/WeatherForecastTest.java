package assign2.weatherforecast;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import java.io.IOException;

import java.util.*;

public class WeatherForecastTest {

  private WeatherForecast forecast;
  private WeatherDetails houstonData, austinData, dallasData, washingtonData, miamiData, floridaData, californiaData;
  private WeatherService weatherService;

  @Before
  public void init() {
    forecast = new WeatherForecast();

    houstonData = new WeatherDetails("Houston", 20.0, "cloudy");
    austinData = new WeatherDetails("Austin", 30.3, "sunny");
    dallasData  = new WeatherDetails("Dallas", 40.2, "rainy");
    washingtonData = new WeatherDetails("Washington", 40.2, "rainy");
    miamiData = new WeatherDetails("Miami", 20.0, "snow");
    floridaData = new WeatherDetails("Florida", 40.2, "sunny");
    californiaData = new WeatherDetails("California", 20.0, "cold");
  
  }

  @Test
  public void Canary() {
    assertTrue(true);
  }

  @Test
  public void testSortByCity() {
    List<WeatherDetails> sampleData = Arrays.asList(houstonData, austinData, dallasData);
    List<WeatherDetails> sortedData = Arrays.asList(austinData, dallasData, houstonData);
    List<WeatherDetails> result = forecast.getSortedWeatherDetails(sampleData);
    assertEquals(sortedData, result);
  }

  @Test
  public void testGetSortedWeatherDetailsWithEmptyInput() {
    List<WeatherDetails> result = forecast.getSortedWeatherDetails(new ArrayList<WeatherDetails>());
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGetSortedWeatherDetailsWithSingleInput() {
    List<WeatherDetails> result = forecast.getSortedWeatherDetails(Arrays.asList(austinData));
    assertEquals("Austin", result.get(0).getCity());
  }

  @Test
  public void testSortByCityPresortedData() {
    List<WeatherDetails> sortedData = Arrays.asList(austinData, dallasData, houstonData);
    List<WeatherDetails> result = forecast.getSortedWeatherDetails(sortedData);
    assertEquals(sortedData, result);
 }

  @Test
  public void testGetHottestCity() {
    List<WeatherDetails> sampleData = Arrays.asList(houstonData, austinData, dallasData);
    assertEquals(Arrays.asList("Dallas"), forecast.getHottestCities(sampleData));
  }

  @Test
  public void testGetHottestCityWithEmptyData() {
    assertEquals(new ArrayList<String>(), forecast.getHottestCities(new ArrayList<WeatherDetails>()));
  }

  @Test
  public void testGetHottestCityWhenOnlyOneCityPresent() {
    assertEquals(Arrays.asList("Austin"), forecast.getHottestCities(Arrays.asList(austinData)));
  }

  @Test
  public void testGetHottestCityWithTwoHottestCities() {
    List<WeatherDetails> sampleDoubleData = Arrays.asList(houstonData, austinData, dallasData, washingtonData, miamiData);
    assertEquals(Arrays.asList("Dallas", "Washington"), forecast.getHottestCities(sampleDoubleData));
  }

  @Test
  public void testGetHottestCityWithThreeHottestCities() {
    List<WeatherDetails> sampleTripleData = Arrays.asList(houstonData, austinData, dallasData, washingtonData, miamiData, floridaData, californiaData);
    assertEquals(Arrays.asList("Dallas", "Washington", "Florida"), forecast.getHottestCities(sampleTripleData));
  }

  @Test
  public void testGetColdestCity() {
    List<WeatherDetails> sampleData = Arrays.asList(houstonData, austinData, dallasData);
    assertEquals(Arrays.asList("Houston"), forecast.getColdestCities(sampleData));
  }

  @Test
  public void testGetColdestCityWithEmptyData() {
    assertEquals(new ArrayList<String>(),forecast.getColdestCities(new ArrayList<WeatherDetails>()));
  }

  @Test
  public void testGetColdestCityWhenOnlyOneCityPresent() {
    assertEquals(Arrays.asList("Austin"), forecast.getColdestCities(Arrays.asList(austinData)));
  }

  @Test
  public void testGetColdestCityWithTwoColdestCities() {
    List<WeatherDetails> sampleDoubleData = Arrays.asList(houstonData, austinData, dallasData, washingtonData, miamiData);
    assertEquals(Arrays.asList("Houston", "Miami"), forecast.getColdestCities(sampleDoubleData));
  }

  @Test
  public void testGetColdestCityWithThreeColdestCities() {
    List<WeatherDetails> sampleTripleData = Arrays.asList(houstonData, austinData, dallasData, washingtonData, miamiData, floridaData, californiaData);
    assertEquals(Arrays.asList("Houston", "Miami", "California"), forecast.getColdestCities(sampleTripleData));
  }

  @Test
  public void testGetAllWeatherDataForNoInputCities() {
    WeatherResults weatherResults = forecast.getWeatherData(new ArrayList<String>());
    assertEquals(0, weatherResults.getSortedResults().size() );
    assertEquals(0, weatherResults.getHottestCities().size() );
    assertEquals(0, weatherResults.getColdestCities().size() );
  }

  @Test
  public void testGetAllWeatherDataForThreeInputCities() {

    weatherService = Mockito.mock(WeatherService.class);
    when(weatherService.getData("Houston")).thenReturn(new WeatherDetails("Houston", 20.0, "cloudy"));
    when(weatherService.getData("Austin")).thenReturn(new WeatherDetails("Austin", 30.3, "sunny"));
    when(weatherService.getData("Dallas")).thenReturn(new WeatherDetails("Dallas", 40.2, "rainy"));
    
    forecast.setWeatherService(weatherService);

    WeatherResults weatherResults = forecast.getWeatherData(Arrays.asList("Houston","Austin","Dallas"));
    assertEquals(3, weatherResults.getSortedResults().size() );
    assertEquals(1, weatherResults.getHottestCities().size() );
    assertEquals(1, weatherResults.getColdestCities().size() );
  }

  @Test
  public void testGetAllWeatherDataWhenServiceHasError() {
    weatherService = Mockito.mock(WeatherService.class);
    when(weatherService.getData("Houston")).thenThrow(new RuntimeException("Error Processing"));

    forecast.setWeatherService(weatherService);

    WeatherResults weatherResults = forecast.getWeatherData(Arrays.asList("Houston"));
    WeatherDetails exceptionCityDetails = weatherResults.getExceptionCities().get(0);
    Exception ex = exceptionCityDetails.getExceptionError().get();

    assertEquals(0, weatherResults.getSortedResults().size() );
    assertEquals(0, weatherResults.getHottestCities().size() );
    assertEquals(0, weatherResults.getColdestCities().size() );
    assertEquals(1, weatherResults.getExceptionCities().size() );
    assertEquals("Houston", exceptionCityDetails.getCity() );
    assertEquals("Error Processing", ex.getMessage() );
  }

 @Test
  public void testGetAllWeatherDataWhenServiceHasErrorOnOneCity() {
    weatherService = Mockito.mock(WeatherService.class);
    when(weatherService.getData("Austin")).thenReturn(new WeatherDetails("Austin", 30.3, "sunny"));
    when(weatherService.getData("Houston")).thenThrow(new RuntimeException("Error Processing"));
    when(weatherService.getData("Dallas")).thenReturn(new WeatherDetails("Dallas", 40.2, "rainy"));

    forecast.setWeatherService(weatherService);

    WeatherResults weatherResults = forecast.getWeatherData(Arrays.asList("Austin", "Dallas", "Houston"));
    WeatherDetails exceptionCityDetails = weatherResults.getExceptionCities().get(0);
    Exception ex = exceptionCityDetails.getExceptionError().get();

    assertEquals(2, weatherResults.getSortedResults().size() );
    assertEquals(1, weatherResults.getHottestCities().size() );
    assertEquals(1, weatherResults.getColdestCities().size() );
    assertEquals(1, weatherResults.getExceptionCities().size() );
    assertEquals("Houston", exceptionCityDetails.getCity() );
    assertEquals("Error Processing", ex.getMessage() );
  }

}
