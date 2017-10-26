package com.tylerapps.tyler.sovietgame.country;

import android.graphics.Color;

import com.tylerapps.tyler.sovietgame.territory.ITerritory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tyler on 5/31/2017.
 */

public class Country implements ICountry, Serializable{

    public String name = null;
    public String pluralName = null;
    public int color;

    public Country(String name, String pluralName){
        this.name = name;
        this.pluralName = pluralName;

        if (name == "Germany"){
            color = Color.rgb(153, 153, 153);
        }

        if (name == "USSR"){
            color = Color.rgb(203, 52, 52);
        }

        this.color = color;
    }



    public String toString(){
        return name;
    }

    public String getPluralName(){
        return pluralName;
    }

    @Override
    public List<ITerritory> getTerritories() {
        return null;
    }

    @Override
    public int getScience() {
        return 0;
    }

    @Override
    public int getRepression() {
        return 0;
    }

    @Override
    public int getRebellion() {
        return 0;
    }

    @Override
    public int getForeignEspionage() {
        return 0;
    }


    public int getColor(){

        return color;

    }
}
