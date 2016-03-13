package fr.upem.journal.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.upem.journal.newsfeed.WeatherFeed;
import fr.upem.journal.utils.RSSParser;


public class WeatherService {

    private String location;
    private String temperatureUnit;

    public WeatherService() {

    }

    /**
     * Gets the current location
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the temperature unit
     * @return the temperature unit
     */
    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    /**
     * Upload the weather informations with the specified location and temperature unit
     * @param location the location
     * @param temperatureUnit the temperature unit
     * @return an async task fetching the weather information
     */
    public AsyncTask<String, Void, WeatherFeed> refreshWeather(final String location, final String temperatureUnit) {
        this.location = location;
        this.temperatureUnit = temperatureUnit;

        return new AsyncTask<String, Void, WeatherFeed>() {

            @Override
            protected WeatherFeed doInBackground(String... params) {
                WeatherFeed weatherFeed = null;

                try {
                    weatherFeed = download(location, temperatureUnit);
                } catch (IOException e) {
                    Log.e("DEBUG WEATHER", "Error while fetching rss feed data");
                }

                return weatherFeed;
            }

            @Override
            protected void onPostExecute(WeatherFeed s) {
                super.onPostExecute(s);

            }
        }.execute(location, temperatureUnit);
    }



    private WeatherFeed download(String location, String temperatureUnit) throws IOException {
        InputStream inputStream = null;
        try {

            String uri = createURL(location, temperatureUnit);

            URL url = new URL(uri);

            Log.d("DEBUG WEATHER", "URL : " + uri);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(60000);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int response = connection.getResponseCode();
            Log.d("DEBUG WEATHER", "The response is: " + response);
            inputStream = connection.getInputStream();

            return RSSParser.parseWeather(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private String createURL(String location, String temperatureUnit) {
        String unit = null;
        switch (temperatureUnit) {
            case "celsius" :
                unit = "metric";
                break;
            case "farenheit" :
                unit = null;
                break;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("http://api.openweathermap.org/data/2.5/weather?")
                .append(location) // location
                .append("&mode=xml"); // xml mode
        if (unit != null) {
            sb.append("&units=").append(unit); // temperature unit
        }

        sb.append("&appid=").append("595bb54f8ec24acca91ed85467e03442");

        return sb.toString();

    }

}
