package com.huahua.minalongconnect.minatest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/3.
 */

public class MinaService extends Service {

    private ConnectionThread mConnectionThread;

    @Override
    public void onCreate() {
        super.onCreate();

        mConnectionThread = new ConnectionThread("mina", getApplicationContext());
        mConnectionThread.start(); // 启动

        Log.i("mina", "连接服务器的线程已经启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mConnectionThread.disConnection(); // 断开连接
        /**
         * 没有停掉线程，如果没有连接上的话，会一直尝试连接
         */
        mConnectionThread = null;  // 释放掉线程
        super.onDestroy();
    }

    /**
     * 负责调用connection manager类来完成与服务器的连接
     */
    class ConnectionThread extends HandlerThread {

        private Context mContext;
        boolean isConnection;
        ConnectionManager mManager;

        public ConnectionThread(String name, Context context) {
            super(name);
            this.mContext = context;

            ConnectionConfig config = new ConnectionConfig.Builder(mContext)
                    .setIp("192.168.2.102")
                    .setPort(9123)
                    .setConnectionTimeout(1024 * 10)
                    .setReadBufferSize(1000 * 10)
                    .builder();

            mManager = new ConnectionManager(config);
        }

        // 相当于thread的run方法，开始连接我们的服务器
        @Override
        protected void onLooperPrepared() {
            for (; ; ) { // 死循环
                isConnection = mManager.connect();  // 完成服务器的连接
                if (isConnection) {
                    Log.i("mina", "已经连接上");
                    break;  // 连接成功后跳出死循环
                } else {
                    // 每隔3秒去循环一次
                    try {
                        Log.i("mina", "没有连接上，休眠3秒");
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 断开连接的方法
         */
        public void disConnection() {
            mManager.disConnect(); // 完成与服务器的断开操作
        }
    }
}




















