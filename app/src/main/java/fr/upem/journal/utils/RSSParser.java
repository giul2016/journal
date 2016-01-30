package fr.upem.journal.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.upem.journal.Item;


public class RSSParser {

    private static XmlPullParser parser = Xml.newPullParser();
    private static final String UTF8 = "UTF-8";

    public static List<Item> parse(InputStream rssInputStream) {
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
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    Item item = new Item(title, description, link);
                    System.out.println(item);
                    items.add(item);
                    title = null;
                    description = null;
                    link = null;
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
