package com.imooc.netty.ch2;

/**
 * @author 闪电侠
 */
public class ServerBoot {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        //通过构造函数进行初始化  创建一个 serverSocket
        Server server = new Server(PORT);
        //启动一个线程 ，创建ClientHandler，处理客户端链接 数据接收与处理工作
        server.start();
    }

}
