package com.imooc.netty.ch3;

import com.imooc.netty.ch6.AuthHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

/**
 *  netty启动标准的demo
 */
public final class Server {

    public static void main(String[] args) throws Exception {
        //对应Server中的线程  服务端
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //对应Client中的线程 客户端
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            //配置两大基本线程
            b.group(bossGroup, workerGroup)
                    //设置服务端的SocketChannel
                    .channel(NioServerSocketChannel.class)
                    //为每一条客户端的链接设置tcp属性
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //每次创建客户端链接的时候，绑定一些属性
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    //服务端启动对应的逻辑   对应   Socket client = serverSocket.accept();
                    .handler(new ServerHandler())
                    //
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new AuthHandler());
                            //..对每一条新链接的处理逻辑 数据读写

                        }
                    });
            //端口的绑定
            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}