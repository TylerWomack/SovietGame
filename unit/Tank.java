package com.tylerapps.tyler.sovietgame.unit;

import android.graphics.Bitmap;

import com.tylerapps.tyler.sovietgame.R;
import com.tylerapps.tyler.sovietgame.country.ICountry;
import com.tylerapps.tyler.sovietgame.territory.ITerritory;

import java.io.Serializable;


/**
 * Created by Tyler on 5/31/2017.
 */

public class Tank extends BaseUnit{

    public static int detailedPictureId = R.drawable.soviet_tank;
    public static String description = "By the eve of World War II, the Soviet Union had some of the world's best tanks. Their newest models were basically a generation ahead, which came as a shock to invading forces. However, it still had many older tanks in its front-line armoured forces, with the T-26 forming the backbone of the Red Army's armoured forces during the first months of the German invasion of the Soviet Union. In overall tanks, however, the Soviet numerical advantage was considerable as the Red Army had a large quantitative superiority. However, maintenance and readiness standards were very poor; ammunition and radios were in short supply, and many units lacked the trucks needed for resupply beyond their basic fuel and ammunition loads";

    public ICountry country = null;
    public int spacesMoved = 0;

    public Tank(ITerritory startingTerritory, ICountry country){
        super(startingTerritory, country);
        this.country = country;

        //todo: update the detailed picture for the soldier.
        //detailedPicture = ...

    }





    public int getDetailedPictureId() {
        return detailedPictureId;
    }


    @Override
    public int getDetailPictureId() {
        return 0;
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
    public UnitType getType() {
        return UnitType.Tank;
    }

    @Override
    public int getAttack() {
        return UnitType.Tank.getAttack();
    }

    @Override
    public int getDefense() {
        return UnitType.Tank.getDefense();
    }

    @Override
    public int getCost() {
        return UnitType.Tank.getCost();
    }

    @Override
    public int getSpeed() {
        return UnitType.Tank.getSpeed();
    }


    @Override
    public int spacesMovedThisTurn() {
        return spacesMoved;
    }

}
