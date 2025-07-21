package com.population.dao;

import com.population.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportDAOTest {

    private final ReportDAO dao = new ReportDAO();

    @Test
    void testCountriesByPopulation() throws Exception {
        List<Country> countries = dao.countriesByPopulation();
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Countries list should not be empty");

        Country first = countries.get(0);
        System.out.println("Most populous country: " + first.name() + " (" + first.population() + ")");
    }

    @Test
    void testCitiesByPopulation() throws Exception {
        List<City> cities = dao.citiesByPopulation();
        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Cities list should not be empty");

        City first = cities.get(0);
        System.out.println("Most populous city: " + first.name() + ", " + first.country());
    }

    @Test
    void testCapitalCitiesByPopulation() throws Exception {
        List<City> capitals = dao.capitalCitiesByPopulation();
        assertNotNull(capitals);
        assertFalse(capitals.isEmpty(), "Capital cities list should not be empty");

        City topCapital = capitals.get(0);
        System.out.println("Most populous capital: " + topCapital.name());
    }

    @Test
    void testTopNCities() throws Exception {
        int N = 5;
        List<City> topCities = dao.topNCities(N);
        assertNotNull(topCities);
        assertEquals(N, topCities.size(), "Should return exactly " + N + " cities");
    }

    @Test
    void testWorldPopulation() throws Exception {
        long worldPop = dao.worldPopulation();
        assertTrue(worldPop > 0, "World population should be greater than 0");
        System.out.println("World Population: " + worldPop);
    }

    @Test
    void testPopulationOfCountry() throws Exception {
        long pop = dao.populationOfCountry("USA");
        assertTrue(pop > 0, "USA population should be greater than 0");
    }

    @Test
    void testCityVsNonCityByCountry() throws Exception {
        Map<String, long[]> map = dao.populationCityVsNonCityByCountry();
        assertNotNull(map);
        assertFalse(map.isEmpty());

        map.entrySet().stream().limit(5).forEach(entry -> {
            long[] vals = entry.getValue();
            System.out.println(entry.getKey() + ": Total=" + vals[0] + ", InCities=" + vals[1] + ", Outside=" + vals[2]);
        });
    }

    @Test
    void testLanguageStats() throws Exception {
        List<LanguageStat> stats = dao.languageStats();
        assertNotNull(stats);
        assertFalse(stats.isEmpty());

        stats.forEach(stat ->
                System.out.println(stat.language() + ": " + stat.speakers() + " speakers (" + stat.worldPercentage() + "%)")
        );
    }
}
