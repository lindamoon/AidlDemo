package com.feima.huba.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * @author lixb
 * @version 1.0
 * @date 2016/10/17 22:07
 */
public class MyAIDLServer extends Service {

    private static final int MESSAGE_RECEIVE_DATA = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_DATA:
                    Toast.makeText(getApplicationContext(), "收到数据"+String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private IMyAidlInterface.Stub mStub = new IMyAidlInterface.Stub() {
        @Override
        public void processString(String json) throws RemoteException {
            Log.e("haha", "收到数据"+json);
            mHandler.obtainMessage(MESSAGE_RECEIVE_DATA,json).sendToTarget();
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }
}
