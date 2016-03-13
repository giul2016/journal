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
        this.date = date;
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

    /**
     * Gets the country
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the temperature
     * @return the temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Gets the last updating date
     * @return the last updating date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the humidity
     * @return the current humidity
     */
    public String getHumidity() {
        return humidity + "%";
    }

    /**
     * Gets the maximum temperature
     * @return the maximum temperature
     */
    public String getMaxTemperature() {
        return maxTemperature + "°c";
    }

    /**
     * Gets the minimum temperature
     * @return the minimum temperature
     */
    public String getMinTemperature() {
        return minTemperature + "°c";
    }

    /**
     * Gets the current pressure plus it unit
     * @return the current pressure
     */
    public String getPressure() {
        return pressure + "hPa";
    }

    /**
     * Gets the current condition
     * @return the current condition
     */
    public String getSkyState() {
        return skyState;
    }

    /**
     * Gets the temperature unit
     * @return the current temperature unit
     */
    public String getTemperatureUnit() {
        return "°C";
    }
}
