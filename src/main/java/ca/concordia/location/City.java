package ca.concordia.location;

public class City {
    private String name;
    private String country;
    private Temperature temp;

    public City(String name, String country, Temperature temp) {
        this.name = name;
        this.country = country;
        this.temp = temp;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Temperature getTemp() {
        return this.temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public String toSql(){
        String command = "Insert into City (name, country, temperature, metric) values ('"+this.name+"', '"+this.country+"', "+this.temp.getTemperature()+",'"+this.temp.getMetric()+"');";
        return command;
    }

}
