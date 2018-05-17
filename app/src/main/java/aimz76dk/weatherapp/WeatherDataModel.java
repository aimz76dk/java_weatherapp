package aimz76dk.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDataModel {

    // Variables that hold weather information
    private String mDay;
    private String mTemperature;
    private String mCity;
    private String mIconName;
    private int mCondition;


    // Create singular WeatherDataModel from JSON and return it
    public static WeatherDataModel fromJson(JSONObject response) {

        // JSON parsing is risky business - Need to surround code with a try-catch
        try {
            WeatherDataModel weatherData = new WeatherDataModel();

            weatherData.mCity = response.getString("name");
            weatherData.mCondition = response.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mIconName = updateWeatherIcon(weatherData.mCondition);

            double tempResult = response.getJSONObject("main").getDouble("temp");
            int roundedValue = (int) Math.rint(tempResult);

            weatherData.mTemperature = Integer.toString(roundedValue);

            return weatherData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create a list of WeatherDataModel objects from JSON and return it
    public static List<WeatherDataModel> forecastFromJson(JSONObject response) {

        // List of days
        List<WeatherDataModel> weatherData = new ArrayList<>();

        for (int i = 0; i <= 3; i++) {

            // JSON parsing is risky business - Need to surround code with a try-catch
            try {
                WeatherDataModel tmpWeatherData =  new WeatherDataModel();

                // Get from JSON and set date with format
                int unixDay1 = response.getJSONArray("list").getJSONObject(i).getInt("dt");
                // * 1000 is to convert seconds to milliseconds for Date object
                Date date = new Date(unixDay1 * 1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
                tmpWeatherData.mDay = sdf.format(date);

                // Get from JSON and set temperature
                double tempResult = response.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getDouble("day");
                int roundedValue = (int) Math.rint(tempResult);
                tmpWeatherData.mTemperature = Integer.toString(roundedValue);

                // Get from JSON and set condition using updateWeatherIcon method
                tmpWeatherData.mCondition = response.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                tmpWeatherData.mIconName = updateWeatherIcon(tmpWeatherData.mCondition);

                weatherData.add(i, tmpWeatherData);

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        } return weatherData;
    }


    // Get the weather image name from OpenWeatherMap's condition (marked by a number code)
    public static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // Getter methods for temperature, city, and icon name:
    public String getTemperature() {
        return mTemperature + "°";
    }

    public String getCity() {
        return mCity;
    }

    public String getIconName() {
        return mIconName;
    }

    public String getDay() { return mDay; }
}
