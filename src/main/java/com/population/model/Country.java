package com.population.model;

public class Country {
    private final String code;
    private final String name;
    private final String continent;
    private final String region;
    private final int population;
    private final int capitalId;

    public Country(String code, String name, String continent, String region,
                   int population, int capitalId) {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capitalId = capitalId;
    }

    public String code()       { return code; }
    public String name()       { return name; }
    public String continent()  { return continent; }
    public String region()     { return region; }
    public int population()    { return population; }
    public int capitalId()     { return capitalId; }

    @Override
    public String toString() {
        return String.format("%-5s %-40s %-15s %-25s %12d %8d",
                code, name, continent, region, population, capitalId);
    }
}