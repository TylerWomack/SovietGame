

package com.tylerapps.tyler.sovietgame.territory;

import com.tylerapps.tyler.sovietgame.country.ICountry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public enum TerritoryList {



    MOSCOW("Moscow", new ArrayList<String>(
            Arrays.asList("Siberia", "Stalingrad", "St. Petersburg", "Ukraine", "Scandinavia"))),

    SIBERIA("Siberia", new ArrayList<String>(
            Arrays.asList("Moscow", "Stalingrad"))),

    STALINGRAD("Stalingrad", new ArrayList<String>(
            Arrays.asList("Moscow", "Siberia", "Ukraine", "Persia", "Turkey"))),

    PERSIA("Persia", new ArrayList<String>(
            Arrays.asList("Turkey", "Stalingrad"))),

    UKRAINE("Ukraine", new ArrayList<String>(
            Arrays.asList("Moscow", "St. Petersburg", "Stalingrad", "Romania", "Poland"))),

    TURKEY("Turkey", new ArrayList<String>(
            Arrays.asList("Southern Europe", "Persia", "Stalingrad"))),

    SOUTHERNEUROPE("Southern Europe", new ArrayList<String>(
            Arrays.asList("Turkey", "Romania", "Yugoslavia"))),

    ROMANIA("Romania", new ArrayList<String>(
            Arrays.asList("Southern Europe", "Yugoslavia", "Central Europe" , "Ukraine", "Poland"))),

    YUGOSLAVIA("Yugoslavia", new ArrayList<String>(
            Arrays.asList("Southern Europe", "Romania", "Central Europe" , "Italy"))),

    CENTRALEUROPE("Central Europe", new ArrayList<String>(
            Arrays.asList("Poland", "Romania", "Yugoslavia" , "Italy", "Germany"))),

    GERMANY("Germany", new ArrayList<String>(
            Arrays.asList("Scandinavia", "Poland", "Central Europe" , "France"))),

    POLAND("Poland", new ArrayList<String>(
            Arrays.asList("Balkans", "St. Petersburg", "Central Europe" , "Ukraine", "Romania", "Germany"))),

    BALKANS("Balkans", new ArrayList<String>(
            Arrays.asList("St. Petersburg", "Poland"))),

    STPETERSBURG("St. Petersburg", new ArrayList<String>(
            Arrays.asList("Scandinavia", "Moscow", "Ukraine", "Poland", "Balkans"))),

    SCANDINAVIA("Scandinavia", new ArrayList<String>(
            Arrays.asList("Moscow", "St. Petersburg", "Germany"))),

    ITALY("Italy", new ArrayList<String>(
            Arrays.asList("Yugoslavia", "Central Europe", "France"))),

    FRANCE("France", new ArrayList<String>(
            Arrays.asList("Germany", "Italy", "Spain")));







    private String name;
    public ArrayList<String> neighboringCountries;

    TerritoryList(String name, ArrayList<String> neighboringCountries) {
        this.name = name;
        this.neighboringCountries = neighboringCountries;
    }

}


