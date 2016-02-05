package fr.upem.journal.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.upem.journal.newsfeed.NewsFeedItem;
import fr.upem.journal.newsfeed.NewsFeed;
import fr.upem.journal.utils.RSSParser;

public class FetchRSSFeedTask extends AsyncTask<NewsFeed, Integer, List<NewsFeedItem>> {

    public FetchRSSFeedTask() {
    }

    @Override
    protected List<NewsFeedItem> doInBackground(NewsFeed... newsFeeds) {
        List<NewsFeedItem> items = new ArrayList<>();
        for (NewsFeed newsFeed : newsFeeds) {
            try {
                items.addAll(download(newsFeed));
            } catch (IOException e) {
                Log.e("DOWNLOAD", "Error while fetching rss feed data");
            }
        }
        return items;
    }

    private List<NewsFeedItem> download(NewsFeed newsFeed) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(newsFeed.getLink());
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

            return RSSParser.parse(inputStream, newsFeed.getLabel());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
