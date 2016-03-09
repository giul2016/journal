package fr.upem.journal.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.upem.journal.newsfeed.WeatherFeed;
import fr.upem.journal.utils.RSSParser;


public class FetchRSSWeatherFeedTask extends AsyncTask<WeatherFeed, Integer, List<WeatherFeed>> {


    private final String url = "http://api.openweathermap.org/data/2.5/weather?q=Paris,fr&mode=xml&units=metric";
    private final String pidStr = "&appid=";
    private final String key = "595bb54f8ec24acca91ed85467e03442";


    @Override
    protected List<WeatherFeed> doInBackground(WeatherFeed... weatherFeeds) {
        List<WeatherFeed> wfs = new ArrayList<>();
        try {
            wfs.add(download());
        } catch (IOException e) {
            Log.e("DOWNLOAD", "Error while fetching rss feed data");
        }
        return wfs;
    }


    private WeatherFeed download() throws IOException {
        InputStream inputStream = null;
        try {

            URL url = new URL(this.url + pidStr + key);

            Log.d("DEBUG WEATHER", "URL : " + url.getPath());


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

}
