package fr.upem.journal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import fr.upem.journal.newsfeed.NewsCategory;
import fr.upem.journal.newsfeed.NewsFeed;

/**
 * Class containing helper methods to access the data present in the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Journal.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.NewsCategory.CREATE_TABLE_NEWS_CATEGORY);
        db.execSQL(DatabaseContract.NewsFeed.CREATE_TABLE_NEWS_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.NewsCategory.DROP_TABLE_NEWS_CATEGORY);
        db.execSQL(DatabaseContract.NewsFeed.DROP_TABLE_NEWS_FEED);
        onCreate(db);
    }

    /**
     * Insert a news feed into the database
     * @param newsFeed the news feed to add to the database
     * @param category the title of the category
     * @return true if the news feed was successfully inserted, false otherwise
     */
    public boolean insertNewsFeed(NewsFeed newsFeed, String category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NewsFeed.COLUMN_NAME_LABEL, newsFeed.getLabel());
        values.put(DatabaseContract.NewsFeed.COLUMN_NAME_LINK, newsFeed.getLink());
        values.put(DatabaseContract.NewsFeed.COLUMN_NAME_CATEGORY, category);
        try {
            db.insertWithOnConflict(DatabaseContract.NewsFeed.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_ABORT);
        } catch (SQLiteConstraintException e) {
            Log.d("DB", "Conflict on insertion");
            return false;
        }

        return true;
    }

    /**
     * Remove a news feed from the database
     * @param label the label of the news feed to remove
     * @return true if the news feed was successfully removed, false otherwise
     */
    public boolean removeNewsFeed(String label) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete(DatabaseContract.NewsFeed.TABLE_NAME, "label = ?", new String[]{label});
        } catch (SQLiteConstraintException e) {
            Log.d("DB", "Conflict on deletion");
            return false;
        }

        return true;
    }

    /**
     * Insert a news category into the database
     * @param newsCategory the news category to add to the database
     * @return true if the news category was successfully inserted, false otherwise
     */
    public boolean insertNewsCategory(NewsCategory newsCategory) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NewsCategory.COLUMN_NAME_TITLE, newsCategory.getTitle());

        try {
            db.insertWithOnConflict(DatabaseContract.NewsCategory.TABLE_NAME, null, values, SQLiteDatabase
                    .CONFLICT_ABORT);
        } catch (SQLiteConstraintException e) {
            Log.d("DB", "Conflict on insertion");
            return false;
        }

        for (NewsFeed newsFeed : newsCategory.getFeeds()) {
            insertNewsFeed(newsFeed, newsCategory.getTitle());
        }

        return true;
    }

    /**
     * Remove a news category from the database
     * @param newsCategory the news category to remove
     * @return true if the news category was successfully removed, false otherwise
     */
    public boolean removeNewsCategory(NewsCategory newsCategory) {
        SQLiteDatabase db = getWritableDatabase();

        for (NewsFeed newsFeed : newsCategory.getFeeds()) {
            removeNewsFeed(newsFeed.getLabel());
        }

        try {
            db.delete(DatabaseContract.NewsCategory.TABLE_NAME, "title = ?", new String[]{newsCategory.getTitle()});
        } catch (SQLiteConstraintException e) {
            Log.d("DB", "Conflict on deletion");
            return false;
        }

        return true;
    }

    /**
     * Select all the news categories present in the database
     * @return an ArrayList containing all the categories
     */
    public ArrayList<NewsCategory> selectNewsCategories() {
        String query = "SELECT * FROM " + DatabaseContract.NewsCategory.TABLE_NAME;

        ArrayList<NewsCategory> newsCategories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(DatabaseContract.NewsCategory.COLUMN_NAME_TITLE));
                NewsCategory newsCategory = new NewsCategory(title);
                ArrayList<NewsFeed> newsFeeds = selectNewsFeedsByCategory(newsCategory.getTitle());
                for (NewsFeed newsFeed : newsFeeds) {
                    newsCategory.addFeed(newsFeed);
                }
                newsCategories.add(newsCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return newsCategories;
    }

    /**
     * Select all the news feeds of a specific category
     * @param categoryTitle the title of the category
     * @return an ArrayList containing all the feeds of a category
     */
    public ArrayList<NewsFeed> selectNewsFeedsByCategory(String categoryTitle) {
        String query = "SELECT * FROM " + DatabaseContract.NewsFeed.TABLE_NAME + " WHERE " + DatabaseContract
                .NewsFeed.COLUMN_NAME_CATEGORY + " = \"" + categoryTitle + "\"";

        ArrayList<NewsFeed> newsFeeds = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String label = cursor.getString(cursor.getColumnIndex(DatabaseContract.NewsFeed.COLUMN_NAME_LABEL));
                String link = cursor.getString(cursor.getColumnIndex(DatabaseContract.NewsFeed.COLUMN_NAME_LINK));
                NewsFeed newsFeed = new NewsFeed(label, link);
                newsFeeds.add(newsFeed);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return newsFeeds;
    }

    /**
     * Initialize the database on the first use of the app
     */
    public void initialData() {

        NewsCategory general = new NewsCategory("General");
        general.addFeed(new NewsFeed("Le Monde", "http://www.lemonde.fr/m-actu/rss_full.xml"));

        NewsCategory international = new NewsCategory("International");
        international.addFeed(new NewsFeed("Le Monde - Japon", "http://www.lemonde.fr/japon/rss_full.xml"));
        international.addFeed(new NewsFeed("Le Monde - Europe", "http://www.lemonde.fr/europe/rss_full.xml"));
        international.addFeed(new NewsFeed("Le Monde - Amériques", "http://www.lemonde.fr/ameriques/rss_full.xml"));
        international.addFeed(new NewsFeed("Le Monde - Asie-Pacifique", "http://www.lemonde" +
                ".fr/asie-pacifique/rss_full.xml"));

        NewsCategory sport = new NewsCategory("Sport");
        sport.addFeed(new NewsFeed("Le Monde - Sport", "http://www.lemonde.fr/sport/rss_full.xml"));

        NewsCategory technology = new NewsCategory("Technology");
        technology.addFeed(new NewsFeed("Le Monde - Technologies", "http://www.lemonde.fr/technologies/rss_full.xml"));

        NewsCategory gaming = new NewsCategory("Gaming");
        gaming.addFeed(new NewsFeed("Gamekult", "http://www.gamekult.com/feeds/actu.html"));
        gaming.addFeed(new NewsFeed("Le Monde - Jeux Vidéo", "http://www.lemonde.fr/jeux-video/rss_full.xml"));

        NewsCategory culture = new NewsCategory("Culture");
        culture.addFeed(new NewsFeed("Le Monde - Culture", "http://www.lemonde.fr/culture/rss_full.xml"));

        insertNewsCategory(general);
        insertNewsCategory(international);
        insertNewsCategory(sport);
        insertNewsCategory(technology);
        insertNewsCategory(gaming);
        insertNewsCategory(culture);
    }
}
