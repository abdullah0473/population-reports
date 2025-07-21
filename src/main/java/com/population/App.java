package com.population;

import com.population.service.ReportService;

import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws SQLException {
        ReportService service = new ReportService();
        try (Scanner in = new Scanner(System.in)) {
			while (true) {
			    System.out.println("""
			            \nChoose an option
			              1. Countries by population
			              2. Cities by population
			              3. Capital cities by population
			              4. Top N cities
			              5. City vs Non‑city per country
			              6. Language statistics
			              0. Quit""");
			    switch (in.nextInt()) {
			        case 1 -> service.printCountries();
			        case 2 -> service.printCities();
			        case 3 -> service.printCapitalCities();
			        case 4 -> {
			            System.out.print("N = ");
			            service.printTopNCities(in.nextInt());
			        }
			        case 5 -> service.printCityVsNonCity();
			        case 6 -> service.printLanguageStats();
			        case 0 -> { System.exit(0); }
			        default -> System.out.println("Invalid.");
			    }
			}
		}
    }
}