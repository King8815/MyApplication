package com.example.user.myapplication.phonesafe.phonesafe_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.user.myapplication.phonesafe.phonesafe_adapter.BlackNumberOpenHelper;
import com.example.user.myapplication.phonesafe.phonesafe_entity.BlackContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/7/16.
 * author: Brain
 * description: 对黑名单中的数据进行增、删、查询等操作,简称为数据库操作
 */
public class BlackNumberOperator {
    private BlackNumberOpenHelper blackNumberOpenHelper;

    public BlackNumberOperator(Context context) {
        super();
        blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    /**
     * 添加数据
     */
    public boolean add(BlackContactInfo blackContactInfo) {
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (blackContactInfo.phoneNumber.startsWith("+86")) {
            blackContactInfo.phoneNumber = blackContactInfo.phoneNumber.substring(3, blackContactInfo.phoneNumber.length());
        }

        values.put("number", blackContactInfo.phoneNumber);
        values.put("name", blackContactInfo.contactName);
        values.put("mode", blackContactInfo.mode);
        long rowid = db.insert("blacknumber", null, values);
        if (rowid == -1) {//插入数据的判断
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除数据
     */
    public boolean delete(BlackContactInfo blackContactInfo) {
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        int rownumber = db.delete("blacknumber", "number=?", new String[]{blackContactInfo.phoneNumber});
        if (rownumber == 0) {//删除数据的判断
            return false;
        } else {
            return true;
        }
    }

    /**
     * 分页查询数据库的记录
     * pagenumber: 第几页，页码从0开始
     * pagesize: 每一个页面的大小
     */
    public List<BlackContactInfo> getPageBlackNumber(int pagenumber, int pagesize) {
        // 得到可读的数据库
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number, mode, name from blacknumber limit ? offset ?", new String[]{String.valueOf(pagesize), String.valueOf(pagesize * pagenumber)});
        List<BlackContactInfo> blacknumberInfo_list = new ArrayList<BlackContactInfo>();
        while (cursor.moveToNext()){
            BlackContactInfo info = new BlackContactInfo();
            info.phoneNumber = cursor.getString(0);
            info.mode = cursor.getInt(1);
            info.contactName = cursor.getString(2);
            blacknumberInfo_list.add(info);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);
        return blacknumberInfo_list;
    }
    /**
     * 判断号码是否在黑名单中存在
     */
    public boolean IsNumberExist(String number){
        //得到可读的数据库
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
    /**
     * 根据号码查询黑名单信息
     */
    public int getBlackContactMode(String number){
        //得到可读的数据库
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        int mode = 0;
        if (cursor.moveToNext()){
            mode = cursor.getInt(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }
    /**
     * 获取数据库的总条目个数
     */
    public int getTotalNum(){
        //得到可读的数据库
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count =cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
