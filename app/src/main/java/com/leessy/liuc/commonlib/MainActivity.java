package com.leessy.liuc.commonlib;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leessy.liuc.commonlibs.TimeUilts;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimeUilts.showDialog(this, "hello!!");
        new AlertDialog.Builder(this).setTitle("???").setMessage(TimeUilts.getNowDate()).show();
    }
}
