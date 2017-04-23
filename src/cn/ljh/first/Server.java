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
 * 第一个netty程序
 * 存在粘包拆包问题
 */
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
                     ch.pipeline().addLast(new ServerHandler());
                 }
             });

            //绑定端口，同步等待成功(同步阻塞方法sync)
            //ChannelFuture用于异步操作的通知回调
            ChannelFuture f = b.bind(port).sync();
            System.out.println("服务器监听端口："+port);
            
            //netty的多端口使用
            ChannelFuture f2 = b.bind(9091).sync();
            System.out.println("服务器监听端口2："+ 9091);
            
            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
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