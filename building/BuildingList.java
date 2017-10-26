package com.tylerapps.tyler.sovietgame.building;

/**
 * Created by Tyler on 5/31/2017.
 */

public enum BuildingList {

    COLLECTIVEFARM("Collective Farm", 80),
    BARRACKS("Barracks", 120),
    FACTORY("Factory", 200);


    public String name;
    public int cost;



    BuildingList(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }




}
