public class SensorReading {
    private String timestamp;
    private double temperature;
    private double soilMoisture;
    private double humidity;
    private double rainfall;
    private double windSpeed;
    private double lightIntensity;
    private String location;
    
    public SensorReading(String timestamp, double temperature, double soilMoisture, 
                         double humidity, double rainfall, double windSpeed, 
                         double lightIntensity, String location) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.soilMoisture = soilMoisture;
        this.humidity = humidity;
        this.rainfall = rainfall;
        this.windSpeed = windSpeed;
        this.lightIntensity = lightIntensity;
        this.location = location;
    }
    
    // Getters
    public String getTimestamp() {
        return timestamp;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public double getSoilMoisture() {
        return soilMoisture;
    }
    
    public double getHumidity() {
        return humidity;
    }
    
    public double getRainfall() {
        return rainfall;
    }
    
    public double getWindSpeed() {
        return windSpeed;
    }
    
    public double getLightIntensity() {
        return lightIntensity;
    }
    
    public String getLocation() {
        return location;
    }
    
    @Override
    public String toString() {
        return String.format("Reading at %s: %.1f°C, %.1f%% moisture", 
                             timestamp, temperature, soilMoisture);
    }
}