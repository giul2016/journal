package fr.upem.journal.newsfeed;




public class WeatherFeed {
    private final String country;
    private final String city;
    private final String date;
    private final String temperature;
    private final String temperatureUnit;
    private final String skyState;
    private final String maxTemperature;
    private final String minTemperature;
    private final String humidity;
    private final String pressure;

    public WeatherFeed(String country, String city, String date, String temperature, String temperatureUnit, String skyState, String maxTemperature, String minTemperature, String humidity, String pressure) {
        this.country = country;
        this.city = city;
        this.date = date.replace('T', ' ');
        this.temperature = temperature;
        this.temperatureUnit = temperatureUnit;
        this.skyState = skyState;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.humidity = humidity;
        this.pressure = pressure;
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

    public String getDate() {
        return date;
    }

    public String getHumidity() {
        return humidity + "%";
    }

    public String getMaxTemperature() {
        return maxTemperature + "°c";
    }

    public String getMinTemperature() {
        return minTemperature + "°c";
    }

    public String getPressure() {
        return pressure + "hPa";
    }

    public String getSkyState() {
        return skyState;
    }

    public String getTemperatureUnit() {
        return "°C";
    }
}
