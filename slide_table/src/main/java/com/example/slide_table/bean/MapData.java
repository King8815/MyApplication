package com.example.slide_table.bean;

import java.io.Serializable;

/**
 * Created by user on 2017/2/28.
 */

public class MapData implements Serializable {
    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Weather weather;
}
