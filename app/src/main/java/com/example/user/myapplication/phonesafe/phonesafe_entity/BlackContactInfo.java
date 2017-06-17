package com.example.user.myapplication.phonesafe.phonesafe_entity;

/**
 * Created by user on 2016/7/15.
 * author: King
 * description: 保存黑名单中的联系人信息的实体类，包含三个属性：电话号码、联系人姓名、拦截模式
 */
public class BlackContactInfo {
    //黑名单号码
    public String phoneNumber;
    //黑名单联系人姓名
    public String contactName;
    /* 黑名单拦截模式：1.电话拦截 2.短信拦截 3.电话、短信都拦截 */
    public int mode;
    /*
    * 定义三种拦截模式的返回值，由于黑名单有三种模式，因此以返回值1/2/3代表不同的拦截模式
    * */
    public String getModeString(int mode){
        switch(mode)
        {
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话、短信都拦截";
        }
        return "";
    }
}
