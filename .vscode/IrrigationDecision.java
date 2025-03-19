public class IrrigationDecision {
    private String decision;
    private String reason;
    private double irrigationAmount;
    
    public IrrigationDecision(String decision, String reason, double irrigationAmount) {
        this.decision = decision;
        this.reason = reason;
        this.irrigationAmount = irrigationAmount;
    }
    
    public String getDecision() {
        return decision;
    }
    
    public String getReason() {
        return reason;
    }
    
    public double getIrrigationAmount() {
        return irrigationAmount;
    }
}