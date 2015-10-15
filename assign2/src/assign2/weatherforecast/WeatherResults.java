package assign2.weatherforecast;

import java.util.List;
    
public class WeatherResults {

    public final List<WeatherDetails> sortedResults;
    public final List<String> hottestCities;
    public final List<String> coldestCities;
    public final List<WeatherDetails> exceptionResults;

    WeatherResults(List<WeatherDetails> theSortedResults, List<String> theHottestCities, List<String> theColdestCities, List<WeatherDetails> theExceptions) {
        sortedResults = theSortedResults;
        hottestCities = theHottestCities;
        coldestCities = theColdestCities;
        exceptionResults = theExceptions;
    }

    public List<WeatherDetails> getSortedResults() {
        return sortedResults;
    }

    public List<String> getHottestCities() {
        return hottestCities;
    }

    public List<String> getColdestCities() {
        return coldestCities;
    }

    public List<WeatherDetails> getExceptionCities() {
        return exceptionResults;
    }
}