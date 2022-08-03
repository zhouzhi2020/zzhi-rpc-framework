package com.zzhi.server;

/**
 * 服务器接口
 *
 * @author zzhi
 * @date 2022/08/02
 */
public interface Server {
    /**
     * 启动服务器
     *
     * @param port 端口号
     */
    void start(int port);

    /**
     * 停止服务器
     */
    void stop();
}
