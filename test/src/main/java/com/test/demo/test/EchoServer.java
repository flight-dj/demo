package com.test.demo.test;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty服务端
 */
public class EchoServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer(){
        //1.定义server启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //2.定义工作组:boss分发请求给各个worker:boss负责监听端口请求，worker负责处理请求（读写）
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        //3.定义工作组
        serverBootstrap.group(boss,worker);

        //4.设置通道channel
        serverBootstrap.channel(NioServerSocketChannel.class);//A
        //serverBootstrap.channelFactory(new ReflectiveChannelFactory(NioServerSocketChannel.class));//旧版本的写法，但是此过程在A中有同样过程

        //5.添加handler，管道中的处理器，通过ChannelInitializer来构造
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //此方法每次客户端连接都会调用，是为通道初始化的方法

                //获得通道channel中的管道链（执行链、handler链）
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new StringDecoder());
                pipeline.addLast("serverHandler1",new ServerHandler());
                pipeline.addLast("serverHandler2",new ServerHandler2());
                pipeline.addLast(new StringEncoder());

                System.out.println("success to initHandler!");
            }
        });

        //6.设置参数
        //设置参数，TCP参数
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);         //连接缓冲池的大小
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送

        //7.绑定ip和port
        try {
            ChannelFuture channelFuture = serverBootstrap.bind("0.0.0.0", 9099).sync();//Future模式的channel对象
            //7.5.监听关闭
            channelFuture.channel().closeFuture().sync();  //等待服务关闭，关闭后应该释放资源
        } catch (InterruptedException e) {
            System.out.println("server start got exception!");
            e.printStackTrace();
        }finally {
            //8.优雅的关闭资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}

class ServerHandler extends ChannelInboundHandlerAdapter {

    /*
     * ChannelInboundHandlerAdapter：ChannelInboundHandlerAdapter是ChannelInboundHandler的一个简单实现，默认情况下不会做任何处理，
     *   只是简单的将操作通过fire*方法传递到ChannelPipeline中的下一个ChannelHandler中让链中的下一个ChannelHandler去处理。
     *
     * SimpleChannelInboundHandler：SimpleChannelInboundHandler支持泛型的消息处理，默认情况下消息处理完将会被自动释放，无法提供
     *   fire*方法传递给ChannelPipeline中的下一个ChannelHandler,如果想要传递给下一个ChannelHandler需要调用ReferenceCountUtil#retain方法。
     * */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ServerHandler receive msg:"+msg.toString());

        //写消息：先得到channel，在写如通道然后flush刷新通道把消息发出去。
        ctx.channel().writeAndFlush("this is ServerHandler reply msg happend at !"+System.currentTimeMillis());

        //把消息往下一个Handler传
        ctx.fireChannelRead(msg);
    }
}

class ServerHandler2  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ServerHandler2 receive msg:"+msg.toString());
        ctx.channel().writeAndFlush("this is ServerHandler2 reply msg happend at !"+System.currentTimeMillis());
    }
}