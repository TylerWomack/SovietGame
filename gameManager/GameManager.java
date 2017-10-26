package com.tylerapps.tyler.sovietgame.gameManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.tylerapps.tyler.sovietgame.MainActivity;
import com.tylerapps.tyler.sovietgame.R;
import com.tylerapps.tyler.sovietgame.building.Building;
import com.tylerapps.tyler.sovietgame.building.BuildingList;
import com.tylerapps.tyler.sovietgame.building.IBuilding;
import com.tylerapps.tyler.sovietgame.country.Country;
import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.territory.ITerritory;
import com.tylerapps.tyler.sovietgame.territory.Territory;
import com.tylerapps.tyler.sovietgame.unit.BaseUnit;
import com.tylerapps.tyler.sovietgame.unit.IUnit;
import com.tylerapps.tyler.sovietgame.unit.Soldier;
import com.tylerapps.tyler.sovietgame.unit.Tank;
import com.tylerapps.tyler.sovietgame.unit.UnitType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Tyler on 5/25/2017.
 */

 public class GameManager {

    public static GameManager gm = new GameManager();

    //treat this class like a database. Store and load all data here.

    private static GameManager gameManager = new GameManager();

    Map<String, ITerritory> territories = new HashMap<String, ITerritory>();
    public Map<String, ICountry> countries = new HashMap<String, ICountry>();

    private GameManager(){

        ICountry USSR = new Country("USSR", "Soviet");
        ICountry Germany = new Country("Germany", "German");

        countries.put("USSR", USSR);
        countries.put("Germany", Germany);
        //adds in the territories the first time

        Bitmap image = MainActivity.getMap();

        int width = image.getWidth();
        int height = image.getHeight();

        int cEuropeX = (int) (width * 0.391);
        int cEuropeY = (int) (height * 0.61);
        int romaniaX = (int) (width * 0.501);
        int romaniaY = (int) (height * 0.721);
        int yugoX = (int) (width * 0.421);
        int yugoY = (int) (height * 0.822);
        int italyX = (int) (width * 0.320);
        int italyY = (int) (height * 0.812);
        int polandX = (int) (width * 0.452);
        int polandY = (int) (height * 0.538);
        int germanyX = (int) (width * 0.320);
        int germanyY = (int) (height * 0.563);
        int scandX = (int) (width * 0.351);
        int scandY = (int) (height * 0.235);
        int ukX = (int) (width * 0.191);
        int ukY = (int) (height * 0.509);
        int franceX = (int) (width * 0.225);
        int franceY = (int) (height * 0.741);
        int swissX = (int) (width * 0.286);
        int swissY = (int) (height * 0.728);
        int portX = (int) (width * 0.067);
        int portY = (int) (height * 0.914);
        int turkeyX = (int) (width * .663);
        int turkeyY = (int) (height * .884);
        int siberiaX = (int) (width * .845);
        int siberiaY = (int) (height * .158);
        int moscowX = (int) (width * .637);
        int moscowY = (int) (height * .277);
        int stPetersburgX = (int) (width * .514);
        int stPetersburgY = (int) (height * .202);
        int stalingradX = (int) (width * .698);
        int stalinGradY = (int) (height * .556);
        int persiaX = (int) (width * .850);
        int persiaY = (int) (height * .825);
        int greeceX = (int) (width * .496);
        int greeceY = (int) (height * .857);
        int balkanX = (int) (width * .440);
        int balkanY = (int) (height * .378);
        int ukraineX = (int) (width * 0.52);
        int ukraineY = (int) (height * 0.57);


        territories.put("Moscow",
                new Territory("Moscow", moscowX, moscowY, new ArrayList<Building>(
                Arrays.asList(Building.FARM)),
                new ArrayList<IUnit>(),
                USSR , 20, 100, 1000, 200, 100, 100, 0, Building.FACTORY));

        territories.put("St. Petersburg",
                new Territory("St. Petersburg", stPetersburgX, stPetersburgY,
                new ArrayList<Building>(Arrays.asList(Building.HOUSE)),
                new ArrayList<IUnit>(),
                USSR, 10, 100, 1000, 200, 100, 100, 0, Building.FACTORY));

        territories.put("Stalingrad",
                new Territory("Stalingrad", stalingradX, stalinGradY, new ArrayList<Building>(),
                        new ArrayList<IUnit>(),
                USSR, 20, 100, 1000, 200, 100, 100, 100, Building.FACTORY));

        territories.put("Germany",
                new Territory("Germany", germanyX, germanyY, new ArrayList<Building>(),
                        new ArrayList<IUnit>(),
                        Germany, 20, 100, 1000, 200, 100, 100, 100, Building.FACTORY));

        Tank tank = new Tank(getTerritoryFromString("Moscow"), USSR);
        Tank tank2 = new Tank(getTerritoryFromString("Moscow"), USSR);
        Tank tank3 = new Tank(getTerritoryFromString("St. Petersburg"), USSR);
        Tank tank4 = new Tank(getTerritoryFromString("St. Petersburg"), USSR);

        Soldier soldier1 = new Soldier(getTerritoryFromString("Germany"), Germany);
        Soldier soldier2 = new Soldier(getTerritoryFromString("Germany"), Germany);
        Soldier soldier3 = new Soldier(getTerritoryFromString("Germany"), Germany);

        getTerritoryFromString("Moscow").addUnit(tank);
        getTerritoryFromString("Moscow").addUnit(tank2);
        getTerritoryFromString("St. Petersburg").addUnit(tank3);
        getTerritoryFromString("St. Petersburg").addUnit(tank4);

        getTerritoryFromString("Germany").addUnit(soldier1);
        getTerritoryFromString("Germany").addUnit(soldier2);
        getTerritoryFromString("Germany").addUnit(soldier3);


    }

    public ITerritory getTerritoryFromString(String name){
        return territories.get(name);
    }

    public static GameManager getGameManager(){
        return gm;
    }

    public ICountry getCountryFromString(String string){
        return countries.get(string);
    }



    public void moveUnits(List<IUnit> units , ITerritory from, ITerritory to){

        //doing this to avoid concurrent modification exception when removing from
        //a list we're iterating over.

        Object[] unitsClone = units.toArray();

        for (Object o : unitsClone){
            IUnit unit = (IUnit)o;
            unit.move(to);
        }

    }

    //attacks, and returns list of defenders that are left.
    public ArrayList<IUnit> attack(ArrayList<IUnit> attackingUnits, ArrayList<IUnit> defendingUnits){
        int attackingHits = 0;
        int defendingHits = 0;
        Random random = new Random();

        for (IUnit attackingUnit : attackingUnits){
            int roll = rollDice();
            if (roll < attackingUnit.getAttack() + 1)
                attackingHits++;
        }

        for (int i = 0; i < attackingHits; i++){
            if (defendingUnits.size() > 0)
                defendingUnits.remove(random.nextInt(defendingUnits.size()));
        }

        return defendingUnits;

    }

    public ArrayList<IUnit> defend(ArrayList<IUnit> attackingUnits, ArrayList<IUnit> defendingUnits){
        int attackingHits = 0;
        int defendingHits = 0;
        Random random = new Random();

        for (IUnit defendingUnit : defendingUnits){
            int roll = rollDice();
            if (roll < defendingUnit.getDefense() + 1)
                defendingHits++;
        }

        for (int i = 0; i < defendingHits; i++){
            if (attackingUnits.size() > 0)
                attackingUnits.remove(random.nextInt(attackingUnits.size()));
        }

        return attackingUnits;


    }



    public List<ITerritory> getTerritories(){
        return new ArrayList<ITerritory>(territories.values());
    }

    public int rollDice(){
        Random random = new Random();
        return (random.nextInt(60) + 1);
    }

    //checks for battles and executes them.
    public void checkForBattles(){

        ArrayList<IUnit> attackingUnits = new ArrayList<IUnit>();
        ArrayList<IUnit> defendingUnits = new ArrayList<IUnit>();

        ICountry country1 = null;
        ICountry country2 = null;

        for (ITerritory x : territories.values()){

            //list of all units in territory
            List<IUnit> allUnitsInTerritory = x.getUnits();
            ICountry ownerOfTerritory = x.getCountry();

            if (allUnitsInTerritory.size() > 0){

            for (IUnit unit : allUnitsInTerritory){
                if(unit.getCountry() != ownerOfTerritory)
                    attackingUnits.add(unit);
                if (unit.getCountry() == ownerOfTerritory)
                    defendingUnits.add(unit);
            }
            if (defendingUnits.size() != 0 && attackingUnits.size() != 0){
                defendingUnits = attack(attackingUnits, defendingUnits);
                attackingUnits = defend(attackingUnits, defendingUnits);

                if (defendingUnits.size() == 0 && attackingUnits.size() > 0){
                    x.changeOwner(x.getCountry(), attackingUnits.get(0).getCountry());
                    Toast.makeText(MainActivity.getMainContext(), "Germany has fallen!!", Toast.LENGTH_SHORT).show();
                }


                x.removeAllUnits();
                x.addUnitArrayList(defendingUnits);
                x.addUnitArrayList(attackingUnits);
                

            }
        }
        }
    }


    /**
     * end of turn sequence:
     * Move enemy units
     * Perform battles
     * Build items in each territory (enemy and own).
     * Update tech
     * Advance date
     * Update country status
     * Generate random events
     * Status report
     */

    //// TODO: 6/1/2017 change the actual color on the screen

    //// TODO: 6/2/2017 Delete this method
    public void changePossession(ITerritory territory, ICountry country){

        Context context = MainActivity.getMainContext();
        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.startingborders);


        int width = image.getWidth();
        int height = image.getHeight();

        int cEuropeX = (int) (width * 0.391);
        int cEuropeY = (int) (height * 0.61);
        int romaniaX = (int) (width * 0.501);
        int romaniaY = (int) (height * 0.721);
        int yugoX = (int) (width * 0.421);
        int yugoY = (int) (height * 0.822);
        int italyX = (int) (width * 0.320);
        int italyY = (int) (height * 0.812);
        int polandX = (int) (width * 0.452);
        int polandY = (int) (height * 0.538);
        int germanyX = (int) (width * 0.320);
        int germanyY = (int) (height * 0.563);
        int scandX = (int) (width * 0.351);
        int scandY = (int) (height * 0.235);
        int ukX = (int) (width * 0.191);
        int ukY = (int) (height * 0.509);
        int franceX = (int) (width * 0.225);
        int franceY = (int) (height * 0.741);
        int swissX = (int) (width * 0.286);
        int swissY = (int) (height * 0.728);
        int portX = (int) (width * 0.067);
        int portY = (int) (height * 0.914);
        int turkeyX = (int) (width * .663);
        int turkeyY = (int) (height * .884);
        int siberiaX = (int) (width * .845);
        int siberiaY = (int) (height * .158);
        int moscowX = (int) (width * .637);
        int moscowY = (int) (height * .277);
        int stPetersburgX = (int) (width * .514);
        int stPetersburgY = (int) (height * .202);
        int stalingradX = (int) (width * .698);
        int stalinGradY = (int) (height * .556);
        int persiaX = (int) (width * .850);
        int persiaY = (int) (height * .825);
        int greeceX = (int) (width * .496);
        int greeceY = (int) (height * .857);
        int balkanX = (int) (width * .440);
        int balkanY = (int) (height * .378);
        int ukraineX = (int) (width * 0.52);
        int ukraineY = (int) (height * 0.57);

        int xPoint = 0;
        int yPoint = 0;

        if (territory.getName().equalsIgnoreCase("Central Europe")){
            xPoint = cEuropeX;
            yPoint = cEuropeY;
        }

        if (territory.getName().equalsIgnoreCase("Romania")){
            xPoint = romaniaX;
            yPoint = romaniaY;
        }

        if (territory.getName().equalsIgnoreCase("Yugoslavia")){
            xPoint = yugoX;
            yPoint = yugoY;
        }

        if (territory.getName().equalsIgnoreCase("Italy")){
            xPoint = italyX;
            yPoint = italyY;
        }

        if (territory.getName().equalsIgnoreCase("Poland")){
            xPoint = polandX;
            yPoint = polandY;
        }

        if (territory.getName().equalsIgnoreCase("Germany")){
            xPoint = germanyX;
            yPoint = germanyY;
        }

        //// TODO: 6/2/2017 add in the rest of the countries x and y coordinates when done testing


    }

    
    
    public void endTurn(){

        for (ITerritory territory : territories.values()){
            territory.applyHammers();
        }

        checkForBattles();


        //applyScience();
        //doEvents();
        //ai();





    }

    /*
    AI strategy:
    deterministic. No AI.

    Some units permanently stay in a territory.
    Other half

    AI simulates a battle and calculates results. If results are really good, it should attack.
    Results should be based on unit cost.
    If attack results in +20% more damage than damage received (in terms of unit cost), attack.
    Just simulate once to introduce randomness.

    At the same time, you need predetermined attacks. In 1942 (or whenever) the germans attack and don't stop,
    no matter what.

    After that the ai will kick in.

    In summary:
    3 types of units:
    blitzkreig forces
    stationary forces
    ai forces

    We probably need to build an ai type for units. I.e. label each unit as blitz, stationary, ai.

    ai movement:


    objectives for a unit:
    1. defend territory.
    2. form a larger attack battalion.
    3. attack



     */

}
