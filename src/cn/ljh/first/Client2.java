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
 * ��һ��netty����
 * ����ճ���������
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
        	//netty��������nio�ͻ��˵ĸ���������
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
            
            //�󶨶˿ڣ�ͬ���ȴ��ɹ�(ͬ����������sync)
            //��ʾnetty�Ķ�˿�ʹ��
            ChannelFuture f2 = b.connect(host, 9091).sync();
            f2.channel().writeAndFlush(Unpooled.copiedBuffer(("2+2").getBytes()));
            
            
            //�ȴ������������˿ڹر�
            f2.channel().closeFuture().sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//�����˳����ͷ��̳߳���Դ
            workerGroup.shutdownGracefully();
        }
	}
}