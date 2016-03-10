package fr.upem.journal.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.upem.journal.R;
import fr.upem.journal.newsfeed.WeatherFeed;
import fr.upem.journal.task.WeatherService;


public class WeatherActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private final String[] drawerItems = {"News", "Facebook", "Twitter", "Weather", "Settings"};


    //WeatherIcons
    private TextView cityTextView;
    private TextView dateTextView;
    private TextView temperatureTextView;
    private TextView unitTextView;
    private ImageView skyStateImageView;
    private TextView skyStateTextView;
    private TextView max;
    private TextView min;
    private TextView humidity;
    private TextView pressure;

    private WeatherService service;

    private final Map<String, MorningEveningImages> conditions = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initWidgets();
        service = new WeatherService();

        WeatherFeed currentWeather = null;
        try {
            currentWeather = service.refreshWeather("lat=48.85&lon=2.35", "celsius").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        updateDisplay(currentWeather);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        drawerList = (ListView) findViewById(R.id.leftDrawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(WeatherActivity.this, NewsFeedActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(WeatherActivity.this, FacebookActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(WeatherActivity.this, TwitterActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        // ALREADY SELECTED
                        /*intent = new Intent(WeatherActivity.this, WeatherActivity.class);
                        startActivity(intent);*/
                        break;
                    case 4:
                        intent = new Intent(WeatherActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.actionSettings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.actionEditCategories:
                Intent intentEdit = new Intent(this, EditCategoriesActivity.class);
                startActivity(intentEdit);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateDisplay(String country, String city, String date, String temperature, String temperatureUnit, String skyState, String maxTemperature, String minTemperature, String humidity, String pressure) {
        this.cityTextView.setText(city);
        this.dateTextView.setText(date);
        this.temperatureTextView.setText(temperature);
        this.unitTextView.setText(temperatureUnit);
        this.skyStateTextView.setText(skyState);
        this.max.setText(maxTemperature);
        this.min.setText(minTemperature);
        this.humidity.setText(humidity);
        this.pressure.setText(pressure);
        searchConditionImage();
    }

    public void updateDisplay(WeatherFeed weatherFeed) {
        /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateFFormat;
        String[] strings = weatherFeed.getDate().split("-");

        try {
            dateFFormat = sdf.format(sdf.parse(strings[0]));
        } catch (ParseException e) {
            return;
        }*/

        this.cityTextView.setText(weatherFeed.getCity());
        //this.dateTextView.setText(dateFFormat + ' '+ strings[1]);
        this.dateTextView.setText(weatherFeed.getDate());
        this.temperatureTextView.setText(weatherFeed.getTemperature());
        this.unitTextView.setText(weatherFeed.getTemperatureUnit());
        this.skyStateTextView.setText(weatherFeed.getSkyState());
        this.max.setText(weatherFeed.getMaxTemperature());
        this.min.setText(weatherFeed.getMinTemperature());
        this.humidity.setText(weatherFeed.getHumidity());
        this.pressure.setText(weatherFeed.getPressure());
        searchConditionImage();
    }

    private void initWidgets() {
        this.cityTextView = (TextView) findViewById(R.id.city);
        this.dateTextView = (TextView) findViewById(R.id.date);
        this.temperatureTextView = (TextView) findViewById(R.id.temperature);
        this.unitTextView = (TextView) findViewById(R.id.unit);
        this.skyStateImageView = (ImageView) findViewById(R.id.sky);
        this.skyStateTextView = (TextView) findViewById(R.id.sky_state);
        this.max = (TextView) findViewById(R.id.max_value);
        this.min = (TextView) findViewById(R.id.min_value);
        this.humidity = (TextView) findViewById(R.id.humidity_value);
        this.pressure = (TextView) findViewById(R.id.pressure_value);

        this.conditions.put("clear sky", new MorningEveningImages(R.drawable.sunny, R.drawable.clear_night));
        this.conditions.put("few clouds", new MorningEveningImages(R.drawable.partly_cloudy, R.drawable.cloudy_night));
        this.conditions.put("scattered clouds", new MorningEveningImages(R.drawable.overcast, R.drawable.overcast));
        this.conditions.put("scattered clouds", new MorningEveningImages(R.drawable.overcast, R.drawable.overcast));
        this.conditions.put("broken clouds", new MorningEveningImages(R.drawable.overcast, R.drawable.heavy_cloudy_night));
        this.conditions.put("shower rain", new MorningEveningImages(R.drawable.heavy_rain, R.drawable.heavy_rain));
        this.conditions.put("rain", new MorningEveningImages(R.drawable.rain_sun, R.drawable.night_rain));
        this.conditions.put("thunderstorm", new MorningEveningImages(R.drawable.rain_thunder, R.drawable.night_rain_thunder));
        this.conditions.put("snow", new MorningEveningImages(R.drawable.snow_sun, R.drawable.snow_night));
        this.conditions.put("mist", new MorningEveningImages(R.drawable.foggy, R.drawable.fog_night));
    }

    private void searchConditionImage() {
        int value = conditions.get(this.skyStateTextView.getText()).getImage();
        this.skyStateImageView.setImageResource(value);
    }


    private class MorningEveningImages {
        private final int morning;
        private final int evening;

        public MorningEveningImages(int morning, int evening) {
            this.morning = morning;
            this.evening = evening;
        }

        public int getEvening() {
            return evening;
        }

        public int getMorning() {
            return morning;
        }

        public int getImage() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date currentTime = null;
            Date afternoon = null;
            Date midnight = null;

            int t1;
            int t2;
            int t3;

            try {
                currentTime = sdf.parse(sdf.format(new Date()));
                afternoon = sdf.parse("20:00:00");
                midnight = sdf.parse("23:59:59");

                t1 = (int) (currentTime.getTime() % (24*60*60*1000L));
                t2 = (int) (afternoon.getTime() % (24*60*60*1000L));
                t3 = (int) (midnight.getTime() % (24*60*60*1000L));

                /*System.out.println("currentTime : " + currentTime);
                System.out.println("limitTime : " + afternoon);
                System.out.println("midnight : " + midnight);
                */
            } catch (ParseException e) {
                return -1;
            }

            if (t1 < t2 && t1 > t3) {
                return this.morning;
            }

            return this.evening;
}
    }
}

