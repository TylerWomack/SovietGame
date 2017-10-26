package com.tylerapps.tyler.sovietgame.unit;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.tylerapps.tyler.sovietgame.R;
import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.territory.ITerritory;

import java.io.Serializable;

/**
 * Created by Tyler on 5/28/2017.
 */

public class Soldier extends BaseUnit {

    public UnitType type = UnitType.Soldier;

    public int spacesMoved = 0;
    public static int detailedPictureId = R.drawable.soldier;
    public static String description = "Red Army soldier - Often poorly equipped and disorganized, the soldiers of the Red Army" +
            "faced severe material deprivation. In the chaos of the early second world war, many of these soldiers faced impossible " +
            "odds, and were often ordered to carry out missions that were nearly suicidal in nature. Nevertheless, many of these soldiers fought " +
            "with extraordinary bravery, fury, and resolve that shocked invading forces.";

    public ICountry country = null;



    public Soldier(ITerritory startingTerritory, ICountry country){
        super(startingTerritory, country);
        this.country = country;

        //todo: update the detailed picture for the soldier.
        //detailedPicture = ...

    }

    @Override
    public int getDetailPictureId() {
        return detailedPictureId;
    }


    public Bitmap getDetailPicture() {
        return null;
    }

    @Override
    public Bitmap getMapPicture() {
        return null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isMoveValid(ITerritory toMove) {
         return super.isMoveValid(toMove);
    }

    @Override
    public ICountry getCountry() {
        return country;
    }

    @Override
    public UnitType getType() {
        return UnitType.Soldier;
    }

    @Override
    public int getAttack() {
        return UnitType.Soldier.getAttack();
    }

    @Override
    public int getDefense() {
       return UnitType.Soldier.getDefense();
    }

    @Override
    public int getCost() {
        return UnitType.Soldier.getCost();
    }

    @Override
    public int getSpeed() {
        return UnitType.Soldier.getSpeed();
    }

    //// TODO: 5/31/2017 understand this below:


    @Override
    public int spacesMovedThisTurn() {
        return spacesMoved;

    }

}
