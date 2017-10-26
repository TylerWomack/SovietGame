package com.tylerapps.tyler.sovietgame.country;

import android.graphics.Color;

import com.tylerapps.tyler.sovietgame.territory.ITerritory;

import java.util.List;

/**
 * Created by Tyler on 5/25/2017.
 */

public interface ICountry {
    public String toString();
    public List<ITerritory> getTerritories();
    public int getScience();
    public int getRepression();
    public int getRebellion();
    public int getForeignEspionage();
    public String getPluralName();
    public int getColor();



}
