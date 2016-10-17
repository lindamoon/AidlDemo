package com.feima.huba.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.feima.huba.aidldemo.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_CONNECTED = 1;
    private static final int MESSAGE_DISCONNECTED = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CONNECTED:
                    Toast.makeText(MainActivity.this, "service connected", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_DISCONNECTED:
                    Toast.makeText(MainActivity.this, "service disconnected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mHandler.obtainMessage(MESSAGE_CONNECTED).sendToTarget();
            mMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mHandler.obtainMessage(MESSAGE_DISCONNECTED).sendToTarget();
        }
    };
    private IMyAidlInterface mMyAidlInterface;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtInput = (EditText) findViewById(R.id.et_input);
    }

    public void bindService(View view) {
        Intent intent = new Intent();
        //android 5.0以后直设置action不能启动相应的服务，需要设置packageName或者Component。
        intent.setAction("com.fm.myService");
        intent.setComponent(new ComponentName("com.feima.huba.aidldemo", "com.feima.huba.aidldemo.MyAIDLServer"));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void sendMsg(View view) {

        String s = mEtInput.getText().toString();
        if (mMyAidlInterface != null) {
            try {
                mMyAidlInterface.processString(s);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
