package assign2.weatherforecast;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class WeatherWebServiceTest {

  private WeatherWebService webService;

  @Before
  public void init() {
    webService = new WeatherWebService();
  }

  @Test
  public void testInvokeWebServiceOnValidCity() throws IOException {
    String weatherJSON = webService.invokeWebService("Houston");
    assertTrue( weatherJSON.length() > 0 );
  }

  @Test
  public void testInvokeWebServiceOnEmptyCity() throws IOException {
    String weatherJSON = webService.invokeWebService("");
    assertTrue( weatherJSON.length() > 0 );
  }
    
  @Test
  public void testInvokeWebServiceWhenError() {
  	try {
  		WeatherWebService errorService = spy(new WeatherWebService());
  		when(errorService.obtainURLforAPI("Houston")).thenThrow(new IOException("Network Error"));
    	 
      String weatherJSON = errorService.invokeWebService("Houston");
    	fail("Expected IO Exception to be thrown");
    } catch (Exception ex) {
    	assertTrue(true);
	  }
  }

  @Test
  public void testParseJSONWithValidJSONString() throws IOException {
    String jsonString = "{\"coord\":{\"lon\":-95.37,\"lat\":29.76},\"sys\":{\"message\":0.0733,\"country\":\"United States of America\",\"sunrise\":1425990986,\"sunset\":1426033608},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":57.64,\"temp_min\":57.64,\"temp_max\":57.64,\"pressure\":1027.94,\"sea_level\":1029.52,\"grnd_level\":1027.94,\"humidity\":100},\"wind\":{\"speed\":9.96,\"deg\":338.501},\"clouds\":{\"all\":92},\"dt\":1426008943,\"id\":4699066,\"name\":\"Houston\",\"cod\":200}";
    WeatherDetails weatherDetails = webService.parseJSONString(jsonString);
    assertEquals( weatherDetails.getCity(), "Houston" );
    assertTrue( weatherDetails.getCondition().length() > 0);
  }

  @Test
  public void testParseJSONWithInvalidJSON() {
    try {
      WeatherDetails weatherDetails = webService.parseJSONString("");
      fail("Expected IO Exception to be thrown");
    } catch (Exception ex) {
      assertTrue(true);
    }
  }

  @Test
  public void testGetDataWithValidCity() throws IOException {
    WeatherWebService webServiceSpy = spy(new WeatherWebService());
    doReturn("{test}").when(webServiceSpy).invokeWebService("Austin");
    doReturn(new WeatherDetails("Austin",70.2,"rain")).when(webServiceSpy).parseJSONString("{test}");

    WeatherDetails weatherDetails = webServiceSpy.getData("Austin");
    verify(webServiceSpy, times(1)).invokeWebService("Austin");
    verify(webServiceSpy, times(1)).parseJSONString("{test}");
  }

  @Test
  public void testGetDataWithInvalidCity() {
    WeatherDetails weatherDetails = webService.getData("1");
    assertTrue( weatherDetails.isError() );
  }

}