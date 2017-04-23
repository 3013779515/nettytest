package cn.ljh.first;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * ��һ��netty����
 * ����ճ���������
 */
public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
    	//һ��NIO�߳����ڷ���˽��տͻ��˵�����
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //һ��NIO�߳����ڽ���socketchannel�������д
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	//netty��������nio����˵ĸ���������
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 1024)
             .childOption(ChannelOption.SO_KEEPALIVE, true)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new ServerHandler());
                 }
             });

            //�󶨶˿ڣ�ͬ���ȴ��ɹ�(ͬ����������sync)
            //ChannelFuture�����첽������֪ͨ�ص�
            ChannelFuture f = b.bind(port).sync();
            System.out.println("�����������˿ڣ�"+port);
            
            //netty�Ķ�˿�ʹ��
            ChannelFuture f2 = b.bind(9091).sync();
            System.out.println("�����������˿�2��"+ 9091);
            
            //�ȴ������������˿ڹر�
            f.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
        } finally {
        	//�����˳����ͷ��̳߳���Դ
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 9090;
        }
        new Server(port).run();
    }
}