package com.tylerapps.tyler.sovietgame.unit;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.gameManager.GameManager;
import com.tylerapps.tyler.sovietgame.territory.ITerritory;
import com.tylerapps.tyler.sovietgame.territory.TerritoryList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Tyler on 5/25/2017.
 */

public abstract class BaseUnit implements IUnit {


    private ITerritory currentTerritory;
    private ICountry country;
    public String uniqueId;

    public BaseUnit(ITerritory startingTerritory, ICountry country) {
        uniqueId = UUID.randomUUID().toString();
        this.currentTerritory = startingTerritory;
        this.country = country;
    }

    public void setTerritory(ITerritory newTerritory) {
        this.currentTerritory = newTerritory;
    }

    public ITerritory getTerritory() {
        return currentTerritory;
    }

    public String getId(){
        return uniqueId;
    }


    public void move(ITerritory toMove) {
        toMove.addUnit(this);
        currentTerritory.removeUnit(this);
        this.setTerritory(toMove);
    }

    public ICountry getCountry() {
        return country;
    }

    public boolean isMoveValid(ITerritory toMove) {

        TerritoryList toMoveTo = TerritoryList.valueOf(toMove.getName());

        //list of all territories it is valid to move to
        Set<TerritoryList> validMoves = null;
        GameManager gameManager = GameManager.getGameManager();

        //neighboring territories that you own
        Set<TerritoryList> friendlyNeighbors = null;

        //your current territory
        ITerritory current = getTerritory();
        String currentName = current.getName();

        //all neighboring countries. Adding them to valid moves


        ArrayList<String> neighbors = TerritoryList.valueOf(currentName).neighboringCountries;


        for (String neighbor : neighbors) {
            validMoves.add(TerritoryList.valueOf(neighbor));
        }


        if ((getSpeed() - spacesMovedThisTurn() >= 1) && validMoves.contains(toMoveTo)) {
            return true;
        }

        if (getSpeed() - spacesMovedThisTurn() <= 0)
            return false;

        if ((getSpeed() > 2))
            throw new RuntimeException("Hasn't been implemented - speed greater than 2");

        if ((getSpeed() - spacesMovedThisTurn() == 2)) {



            //here, we need to deal with the two movers...

            //a lot of code merely to check if the Territory in question is friendly. If so, add it to the friendlyList.
            for (TerritoryList tl : validMoves) {
                String name = tl.name();
                ITerritory test = gameManager.getTerritoryFromString(name);

                //testing to see if the territory you want to move through is a friendly territory.
                if (country == test.getCountry()) {
                    friendlyNeighbors.add(tl);
                }
            }

            for (TerritoryList tl : friendlyNeighbors) {
                ArrayList<String> neighborsOfFriendly = tl.neighboringCountries;
                for (String x : neighborsOfFriendly) {
                    validMoves.add(TerritoryList.valueOf(x));
                }
            }
        }


        //test the ITerritory vs the Set of TerritoryList (validMoves) to see if it is included.

        for (TerritoryList territory : validMoves){
             if(territory.name().equalsIgnoreCase(toMove.getName())){
                 return true;
             }

        }

        return false;

        //get a Set of each country and its' neighbors.
        //add each neighboring country to the Set of available countries to move to.
        //for each neighboring friendly country, find a list of its' neighboring countries (for the units that move 2 spaces)
        //add those to the Set. (Set can't contain duplicates)
    }

    public abstract int getDetailPictureId();

    public abstract Bitmap getMapPicture();

    public abstract String getDescription();


    public abstract UnitType getType();

    /*
     Unit stats
     */
    public abstract int getAttack();

    public abstract int getDefense();

    public abstract int getCost();

    public abstract int getSpeed();

    /*
    public abstract int spacesMovedThisTurn();
    */

}
