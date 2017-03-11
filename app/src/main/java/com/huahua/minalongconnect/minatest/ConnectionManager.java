package com.huahua.minalongconnect.minatest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2017/3/3.
 *
 * function: 封装好connection dissconnection 方法供外层调用
 */

public class ConnectionManager {

    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnector;
    private IoSession mSession;
    private InetSocketAddress mAddress;

    public static final String BROADCAST_ACTION = "com.commonlibrary.mina";
    private static final String MESSAGE = "message";

    public ConnectionManager(ConnectionConfig connectionConfig) {
        mConfig = connectionConfig;
        mContext = new WeakReference<Context>(connectionConfig.getContext());

        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());

        mConnector = new NioSocketConnector();
        mConnector.setDefaultRemoteAddress(mAddress);
        mConnector.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        mConnector.getFilterChain().addLast(
                "logging", new LoggingFilter()
        );
        mConnector.getFilterChain().addLast(
                "codec", new ProtocolCodecFilter(
                        new ObjectSerializationCodecFactory()
                )
        );
        // 设置业务处理类
        mConnector.setHandler(new MinaDefaultHandler(mContext.get()));
    }

    /**
     * 外层调用取得与服务器的连接
     * @return
     */
    public boolean connect() {
        try {
            ConnectFuture future = mConnector.connect();
            future.awaitUninterruptibly();  // 阻塞直到连接上吗？
            mSession = future.getSession();

            SessionManager.getInstance().setSession(mSession);
        } catch (Exception e) {
            return false;
        }

        return mSession != null;
    }

    /**
     * 外层调用 断开与服务器连接的方法
     */
    public void disConnect() {
        mConnector.dispose();
        mConnector = null;
        mSession = null;
        mAddress = null;
        mContext = null;
    }

    /**
     * 事件处理
     */
    private static class MinaDefaultHandler extends IoHandlerAdapter {
        private Context mContext;

        MinaDefaultHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
            // 将我们的session保存到我们的session manager类中，从而可以发送消息到服务器
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {

            if (mContext != null) {
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra(MESSAGE, message.toString());
                // 利用局部广播将数据发送出去（给本身这个应用的处理器处理）
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }
    }

}



















