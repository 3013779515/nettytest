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
    	//一组NIO线程用于服务端接收客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //一组NIO线程用于进行socketchannel的网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	//netty用于启动nio服务端的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 1024)
             .childOption(ChannelOption.SO_KEEPALIVE, true)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 //为了解决粘包拆包的问题，
                	 
                 	 //1.使用LineBasedFrameDecoder解码器,原理是使用换行符作为消息结束符。
                	 //ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                	 
                	 //2.使用DelimiterBasedFrameDecoder解码器,原理是将特殊的分隔符作为消息的结束标志
                	 //ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                	 //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                	 //3.使用FixedLengthFrameDecoder解码器，原理是对固定长度的消息进行自动解码
                	 ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                	 //把消息转换成String类型
                	 ch.pipeline().addLast(new StringDecoder());
                     ch.pipeline().addLast(new ServerHandler());
                 }
             });

            //绑定端口，同步等待成功(同步阻塞方法sync)
            //ChannelFuture用于异步操作的通知回调
            ChannelFuture f = b.bind(port).sync();
            System.out.println("服务器监听端口"+port);
            
            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
        	//优雅退出，释放线程池资源
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