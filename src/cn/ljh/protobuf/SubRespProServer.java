package cn.ljh.protobuf;

import io.netty.bootstrap.ServerBootstrap;  
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioServerSocketChannel;  
import io.netty.handler.codec.protobuf.ProtobufDecoder;  
import io.netty.handler.codec.protobuf.ProtobufEncoder;  
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;  
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;  
import io.netty.handler.logging.LogLevel;  
import io.netty.handler.logging.LoggingHandler;  

/**  
* Created by vixuan-008 on 2015/6/24.  
*/  
public class SubRespProServer {  
  public static void main(String[] args)throws  Exception{  
      int port=15444;  
      new SubRespProServer().bind(port);  

  }  

  public void bind(int port)throws Exception{  
      //���÷���˵�NIO�̳߳�  
      EventLoopGroup bossGroup=new NioEventLoopGroup();  
      EventLoopGroup workGroup=new NioEventLoopGroup();  
      try{  
          ServerBootstrap b=new ServerBootstrap();  
          b.group(bossGroup, workGroup);  
          b.channel(NioServerSocketChannel.class);  
          b.option(ChannelOption.SO_BACKLOG, 100);  
          //b.handler(new LoggingHandler(LogLevel.INFO));  
          b.childHandler(new ChannelInitializer<SocketChannel>() {  
              @Override  
              protected void initChannel(SocketChannel socketChannel) throws Exception {  
            	  //ʹ��Netty�ṩ��ProtobufVarint32FrameDecoder�������Դ�������Ϣ
                  socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());  
                  //������������������Ҫ�����Ŀ����
                  socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));  
                  
                  //�ͽ���������������ʹ��
                  socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());  
                  socketChannel.pipeline().addLast(new ProtobufEncoder());  
                  socketChannel.pipeline().addLast(new SubRespProHandler());  

              }  
          });  
          //�󶨶˿ڣ��ȴ�ͬ���ɹ�  
          ChannelFuture f=b.bind(port).sync();  
          //�ȴ�����˹رռ����˿�  
          f.channel().closeFuture().sync();  

      }finally {  
          //�ͷ��̳߳���Դ  
          bossGroup.shutdownGracefully();  
          workGroup.shutdownGracefully();  

      }  
  }  
}  