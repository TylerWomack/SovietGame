package com.tylerapps.tyler.sovietgame.unit;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.territory.ITerritory;

import java.io.Serializable;

/**
 * Created by Tyler on 5/25/2017.
 */

public interface IUnit {

    //public Bitmap getDetailPicture();
    public Bitmap getMapPicture();
    public String getDescription();

    public void move(ITerritory toMove);
    public boolean isMoveValid(ITerritory toMove);
    public ITerritory getTerritory();
    public ICountry getCountry();
    public String getId();

    /*
     Unit stats
     */
    public int getDefense();
    public int getAttack();
    public int getCost();
    public int getSpeed();
    public int spacesMovedThisTurn();
    public UnitType getType();






}
