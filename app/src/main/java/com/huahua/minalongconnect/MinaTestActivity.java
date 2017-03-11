package com.huahua.minalongconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huahua.minalongconnect.minatest.ConnectionManager;
import com.huahua.minalongconnect.minatest.MinaService;
import com.huahua.minalongconnect.minatest.SessionManager;

/**
 * 使用mina实现与服务器长连接的功能，demo级别，需要写一个服务端配合的
 */
public class MinaTestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mConnectView, mSendView;

    private Button mButton;

    // 自定义一个广播接收器来接收服务器发来的数据
    // 其实是用自定义的广播-->>将服务器发来的数据广播出来.
    // 局部广播安全高效，外界的app接收不到
    private MessageBroadcastReceiver mReceiver =
            new MessageBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectView = (TextView)findViewById(R.id.start_service);
        mConnectView.setOnClickListener(this);
        mSendView = (TextView)findViewById(R.id.send);
        mSendView.setOnClickListener(this);

        mButton = (Button)findViewById(R.id.stop_service);
        mButton.setOnClickListener(this);

        registerBroadcast();

    }

    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter(ConnectionManager.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    public void unRegisterBroadcast() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MinaService.class)); // 停掉服务
        unRegisterBroadcast(); // 解绑广播

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent intent = new Intent(this, MinaService.class);
                startService(intent);
                break;
            case R.id.send:
                // 发送数据
                Toast.makeText(this, "fuck you", Toast.LENGTH_SHORT).show();
                SessionManager.getInstance().writeToServer("Hello Mina fuck you");
                break;
            case R.id.stop_service:
                stopService(new Intent(this, MinaService.class)); // 停掉服务
                break;
        }
    }

    private class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 更新activity的title表示收到数据
            setTitle(intent.getStringExtra("message"));
        }
    }
}
