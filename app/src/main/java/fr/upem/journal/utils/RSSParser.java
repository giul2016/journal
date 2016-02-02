package fr.upem.journal.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import fr.upem.journal.Item;


public class RSSParser {

    private static XmlPullParser parser = Xml.newPullParser();
    private static final String UTF8 = "UTF-8";

    public static List<Item> parse(InputStream rssInputStream, String source) {
        ArrayList<Item> items = new ArrayList<>();

        System.out.println("hello");
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(rssInputStream, UTF8);
        } catch (XmlPullParserException e) {
            return items;
        }

        System.out.println("yeah");

        int eventType;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            eventType = XmlPullParser.END_DOCUMENT;
        }

        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();

                System.out.println(name);
                try {
                    if (name.equals("title")) {
                        title = parser.nextText();
                    } else if (name.equals("description")) {
                        description = parser.nextText();
                    } else if (name.equals("link")) {
                        link = parser.nextText();
                    } else if (name.equals("pubDate")) {
                        pubDate = parser.nextText();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    try {
                        Item item = new Item(title, description, link, DateParser.parse(pubDate), source);
                        items.add(item);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    title = null;
                    description = null;
                    link = null;
                    pubDate = null;
                }
            }

            try {
                eventType = parser.next();
            } catch (XmlPullParserException e) {
                eventType = XmlPullParser.END_DOCUMENT;
            } catch (IOException e) {
                eventType = XmlPullParser.END_DOCUMENT;
            }
        }

        return items;
    }
}
