package com.runcode.server.websocket;

import com.runcode.server.websocket.channelInitializer.WebSocketChannelInitializer;
import com.runcode.utils.UserConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * websocket服务器
 *
 * @author JR
 */
@Slf4j
public class WebsocketServer {

    /**
     * 绑定端口并启动服务器
     *
     * @throws Exception
     */
    public void startup() throws Exception {
        //配置服务器的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new WebSocketChannelInitializer());
            log.info("---------websocket服务器正在启动---------");
            ChannelFuture future = serverBootstrap.bind(UserConfig.WS_PORT).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
