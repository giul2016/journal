package fr.upem.journal.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT";

    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private DatabaseContract() {
    }

    static class NewsFeed implements BaseColumns {
        static final String TABLE_NAME = "news_feed";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_LABEL = "label";
        static final String COLUMN_NAME_LINK = "link";
        static final String COLUMN_NAME_CATEGORY = "category";

        static final String CREATE_TABLE_NEWS_FEED = CREATE_TABLE + TABLE_NAME + " (" +
                COLUMN_NAME_ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
                COLUMN_NAME_LABEL + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CATEGORY + TEXT_TYPE + " )";

        static final String DROP_TABLE_NEWS_FEED = DROP_TABLE + TABLE_NAME;
    }
}