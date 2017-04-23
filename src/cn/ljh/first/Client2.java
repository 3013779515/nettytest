package cn.ljh.first;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
/**
 * 第一个netty程序
 * 存在粘包拆包问题
 */
public class Client2 implements Runnable{
	
    static ClientHandler client = new ClientHandler();
    
    public static void main(String[] args) throws Exception {
    	new Thread(new Client2()).start();
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
                    ch.pipeline().addLast(client);
                }
            });
            
            //绑定端口，同步等待成功(同步阻塞方法sync)
            //演示netty的多端口使用
            ChannelFuture f2 = b.connect(host, 9091).sync();
            f2.channel().writeAndFlush(Unpooled.copiedBuffer(("2+2").getBytes()));
            
            
            //等待服务器监听端口关闭
            f2.channel().closeFuture().sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
        }
	}
}