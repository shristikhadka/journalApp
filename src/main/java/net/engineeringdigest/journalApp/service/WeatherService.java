package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService
{
    private static final String apiKey="9acca4a4abffc51b828e6a46193794ed";

    private static final String API="https://api.weatherstack.com/current?access_key=API&query=CITY";

    @Autowired
    private RestTemplate restTemplate;//http response lai process garcha ra resposne dincha yo class le

    public WeatherResponse getWeather(String city){
        String finalAPI=API.replace("CITY",city).replace("API",apiKey);
        ResponseEntity<WeatherResponse> response=restTemplate.exchange(finalAPI, HttpMethod.GET,null, WeatherResponse.class);
        WeatherResponse body=response.getBody();
        return body;
    }
    //deserialize- process of converting json response to corresponding java object is called deserializing
    //serializarion-pojo to json

}
