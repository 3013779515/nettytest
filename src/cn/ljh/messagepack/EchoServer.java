package cn.ljh.messagepack;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoServer {

    private int port;

    public EchoServer(int port) {
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
                	 //LengthFieldBasedFrameDecoder���ڴ�������Ϣ
                     //���������MsgpackDecoder���յ���Զ��������Ϣ
                     ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                     ch.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
                     //��ByteBuf֮ǰ����2���ֽڵ���Ϣ�����ֶ�
                     ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2)); 
                     ch.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
                     ch.pipeline().addLast(new EchoServerHandler());
                 }
             });

            //�󶨶˿ڣ�ͬ���ȴ��ɹ�(ͬ����������sync)
            //ChannelFuture�����첽������֪ͨ�ص�
            ChannelFuture f = b.bind(port).sync();
            System.out.println("�����������˿�:"+port);
            
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
        new EchoServer(port).run();
    }
}