package com.leessy.liuc.commonlibs;

import android.app.Activity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TimeUilts {
    /**
     * 获取当前时间
     */
    public static String getNowDate() {
        return "2018/07/04 11:40:20";
    }

    public static void showDialog(Activity context, String msg) {
        new SweetAlertDialog(context).setCancelText(msg).show();
    }
}
