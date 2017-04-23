package cn.ljh.decoder;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;

public class ClientHandler extends ChannelHandlerAdapter {
	
	ChannelHandlerContext ctx;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		char operators[] = {'+','-','*','/'};
		Random random = new Random(System.currentTimeMillis());
		
		//循环发送消息，模拟粘包拆包问题。
		for(int i = 0 ; i < 10 ; i ++){
			String expression = random.nextInt(100) + ""
					+ operators[random.nextInt(4)]+(random.nextInt(100)+1);
			sendMsg(expression);
		}
	}
	
	public boolean sendMsg(String msg){
		if(!msg.equals("q")){
			System.out.println("数据发送："+msg);
			//1.对应服务端LineBasedFrameDecoder解码器，加上换行符
			//msg = msg + System.getProperty("line.separator");
			//2.对应服务端DelimiterBasedFrameDecoder解码器，加上$_分隔符
			//msg = msg + "$_";
			//3.对应服务端解码器，进行位数补齐
			StringBuffer sBuffer = new StringBuffer();
			if(msg.length() < 20){
				for(int i = 0; i < 20 - msg.length(); i++ ){
					sBuffer.append(" ");
				}
			}
			msg = msg + sBuffer;
			
			byte[] req = msg.getBytes();
			ByteBuf m = Unpooled.buffer(req.length);
			m.writeBytes(req);
			ctx.writeAndFlush(m);
			return true;
		}else{
			return false;
		}
	}
	

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	//经过StringDecoder的解码后msg已经是String类型。
		System.out.println("客户端收到结果:"+msg);
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}