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
        //���ÿͻ���NIO�̳߳�  
        EventLoopGroup workGroup=new NioEventLoopGroup();  
        try{  
            io.netty.bootstrap.Bootstrap b=new io.netty.bootstrap.Bootstrap();  
            b.group(workGroup);  
            b.channel(NioSocketChannel.class);  
            b.option(ChannelOption.TCP_NODELAY,true);  
            b.handler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                protected void initChannel(SocketChannel socketChannel) throws Exception {  
                	//ʹ��Netty�ṩ��ProtobufVarint32FrameDecoder�������Դ�������Ϣ
                    socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    //������������������Ҫ�����Ŀ����
                    socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance())); 
                    
                    //�ͽ���������������ʹ��
                    socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender()); 
                    //������
                    socketChannel.pipeline().addLast(new ProtobufEncoder());  
                    socketChannel.pipeline().addLast(new SubReqProHandler());  
                }  
            });  
            //�����첽���Ӳ���  
            ChannelFuture f=b.connect(host,port).sync();  
            //�ȴ��ͻ�����·�ر�  
            f.channel().closeFuture().sync();  
  
        }finally {  
            //�ͷ�NIO �߳���  
            workGroup.shutdownGracefully();  
  
        }  
  
  
    }  
}
