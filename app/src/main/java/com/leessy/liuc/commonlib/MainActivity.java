package com.leessy.liuc.commonlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.AiChlFace.AiChlFace;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.serial.SerialReadClient;
import com.leessy.liuc.commonlibs.TestOBS;
import com.leessy.liuc.commonlibs.TimeUilts;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SerialReadClient seri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TimeUilts.showDialog(this, "hello!!");
//        new AlertDialog.Builder(this).setTitle("???").setMessage(TimeUilts.getNowDate()).show();
        Observable<Integer> getobs = TestOBS.getobs();
        getobs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept: =" + integer);
            }
        });

        seri = TimeUilts.getSeri();
        int i = seri.openSerialPort("/dev/ttySAC0", 115200);
        Log.d(TAG, "onCreate: 卡尔启动=" + i);

    }

    public void test(View view) {
        IDCardItem item = seri.readCertWithoutNet();//本地
        Log.d(TAG, "test: 读卡结果=" + item.toString());
    }
}
