package fr.upem.journal.newsfeed;

import android.os.Parcel;
import android.os.Parcelable;



public class WeatherFeed implements Parcelable {
    private final String country;
    private final String city;
    private final String temperature;
    private final String clouds;
    private final String humidity;


    public WeatherFeed(String country, String city, String temperature, String clouds, String humidity) {
        this.country = country;
        this.city = city;
        this.temperature = temperature;
        this.clouds = clouds;
        this.humidity = humidity;
    }

    public static final Creator<WeatherFeed> CREATOR = new Creator<WeatherFeed>() {
        @Override
        public WeatherFeed createFromParcel(Parcel in) {
            String country = in.readString();
            String city = in.readString();
            String temperature = in.readString();
            String clouds = in.readString();
            String humidity = in.readString();
            return new WeatherFeed(country, city, temperature, clouds, humidity);
        }

        @Override
        public WeatherFeed[] newArray(int size) {
            return new WeatherFeed[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(temperature);
        dest.writeSerializable(clouds);
        dest.writeString(humidity);
    }

    @Override
    public String toString() {
        return country + " " + city + " " + temperature;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

}
