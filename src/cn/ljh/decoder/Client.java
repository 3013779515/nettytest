package cn.ljh.decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Scanner;

public class Client implements Runnable{
	
    static ClientHandler client = new ClientHandler();
    
    public static void main(String[] args) throws Exception {
    	new Thread(new Client()).start();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(client.sendMsg(scanner.nextLine()));
    }

	@Override
	public void run() {
        String host = "127.0.0.1";
        int port = 9090;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	//netty用于启动nio客户端的辅助启动类
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	//为了解决粘包拆包的问题，
                	
                	//1.使用LineBasedFrameDecoder解码器,原理是使用换行符作为消息结束符。
                	//ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                	
                	//2.使用DelimiterBasedFrameDecoder解码器,原理是将特殊的分隔符作为消息的结束标志
                	ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                	
                	//把消息转换成String类型
                	ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(client);
                }
            });
            //绑定端口，同步等待成功(同步阻塞方法sync)
            //ChannelFuture用于异步操作的通知回调
            ChannelFuture f = b.connect(host, port).sync();
            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
        }
	}
}