package com.tylerapps.tyler.sovietgame.territory;

import com.tylerapps.tyler.sovietgame.building.Building;
import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.unit.IUnit;
import com.tylerapps.tyler.sovietgame.unit.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler on 5/25/2017.
 */

public interface ITerritory {

    public ICountry getCountry();
    public String getName();
    public List<IUnit> getUnits();
    public List<IUnit> getUnits(UnitType type);
    public List<IUnit> getUnitsByTypeAndCountry(UnitType type, ICountry country);
    public HashMap<UnitType, Integer> getCountofUnitTypesforCountry(ICountry country);
    public List<Building> getBuildings();
    public int getCurrentScience();
    public int getCurrentRebellion();
    public int getFoodProduction();
    public int getHammerProduction();
    public int getPopulation();
    public int getRepressionScore();
    public int getBuildingTurnsRemaining();
    public int getHammersApplied();
    public Building getCurrentBuilding();
    public void applyHammers();
    public void addUnit(IUnit unit);
    public void removeUnit(IUnit unit);
    public void removeAllUnits();
    public void addUnitArrayList(ArrayList<IUnit> units);
    public void changeOwner(ICountry losingCountry, ICountry winningCountry);
    public ArrayList<IUnit> getUnitsFromIds(ArrayList<String> ids);






}


