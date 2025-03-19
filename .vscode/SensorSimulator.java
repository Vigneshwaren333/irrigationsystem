public class SensorSimulator {
    // Thresholds for irrigation decisions
    private static final double MOISTURE_LOW_THRESHOLD = 30.0;
    private static final double MOISTURE_MEDIUM_THRESHOLD = 50.0;
    private static final double MOISTURE_HIGH_THRESHOLD = 70.0;
    
    // Temperature thresholds
    private static final double TEMP_LOW_THRESHOLD = 15.0;
    private static final double TEMP_HIGH_THRESHOLD = 30.0;
    
    // Rainfall thresholds
    private static final double RAINFALL_THRESHOLD = 5.0;
    
    public IrrigationDecision makeIrrigationDecision(SensorReading reading) {
        String decision;
        String reason;
        double irrigationAmount = 0.0;
        
        // Check recent rainfall
        if (reading.getRainfall() > RAINFALL_THRESHOLD) {
            decision = "Do Not Irrigate";
            reason = String.format("Recent rainfall of %.1fmm exceeds threshold (%.1fmm)", 
                                  reading.getRainfall(), RAINFALL_THRESHOLD);
            return new IrrigationDecision(decision, reason, irrigationAmount);
        }
        
        // Check soil moisture levels
        if (reading.getSoilMoisture() < MOISTURE_LOW_THRESHOLD) {
            decision = "Irrigate Immediately";
            reason = String.format("Soil moisture critically low at %.1f%% (below %.1f%%)", 
                                  reading.getSoilMoisture(), MOISTURE_LOW_THRESHOLD);
            
            // Calculate irrigation amount based on temperature
            if (reading.getTemperature() > TEMP_HIGH_THRESHOLD) {
                irrigationAmount = 80.0; // Heavy irrigation for high temperatures
                reason += " and high temperature";
            } else if (reading.getTemperature() < TEMP_LOW_THRESHOLD) {
                irrigationAmount = 50.0; // Medium irrigation for low temperatures
                reason += " with low temperature";
            } else {
                irrigationAmount = 65.0; // Normal irrigation
            }
        } 
        else if (reading.getSoilMoisture() < MOISTURE_MEDIUM_THRESHOLD) {
            decision = "Irrigate Soon";
            reason = String.format("Soil moisture low at %.1f%% (below %.1f%%)", 
                                  reading.getSoilMoisture(), MOISTURE_MEDIUM_THRESHOLD);
            
            // Calculate irrigation amount based on temperature
            if (reading.getTemperature() > TEMP_HIGH_THRESHOLD) {
                irrigationAmount = 60.0;
                reason += " with high temperature";
            } else {
                irrigationAmount = 40.0;
            }
        }
        else if (reading.getSoilMoisture() < MOISTURE_HIGH_THRESHOLD) {
            decision = "Monitor";
            reason = String.format("Soil moisture adequate at %.1f%% (between %.1f%% and %.1f%%)", 
                                  reading.getSoilMoisture(), MOISTURE_MEDIUM_THRESHOLD, MOISTURE_HIGH_THRESHOLD);
            irrigationAmount = 0.0;
        }
        else {
            decision = "Do Not Irrigate";
            reason = String.format("Soil moisture high at %.1f%% (above %.1f%%)", 
                                  reading.getSoilMoisture(), MOISTURE_HIGH_THRESHOLD);
            irrigationAmount = 0.0;
        }
        
        // Adjust for high temperature
        if (reading.getTemperature() > TEMP_HIGH_THRESHOLD && irrigationAmount > 0) {
            irrigationAmount *= 1.2; // Increase irrigation by 20% for high temperatures
            irrigationAmount = Math.min(irrigationAmount, 100.0); // Cap at 100%
        }
        
        return new IrrigationDecision(decision, reason, irrigationAmount);
    }
}