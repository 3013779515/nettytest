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
        	//netty��������nio�ͻ��˵ĸ���������
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	//Ϊ�˽��ճ����������⣬
                	
                	//1.ʹ��LineBasedFrameDecoder������,ԭ����ʹ�û��з���Ϊ��Ϣ��������
                	//ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                	
                	//2.ʹ��DelimiterBasedFrameDecoder������,ԭ���ǽ�����ķָ�����Ϊ��Ϣ�Ľ�����־
                	ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                	
                	//����Ϣת����String����
                	ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(client);
                }
            });
            //�󶨶˿ڣ�ͬ���ȴ��ɹ�(ͬ����������sync)
            //ChannelFuture�����첽������֪ͨ�ص�
            ChannelFuture f = b.connect(host, port).sync();
            //�ȴ������������˿ڹر�
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//�����˳����ͷ��̳߳���Դ
            workerGroup.shutdownGracefully();
        }
	}
}