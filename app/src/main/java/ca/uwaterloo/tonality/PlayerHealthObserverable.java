package ca.uwaterloo.tonality;

import java.util.Observable;

public class PlayerHealthObserverable extends Observable {
    private int health;
    private static PlayerHealthObserverable INSTANCE = null;

    private PlayerHealthObserverable(int startHealth){
        health = startHealth;
    }

    public static PlayerHealthObserverable getInstance(int startHealth){
        if (INSTANCE == null){
            INSTANCE = new PlayerHealthObserverable(startHealth);
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
}
