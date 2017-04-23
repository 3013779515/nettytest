package cn.ljh.protobuf;

import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioSocketChannel;  
import io.netty.handler.codec.protobuf.ProtobufDecoder;  
import io.netty.handler.codec.protobuf.ProtobufEncoder;  
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;  
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;  
  
/**  
 * Created by vixuan-008 on 2015/6/24.  
 */  
public class SubReqProClient {  
    public static void main(String[] args)throws Exception{  
        int port=15444;  
        new SubReqProClient().bind(port, "127.0.0.1");  
    }  
  
    public void bind(int port,String host)throws Exception{  
        //配置客户端NIO线程池  
        EventLoopGroup workGroup=new NioEventLoopGroup();  
        try{  
            io.netty.bootstrap.Bootstrap b=new io.netty.bootstrap.Bootstrap();  
            b.group(workGroup);  
            b.channel(NioSocketChannel.class);  
            b.option(ChannelOption.TCP_NODELAY,true);  
            b.handler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                protected void initChannel(SocketChannel socketChannel) throws Exception {  
                	//使用Netty提供的ProtobufVarint32FrameDecoder，它可以处理半包消息
                    socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    //解码器。参数告诉需要解码的目标类
                    socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance())); 
                    
                    //和解码半包处理器配套使用
                    socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender()); 
                    //编码器
                    socketChannel.pipeline().addLast(new ProtobufEncoder());  
                    socketChannel.pipeline().addLast(new SubReqProHandler());  
                }  
            });  
            //发起异步连接操作  
            ChannelFuture f=b.connect(host,port).sync();  
            //等待客户端链路关闭  
            f.channel().closeFuture().sync();  
  
        }finally {  
            //释放NIO 线程组  
            workGroup.shutdownGracefully();  
  
        }  
  
  
    }  
}
