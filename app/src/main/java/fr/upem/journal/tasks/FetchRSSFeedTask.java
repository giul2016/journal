package fr.upem.journal.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchRSSFeedTask extends AsyncTask<String, Integer, String> {

    public FetchRSSFeedTask() {
    }

    @Override
    protected String doInBackground(String... urlStrings) {
        try {
            return download(urlStrings[0]);
        } catch (IOException e) {
            return "Error while fetching rss feed data";
        }
    }

    private String download(String urlString) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(60000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int response = connection.getResponseCode();
            Log.d("DEBUG", "The response is: " + response);
            inputStream = connection.getInputStream();

            int bufferSize = 1024;
            byte[] byteBuffer = new byte[bufferSize];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read(byteBuffer, 0, bufferSize)) != -1) {
                outputStream.write(byteBuffer, 0, read);
            }

            System.out.println(outputStream.toString("UTF-8"));

            connection.disconnect();

            return outputStream.toString("UTF-8");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
