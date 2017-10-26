package com.tylerapps.tyler.sovietgame.unit;

/**
 * Created by Tyler on 5/25/2017.
 */

public enum UnitType {
    Soldier("Soldier", "Soldiers", 80, 10, 10, 1),
    Sniper("Sniper", "Snipers", 120, 10, 20, 1),
    Tank("Tank", "Tanks", 200, 40, 30, 2),
    Artillery("Artillery", "Artillery", 120, 20, 10, 1),
    KGB("KGB Officer", "KGB Officers", 200, 0, 0, 2);

    public String name;
    public String pluralName;
    public int cost;
    public int attack;
    public int defense;
    public int speed;

    UnitType(String name, String pluralName, int cost, int attack, int defense, int speed) {
        this.name = name;
        this.pluralName = pluralName;
        this.cost = cost;
        this.attack = attack;
        this.defense = defense;
    }

    public int getAttack(){
        return attack;
    }

    public int getDefense(){
        return defense;
    }

    public int getCost(){
        return cost;
    }

    public String getName(){
        return name;
    }

    public String getPluralName(){return pluralName;}

    public int getSpeed(){ return speed; }
}
