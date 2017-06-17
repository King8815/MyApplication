package com.example.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/8/10.
 */
public class Tools {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
     * 格式化价格
     */
    public static String getFloatDotStr(String argStr) {
        float arg = Float.valueOf(argStr);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(arg);
    }

    /**
     * 判断当前网络状态
     * @param context
     * @return
     */
    public static boolean isHaveInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }

    // 得到versionName

    /**
     * 获得版本名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;

    }
    /**
     * 把一个毫秒数转化成字符串
     * */
    public static String millisToString(long millis) {
        boolean negative = millis < 0;
        millis = java.lang.Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat
                .getInstance(Locale.US);
        format.applyPattern("00");
        if (millis > 0) {
            time = (negative ? "-" : "")
                    + (hours == 0 ? 00 : hours < 10 ? "0" + hours : hours)
                    + ":" + (min == 0 ? 00 : min < 10 ? "0" + min : min) + ":"
                    + (sec == 0 ? 00 : sec < 10 ? "0" + sec : sec);
        } else {
            time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

    // 得到versionName
    public static int getVerCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;

    }

    /**
     * 判断 多个字段的值否为空
     *
     * @author Michael.Zhang 2013-08-02 13:34:43
     * @return true为null或空; false不null或空
     */
    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("")
                    || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     * @param s
     * @return
     */
    public static boolean isNull(String s) {
        if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    /**
     * @return 检查是否存在SDcard
     */
    public static boolean hasSdcard(){
        String sdcardstate = Environment.getExternalStorageState();
        if (sdcardstate.equals(Environment.MEDIA_MOUNTED)) {
            //有存储的SDCard
            return true;
        } else {
            Log.i("wzl", "没有检测到SDCard！");
            return false;
        }
    }

    /**
     * 显示纯汉字的星期名称
     *
     * @author TangWei 2013-10-25上午11:31:51
     * @param i
     *            星期：1,2,3,4,5,6,7
     * @return
     */
    public static String changeWeekToHanzi(int i) {
        switch (i) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    /**
     * 验证手机号码
     *
     * @author TangWei
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher matcher = p.matcher(phone);
        return matcher.matches();
    }

    public static boolean validateLoginPassWord(String pwd) {
        if (isNull(pwd))
            return false;
        String pattern = "[a-zA-Z0-9]{6,16}";
        return pwd.matches(pattern);

    }

    /**
     * 检验用户名 可以输入a到z 0到9 汉字的3到8位字符
     *
     * @param pwd
     * @return
     */
    public static boolean validateUserName(String pwd) {
        if (isNull(pwd))
            return false;
        String pattern = "[a-zA-Z0-9\u4E00-\u9FA5]{3,8}";
        return pwd.matches(pattern);

    }

    /**
     * 检查身份证是 否合法,15位或18位(或者最后一位为X)
     *
     * @param
     * @return
     */
    public static boolean validateIdCard(String idCard) {
        if (isNull(idCard)) {
            return false;
        }
        return idCard.matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
    }

    /**
     * 简单的验证一下银行卡号
     *
     * @param bankCard
     *            信用卡是16位，其他的是13-19位
     * @return
     */
    public static boolean validateBankCard(String bankCard) {
        if (isNull(bankCard))
            return false;
        String pattern = "^\\d{13,19}$";
        return bankCard.matches(pattern);
    }

    /**
     * 验证邮箱
     *
     * @author TangWei 2013-12-13下午2:33:16
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        if (isNull(email))
            return false;
        String pattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        return email.matches(pattern);
    }

    /**
     * 将100以内的阿拉伯数字转换成中文汉字（15变成十五）
     *
     * @param round
     *            最大值50
     * @return >99的，返回“”
     */
    public static String getHanZi1(int round) {
        if (round > 99 || round == 0) {
            return "";
        }
        int ge = round % 10;
        int shi = (round - ge) / 10;
        String value = "";
        if (shi != 0) {
            if (shi == 1) {
                value = "十";
            } else {
                value = getHanZi2(shi) + "十";
            }

        }
        value = value + getHanZi2(ge);
        return value;
    }

    /**
     * 将0-9 转换为 汉字（ _一二三四五六七八九）
     *
     * @param round
     * @return
     */
    public static String getHanZi2(int round) {
        String[] value = { "", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        return value[round];
    }

    /**
     * 将服务器返回的日期转换为固定日期
     *
     * @param str
     * @return
     */
    public static String convertoZPMTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        if (date != null) {
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy.M.dd");
            result = format2.format(date);
        }

        return result;

    }

    /**
     * 将服务器返回的日期转换为优惠券需要显示的日期
     *
     * @param str
     * @return
     */
    public static String convertoCouponTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        if (date != null) {
            SimpleDateFormat format2 = new SimpleDateFormat("M/dd");
            result = format2.format(date);
        }

        return result;

    }
    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取当前时间
     * 使用此方法：String curentTime = getCurrentTime();
     * 使用currentTime
     */
    public String getCurrentTime() {
        SimpleDateFormat formatter = null;
        formatter = new SimpleDateFormat("HHmmss", Locale.CHINA);

        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String currentTime = formatter.format(curDate);
        Log.e("wzl","currentTime = " + currentTime);
        return currentTime;
    }

    /**
     * Toast 功能简写
     * @param str
     */
    public static void toast(Context context, final String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

}
