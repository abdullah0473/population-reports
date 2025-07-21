package com.population.dao;

import com.population.model.*;
import com.population.util.DB;

import java.sql.*;
import java.util.*;

public class ReportDAO {

    /* ======= LIST REPORTS ======= */

    public List<Country> countriesByPopulation() throws SQLException {
        String sql = """
            SELECT Code, Name, Continent, Region, Population, Capital
            FROM country
            ORDER BY Population DESC
        """;
        try (Connection c = DB.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Country> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Country(
                        rs.getString("Code"),
                        rs.getString("Name"),
                        rs.getString("Continent"),
                        rs.getString("Region"),
                        rs.getInt("Population"),
                        rs.getInt("Capital")));
            }
            return list;
        }
    }

    public List<City> citiesByPopulation() throws SQLException {
        String sql = """
          SELECT city.Name, country.Name AS Country, District, city.Population
          FROM city JOIN country ON city.CountryCode = country.Code
          ORDER BY city.Population DESC
        """;
        return fetchCityList(sql);
    }

    public List<City> capitalCitiesByPopulation() throws SQLException {
        String sql = """
          SELECT city.Name, country.Name AS Country, city.Population, city.District
          FROM country JOIN city ON country.Capital = city.ID
          ORDER BY city.Population DESC
        """;
        return fetchCityList(sql);
    }

    public List<City> topNCities(int n) throws SQLException {
        String sql = """
          SELECT city.Name, country.Name AS Country, District, city.Population
          FROM city JOIN country ON city.CountryCode = country.Code
          ORDER BY city.Population DESC
          LIMIT ?
        """;
        try (Connection c = DB.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, n);
            return collectCities(ps.executeQuery());
        }
    }

    /* ======= POPULATION REPORTS ======= */

    public long worldPopulation() throws SQLException {
        return firstLong("SELECT SUM(Population) FROM country");
    }

    public long populationOfContinent(String continent) throws SQLException {
        return firstLong("SELECT SUM(Population) FROM country WHERE Continent = ?", continent);
    }

    public long populationOfRegion(String region) throws SQLException {
        return firstLong("SELECT SUM(Population) FROM country WHERE Region = ?", region);
    }

    public long populationOfCountry(String code) throws SQLException {
        return firstLong("SELECT Population FROM country WHERE Code = ?", code);
    }

    public long populationOfDistrict(String district) throws SQLException {
        String sql = "SELECT SUM(Population) FROM city WHERE District = ?";
        return firstLong(sql, district);
    }

    public long populationOfCity(String city) throws SQLException {
        return firstLong("SELECT Population FROM city WHERE Name = ?", city);
    }

    /** People in‑city vs out‑of‑city for every country */
    public Map<String, long[]> populationCityVsNonCityByCountry() throws SQLException {
        String sql = """
          SELECT country.Code,
                 country.Population    AS total,
                 SUM(city.Population)  AS inCities
          FROM country
          JOIN city ON country.Code = city.CountryCode
          GROUP BY country.Code
        """;
        try (Connection c = DB.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<String, long[]> map = new HashMap<>();
            while (rs.next()) {
                long total = rs.getLong("total");
                long inCities = rs.getLong("inCities");
                map.put(rs.getString("Code"),
                        new long[]{ total, inCities, total - inCities });
            }
            return map;
        }
    }

    /* ======= LANGUAGE STATS ======= */

    public List<LanguageStat> languageStats() throws SQLException {
        String sql = """
          SELECT l.Language,
                 SUM( (l.Percentage/100)*c.Population ) AS speakers
          FROM countrylanguage l
          JOIN country c ON c.Code = l.CountryCode
          WHERE l.Language IN ('Chinese','English','Spanish')
          GROUP BY l.Language
          ORDER BY speakers DESC
        """;

        long worldPop = worldPopulation();
        try (Connection c = DB.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<LanguageStat> list = new ArrayList<>();
            while (rs.next()) {
                String lang  = rs.getString("Language");
                long speakers = Math.round(rs.getDouble("speakers"));
                double percent = (speakers * 100.0) / worldPop;
                list.add(new LanguageStat(lang, speakers, percent));
            }
            return list;
        }
    }

    /* ======= helpers ======= */

    private List<City> fetchCityList(String sql) throws SQLException {
        try (Connection c = DB.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return collectCities(rs);
        }
    }

    private List<City> collectCities(ResultSet rs) throws SQLException {
        List<City> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new City(
                    rs.getString("Name"),
                    rs.getString("Country"),
                    rs.getString("District"),
                    rs.getInt("Population")));
        }
        return list;
    }

    private long firstLong(String sql, String... param) throws SQLException {
        try (Connection c = DB.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < param.length; i++)
                ps.setString(i + 1, param[i]);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }
}
