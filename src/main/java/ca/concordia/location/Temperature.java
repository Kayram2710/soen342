package ca.concordia.location;

public class Temperature {
    private double temperature;
    private String metric;

    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getMetric() {
        return this.metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Temperature(double temperature, String metric) {
        this.temperature = temperature;
        this.metric = metric;
    }

}
