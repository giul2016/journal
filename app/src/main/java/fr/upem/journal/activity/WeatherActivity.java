package fr.upem.journal.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fr.upem.journal.R;
import fr.upem.journal.newsfeed.WeatherFeed;
import fr.upem.journal.service.LocationService;
import fr.upem.journal.service.WeatherService;


public class WeatherActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION_ACCESS = 123;
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
            currentWeather = service.refreshWeather(getCurrentLocation(), "celsius").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        } catch (ExecutionException | IOException e) {
            e.printStackTrace();
            return;
        }

        System.err.println("currentWeather : " + currentWeather);
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

    /**
     * Updates every widget from the activity
     * @param country
     * @param city
     * @param date
     * @param temperature
     * @param temperatureUnit
     * @param skyState
     * @param maxTemperature
     * @param minTemperature
     * @param humidity
     * @param pressure
     */
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

    /**
     * Updates every widgets from the activity
     * @param weatherFeed
     */
    public void updateDisplay(WeatherFeed weatherFeed) {
        this.cityTextView.setText(weatherFeed.getCity());
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
        this.conditions.put("overcast clouds", new MorningEveningImages(R.drawable.overcast, R.drawable.overcast));
        this.conditions.put("broken clouds", new MorningEveningImages(R.drawable.overcast, R.drawable.heavy_cloudy_night));
        this.conditions.put("shower rain", new MorningEveningImages(R.drawable.heavy_rain, R.drawable.heavy_rain));
        this.conditions.put("rain", new MorningEveningImages(R.drawable.rain_sun, R.drawable.night_rain));
        this.conditions.put("thunderstorm", new MorningEveningImages(R.drawable.rain_thunder, R.drawable.night_rain_thunder));
        this.conditions.put("snow", new MorningEveningImages(R.drawable.snow_sun, R.drawable.snow_night));
        this.conditions.put("mist", new MorningEveningImages(R.drawable.foggy, R.drawable.fog_night));
    }

    private void searchConditionImage() {
        if (this.skyStateTextView.getText() == null) {
            Log.d("DEBUG LOCATION", "get image");
            return;
        }
        Log.d("DEBUG LOCATION", "Condition : " + this.skyStateTextView.getText());
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

        public int getImage() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date currentTimeDate = new Date();
            Date afternoonDate = null;
            Date midnightDate = null;

            int currentTime;
            int evening;
            int midnight;

            try {

                afternoonDate = sdf.parse("20:00:00");
                midnightDate = sdf.parse("00:00:00");

                currentTime = (int) (currentTimeDate.getTime() % (24 * 60 * 60 * 1000L));
                evening = (int) (afternoonDate.getTime() % (24 * 60 * 60 * 1000L));
                midnight = (int) (midnightDate.getTime() % (24 * 60 * 60 * 1000L));

            } catch (ParseException e) {
                return -1;
            }

            if (currentTime < evening && currentTime > midnight) {
                return this.morning;
            }

            return this.evening;
        }
    }

    private String getCurrentLocation() throws IOException {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationService locationService = new LocationService();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DEBUG LOCATION", "getCurrentLocation");

            ActivityCompat.requestPermissions( this,
                    new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    MY_PERMISSIONS_REQUEST_LOCATION_ACCESS);
        }

        Location loc = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 8000, 10, locationService);
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        String location = null;
        if (loc != null) {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses.size() > 0)
                location = "q=" + addresses.get(0).getLocality();
        }
        else {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(locationService.getLatitude(), locationService.getLongitude(), 1);
            if (addresses.size() > 0)
                location = "q=" + addresses.get(0).getLocality();
        }
        return location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // yes
                    Log.d("DEBUG LOCATION", "YES IT WORKED");


                }
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // yes
                    Log.d("DEBUG LOCATION", "YES IT WORKED VERY WELL");


                } else {
                    // no
                    Log.d("DEBUG LOCATION", "HO NO");
                }

                break;
        }
    }

}

