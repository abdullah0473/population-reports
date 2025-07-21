package com.population.service;

import com.population.dao.ReportDAO;
import com.population.model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final ReportDAO dao = new ReportDAO();

    public void printCountries() throws SQLException {
        printList("Countries by population", dao.countriesByPopulation());
    }

    public void printCities() throws SQLException {
        printList("Cities by population", dao.citiesByPopulation());
    }

    public void printCapitalCities() throws SQLException {
        printList("Capital cities by population", dao.capitalCitiesByPopulation());
    }

    public void printTopNCities(int n) throws SQLException {
        printList("Top " + n + " cities", dao.topNCities(n));
    }

    public void printCityVsNonCity() throws SQLException {
        Map<String, long[]> data = dao.populationCityVsNonCityByCountry();
        System.out.printf("%-5s %15s %15s %15s%n", "Code","Total","In cities","Not in cities");
        data.forEach((code, arr) -> System.out.printf(
                "%-5s %,15d %,15d %,15d%n", code, arr[0], arr[1], arr[2]));
    }

    public void printLanguageStats() throws SQLException {
        List<LanguageStat> list = dao.languageStats();
        System.out.printf("%-10s %15s %10s%n","Language","Speakers","World %");
        list.forEach(l -> System.out.printf("%-10s %,15d %9.2f%n",
                                            l.language(), l.speakers(), l.worldPercentage()));
    }

    private <T> void printList(String title, List<T> list) {
        System.out.println("\n=== " + title + " ===");
        list.forEach(System.out::println);
    }
}
