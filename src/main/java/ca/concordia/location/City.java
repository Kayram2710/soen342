package ca.concordia.location;

public class City {
    private String name;
    private String country;
    private Temperature temp;

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

    public City(String name, String country, Temperature temp) {
        this.name = name;
        this.country = country;
        this.temp = temp;
    }

}
