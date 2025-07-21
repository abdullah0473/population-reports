package com.population.model;

public class LanguageStat {
    private final String language;
    private final long speakers;
    private final double worldPercentage;

    public LanguageStat(String language, long speakers, double worldPercentage) {
        this.language = language;
        this.speakers = speakers;
        this.worldPercentage = worldPercentage;
    }

    public String language()        { return language; }
    public long speakers()          { return speakers; }
    public double worldPercentage() { return worldPercentage; }
}