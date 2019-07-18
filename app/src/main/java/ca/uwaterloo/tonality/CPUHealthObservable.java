package ca.uwaterloo.tonality;

import java.util.Observable;

public class CPUHealthObservable extends Observable {

    private int health;
    private static CPUHealthObservable INSTANCE = null;

    private CPUHealthObservable(int startHealth){
        health = startHealth;
    }

    public static CPUHealthObservable getInstance(int startHealth){
        if (INSTANCE == null){
            INSTANCE = new CPUHealthObservable(startHealth);
        }
        return INSTANCE;
    }

    public int getUserHealth(){
        return health;
    }

    public void decrementUserHealth(){
        health--;
    }

    public void incrementUserHealth(int amount){
        health += amount;
    }

    public void resetHealth(int amount){
        health = amount;
    }
}
