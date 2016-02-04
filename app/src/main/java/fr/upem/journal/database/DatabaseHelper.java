package fr.upem.journal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.upem.journal.NewsFeed;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Journal.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.NewsFeed.CREATE_TABLE_NEWS_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.NewsFeed.DROP_TABLE_NEWS_FEED);
        onCreate(db);
    }

    private void insertNewsFeed(NewsFeed newsFeed, String category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NewsFeed.COLUMN_NAME_LABEL, newsFeed.getLabel());
        values.put(DatabaseContract.NewsFeed.COLUMN_NAME_LINK, newsFeed.getLink());
        values.put(DatabaseContract.NewsFeed.COLUMN_NAME_CATEGORY, category);

        db.insert(DatabaseContract.NewsFeed.TABLE_NAME, null, values);
    }

    public Map<String, ArrayList<NewsFeed>> selectAllNewsFeeds() {
        String query = "SELECT * FROM " + DatabaseContract.NewsFeed.TABLE_NAME;

        HashMap<String, ArrayList<NewsFeed>> newsFeeds = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String label = cursor.getString(cursor.getColumnIndex(DatabaseContract.NewsFeed.COLUMN_NAME_LABEL));
                String link = cursor.getString(cursor.getColumnIndex(DatabaseContract.NewsFeed.COLUMN_NAME_LINK));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseContract.NewsFeed
                        .COLUMN_NAME_CATEGORY));

                NewsFeed newsFeed = new NewsFeed(label, link);
                if (newsFeeds.containsKey(category)) {
                    ArrayList<NewsFeed> newsFeedsList = newsFeeds.get(category);
                    newsFeedsList.add(newsFeed);
                } else {
                    ArrayList<NewsFeed> newsFeedsList = new ArrayList<>();
                    newsFeedsList.add(newsFeed);
                    newsFeeds.put(category, newsFeedsList);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        return newsFeeds;
    }

    public void initialData() {
        insertNewsFeed(new NewsFeed("Le Monde", "http://www.lemonde.fr/m-actu/rss_full.xml"), "General");

        insertNewsFeed(new NewsFeed("Le Monde - Japon", "http://www.lemonde.fr/japon/rss_full.xml"), "International");
        insertNewsFeed(new NewsFeed("Le Monde - Europe", "http://www.lemonde.fr/europe/rss_full.xml"), "International");
        insertNewsFeed(new NewsFeed("Le Monde - Amériques", "http://www.lemonde.fr/ameriques/rss_full.xml"),
                "International");
        insertNewsFeed(new NewsFeed("Le Monde - Asie-Pacifique", "http://www.lemonde.fr/asie-pacifique/rss_full.xml"),
                "International");

        insertNewsFeed(new NewsFeed("Le Monde - Sport", "http://www.lemonde.fr/sport/rss_full.xml"), "Sport");

        insertNewsFeed(new NewsFeed("Le Monde - Technologies", "http://www.lemonde.fr/technologies/rss_full.xml"),
                "Technology");

        insertNewsFeed(new NewsFeed("Gamekult", "http://www.gamekult.com/feeds/actu.html"), "Gaming");
        insertNewsFeed(new NewsFeed("Le Monde - Jeux Vidéo", "http://www.lemonde.fr/jeux-video/rss_full.xml"),
                "Gaming");

        insertNewsFeed(new NewsFeed("Le Monde - Culture", "http://www.lemonde.fr/culture/rss_full.xml"), "Culture");
    }
}
