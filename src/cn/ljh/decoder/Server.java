package cn.ljh.decoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

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
                	 //Ϊ�˽��ճ����������⣬
                	 
                 	 //1.ʹ��LineBasedFrameDecoder������,ԭ����ʹ�û��з���Ϊ��Ϣ��������
                	 //ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                	 
                	 //2.ʹ��DelimiterBasedFrameDecoder������,ԭ���ǽ�����ķָ�����Ϊ��Ϣ�Ľ�����־
                	 //ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                	 //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                	 //3.ʹ��FixedLengthFrameDecoder��������ԭ���ǶԹ̶����ȵ���Ϣ�����Զ�����
                	 ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                	 //����Ϣת����String����
                	 ch.pipeline().addLast(new StringDecoder());
                     ch.pipeline().addLast(new ServerHandler());
                 }
             });

            //�󶨶˿ڣ�ͬ���ȴ��ɹ�(ͬ����������sync)
            //ChannelFuture�����첽������֪ͨ�ص�
            ChannelFuture f = b.bind(port).sync();
            System.out.println("�����������˿�"+port);
            
            //�ȴ������������˿ڹر�
            f.channel().closeFuture().sync();
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