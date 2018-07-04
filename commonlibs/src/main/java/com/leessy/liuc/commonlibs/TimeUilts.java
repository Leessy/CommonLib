package com.leessy.liuc.commonlibs;

import android.app.Activity;

import com.kaer.sdk.serial.SerialReadClient;

public class TimeUilts {
    /**
     * 获取当前时间
     */
    public static String getNowDate() {
        return "2018/07/04 11:40:20";
    }

    public static void showDialog(Activity context, String msg) {
//        new SweetAlertDialog(context).setCancelText(msg).show();
    }

    public static SerialReadClient getSeri() {
        SerialReadClient instance = SerialReadClient.getInstance();
        return instance;
    }
}
