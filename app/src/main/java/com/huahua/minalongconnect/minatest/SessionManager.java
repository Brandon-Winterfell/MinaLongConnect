package com.huahua.minalongconnect.minatest;

import org.apache.mina.core.session.IoSession;

/**
 * Created by Administrator on 2017/3/3.
 *
 * SessionManager 单例对象，持有一个Session的引用，就是成员变量。
 * 然后就能通过Session对象进行所需要的操作啦。
 */

public class SessionManager {

    private static SessionManager mInstance = null;

    /**
     * 最终与服务器进行通信的对象
     */
    private IoSession mSession;

    public static SessionManager getInstance() {
        if (mInstance == null) {
            synchronized (SessionManager.class) {
                if (mInstance == null) {
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager() {

    }

    public void setSession(IoSession session) {
        mSession = session;
    }

    /**
     * 将对象写到服务器
     * @param msg
     */
    public void writeToServer(Object msg) {
        if (mSession != null) {
            mSession.write(msg);
        }
    }

    public void closeSession() {
        if (mSession != null) {
            mSession.closeOnFlush();
        }
    }

    public void removeSession() {
        this.mSession = null;
    }
}
























