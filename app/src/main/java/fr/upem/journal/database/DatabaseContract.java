package fr.upem.journal.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String FOREIGN_KEY = " FOREIGN KEY";
    private static final String REFERENCES = " REFERENCES ";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private DatabaseContract() {
    }

    /**
     * Class representing the news-feed table in the database
     */
    static class NewsFeed implements BaseColumns {
        static final String TABLE_NAME = "news_feed";
        static final String COLUMN_NAME_LABEL = "label";
        static final String COLUMN_NAME_LINK = "link";
        static final String COLUMN_NAME_CATEGORY = "category";

        static final String CREATE_TABLE_NEWS_FEED = CREATE_TABLE + TABLE_NAME + " (" +
                COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_LABEL + TEXT_TYPE + COMMA_SEP +
                PRIMARY_KEY + " (" + COLUMN_NAME_LINK + COMMA_SEP + COLUMN_NAME_CATEGORY + ") " + COMMA_SEP +
                FOREIGN_KEY + " (" + COLUMN_NAME_CATEGORY + ") " + REFERENCES + NewsCategory.TABLE_NAME + "(" +
                NewsCategory.COLUMN_NAME_TITLE + "))";

        static final String DROP_TABLE_NEWS_FEED = DROP_TABLE + TABLE_NAME;
    }

    /**
     * Class representing the news-category table in the database
     */
    static class NewsCategory implements BaseColumns {
        static final String TABLE_NAME = "news_category";
        static final String COLUMN_NAME_TITLE = "title";

        static final String CREATE_TABLE_NEWS_CATEGORY = CREATE_TABLE + TABLE_NAME + " (" +
                COLUMN_NAME_TITLE + TEXT_TYPE + PRIMARY_KEY + " )";

        static final String DROP_TABLE_NEWS_CATEGORY = DROP_TABLE + TABLE_NAME;
    }
}