package ca.uwaterloo.tonality;

import android.graphics.RectF;

public class noteButton {

    public int sound;
    public RectF rect;
    public boolean pressed;

    public noteButton(RectF rect, int sound){
        this.sound = sound;
        this.rect = rect;
        this.pressed = false;
    }

}
