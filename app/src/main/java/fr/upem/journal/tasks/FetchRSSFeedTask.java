package fr.upem.journal.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.upem.journal.Item;
import fr.upem.journal.utils.RSSParser;

public class FetchRSSFeedTask extends AsyncTask<String, Integer, List<Item>> {

    public FetchRSSFeedTask() {
    }

    @Override
    protected List<Item> doInBackground(String... urlStrings) {
        List<Item> items = new ArrayList<>();
        for(String urlString : urlStrings) {
            try {
                items.addAll(download(urlString));
            } catch (IOException e) {
                Log.e("DOWNLOAD", "Error while fetching rss feed data");
            }
        }
        return items;
    }

    private List<Item> download(String urlString) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(60000);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int response = connection.getResponseCode();
            Log.d("DEBUG", "The response is: " + response);
            inputStream = connection.getInputStream();

            //connection.disconnect();

            return RSSParser.parse(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
