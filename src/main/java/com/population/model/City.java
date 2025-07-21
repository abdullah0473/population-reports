package com.population.model;

public class City {
    private final String name;
    private final String country;
    private final String district;
    private final int population;

    public City(String name, String country, String district, int population) {
        this.name = name;
        this.country = country;
        this.district = district;
        this.population = population;
    }

    public String name()      { return name; }
    public String country()   { return country; }
    public String district()  { return district; }
    public int population()   { return population; }

    @Override
    public String toString() {
        return String.format("%-30s %-20s %-20s %10d",
                name, country, district, population);
    }
}