package com.huahua.minalongconnect.minatest;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/3.
 *
 * 构建者模式
 */

public class ConnectionConfig {


    private Context context;
    private String ip;
    private int port;
    private int readBufferSize;
    private long connectionTimeout;

    public Context getContext() {
        return context;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    // 上面类的成员变量都copy过来，要给一个默认实现
    public static class Builder {
        private Context context;
        private String ip = "192.168.1.100";
        private int port = 9123;
        private int readBufferSize = 1024 * 10;
        private long connectionTimeout = 1000 * 10;

        public Builder(Context context) {
            this.context = context;
        }

        // 一堆set方法，将实例返回出去
        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        private void applyConfig(ConnectionConfig config) {
            config.context = this.context;
            config.ip = this.ip;
            config.port = this.port;
            config.connectionTimeout = this.connectionTimeout;
            config.readBufferSize = this.readBufferSize;
        }

        public ConnectionConfig builder() {
            ConnectionConfig config = new ConnectionConfig();
            applyConfig(config);

            return config;
        }
    }


}



















