package view;

import android.graphics.Point;
import android.os.Parcel;

/**
 * Created by user on 2017/4/21.
 */

public class MyPoint{
    private int x;
    private int y;

    public MyPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
