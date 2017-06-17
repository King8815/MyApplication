package com.example.user.myapplication.phonesafe.phonesafe_adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2016/7/15.
 * 黑名单数据库，主要用来存储黑名单中的联系人信息
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    // 创建一个黑名单数据库blackNumber.db
    public BlackNumberOpenHelper(Context context) {
        super(context, "blackNumber.db", null, 1);
    }
    // 在数据库中创建blacknumber表，该表包含4个字段分别为id，number，name，mode，其中id为自增主键，number为电话号码，name为联系人姓名，mode为拦截模式
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber (id integer primary key autoincrement, number varchar(20), name varchar(255), mode integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
