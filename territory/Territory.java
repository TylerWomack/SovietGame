package com.tylerapps.tyler.sovietgame.territory;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Parcelable;

import com.tylerapps.tyler.sovietgame.MainActivity;
import com.tylerapps.tyler.sovietgame.building.Building;

import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.unit.IUnit;
import com.tylerapps.tyler.sovietgame.unit.UnitType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Tyler on 5/25/2017.
 */

public class Territory implements ITerritory, Serializable {

    public String name;
    public int xCoord;
    public int yCoord;


    public ArrayList<Building> buildings;
    public ArrayList<IUnit> units;
    public ICountry owner;
    public int hammersPerTurn;
    public int sciencePerTurn;
    public int population;
    public int food;
    public int repression;
    public int rebellion;

    public int buildingProgress;
    public Building currentBuilding;

    public Territory(String name, int x, int y, ArrayList<Building> buildings, ArrayList<IUnit> units,
                     ICountry owner, int hammersPerTurn, int sciencePerTurn, int population,
                     int food, int repression, int rebellion, int buildingProgress, Building currentBuilding) {

        this.name = name;
        this.xCoord = x;
        this.yCoord = y;
        this.buildings = buildings;
        this.units = units;
        this.owner = owner;
        this.hammersPerTurn = hammersPerTurn;
        this.sciencePerTurn = sciencePerTurn;
        this.population = population;
        this.food = food;
        this.repression = repression;
        this.rebellion = rebellion;
        this.buildingProgress = buildingProgress;
        this.currentBuilding = currentBuilding;
    }

    public String getName(){
        return name;
    }

    @Override
    public ICountry getCountry() {
        return owner;
    }

    public int getBuildingTurnsRemaining(){

        int cost = currentBuilding.getCost();
        int hammersToGo = cost - buildingProgress;
        int turnsRemaining = hammersToGo/hammersPerTurn;
        if (turnsRemaining < 1 )
            turnsRemaining = 1;
        return turnsRemaining;
    }

    public int getHammersApplied(){
        return buildingProgress;
    }



    @Override
    public List<IUnit> getUnits() {
        return units;
    }

    public List<IUnit> getUnits(UnitType type){
        ArrayList<IUnit> toReturn = new ArrayList<>();
        for(IUnit unit : units){
            if(unit.getType().equals(type))
                toReturn.add(unit);
        }
        return toReturn;
    }

    public List<IUnit> getUnitsByCountry(ICountry country){
        ArrayList<IUnit> toReturn = new ArrayList<>();
        for(IUnit unit : units){
            if(unit.getCountry().equals(country))
                toReturn.add(unit);
        }
        return toReturn;
    }

    public List<IUnit> getUnitsByTypeAndCountry(UnitType type, ICountry country){
        ArrayList<IUnit> toReturn = new ArrayList<>();
        for(IUnit unit : units){
            if(unit.getType().equals(type) && unit.getCountry().equals(country))
                toReturn.add(unit);
        }
        return toReturn;
    }


    @Override
    public List<Building> getBuildings() {
        return buildings;
    }

    @Override
    public int getCurrentScience() {
        return sciencePerTurn;
    }

    @Override
    public int getCurrentRebellion() {
        return rebellion;
    }

    @Override
    public int getFoodProduction() {
        return food;
    }

    @Override
    public int getHammerProduction() {
        return hammersPerTurn;
    }

    @Override
    public int getPopulation() {
        return population;
    }

    @Override
    public int getRepressionScore() {
        return repression;
    }

    public Building getCurrentBuilding(){
        return  currentBuilding;
    }


    public void selectBuildingToBuild(Building building){
        currentBuilding = building;
    }

    public void applyHammers(){
        int cost = currentBuilding.getCost();
        int hammersToGo = cost - buildingProgress;
        hammersToGo = hammersToGo - hammersPerTurn;

        if (hammersToGo < 0){
            buildings.add(currentBuilding);
            buildingProgress = Math.abs(hammersToGo);
        }else {
            buildingProgress = buildingProgress + hammersPerTurn;
        }
    }

    public void addUnit(IUnit unit){
        units.add(unit);
    }

    public void removeUnit(IUnit unit){
        units.remove(units.indexOf(unit));
    }

    public void removeAllUnits(){
        units.clear();
    }

    public void addUnitArrayList(ArrayList<IUnit> units){
        for (IUnit unit : units){
            addUnit(unit);
        }
    }

    //Given a country, returns a Map with IUnit as key and the number of that type of unit as values.
    public HashMap<UnitType, Integer> getCountofUnitTypesforCountry(ICountry country) {

        List<IUnit> x = getUnits();
        ArrayList<IUnit> countrysUnits = new ArrayList<IUnit>();
        for (int i = 0; i < x.size(); i++) {

            if (x.get(i).getCountry().equals(country)) {
                countrysUnits.add(x.get(i));
            }
        }
        //at this point, countrysUnits should contain all the units of that country. (And nothing else)

        HashMap<UnitType, Integer> unitCount = new HashMap<>();
        for (int i = 0; i < countrysUnits.size(); i++){
            int currentCount = 0;
            if (unitCount.get(countrysUnits.get(i).getType()) != null)
                currentCount = unitCount.get(countrysUnits.get(i).getType());

            currentCount++;
            unitCount.put(countrysUnits.get(i).getType(), currentCount);
        }

        return unitCount;
    }


    public void changeOwner(ICountry losingCountry, ICountry winningCountry){
        this.owner = winningCountry;
        Point point = new Point( xCoord, yCoord);
        
        //// TODO: 8/2/2017 fix this so it actually changes the color on the map (and stays that way) 
        MainActivity.floodfill(MainActivity.getMap(), point, losingCountry.getColor(), winningCountry.getColor());
        MainActivity.refreshMap();
        //test
        Bitmap bitmap = MainActivity.loadMainMapFromFile();
        String x = "";
    }

    public ArrayList<IUnit> getUnitsFromIds(ArrayList<String> ids){
        ArrayList<IUnit> toReturn = new ArrayList<IUnit>();
            List<IUnit> allUnitsInTerritory = this.getUnits();

            for (IUnit x : allUnitsInTerritory){
                if (ids.contains(x.getId()))
                    toReturn.add(x);
            }
        return toReturn;
    }
}
