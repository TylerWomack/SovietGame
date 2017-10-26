package com.tylerapps.tyler.sovietgame.building;

/**
 * Created by Tyler on 5/28/2017.
 */

public enum Building {

    LABORATORY("Laboratory", 20),
    FACTORY("Factory", 100),
    HOUSE("House", 200),
    FARM("Farm", 40),
    GUNSMITH("Gunsmith", 400);

    String name;
    int cost;



    Building(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName(){
        return name;
    }

    public int getCost(){
        return cost;
    }

}
