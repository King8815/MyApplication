package com.example.slide_table.utils;

/**
 * Created by user on 2016/10/7.
 */
public class ChangeLanguageEvent {
    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;
    private int language;

    public ChangeLanguageEvent(){}

    public ChangeLanguageEvent(int language){
        super();
        this.language = language;
    }
    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }
}
