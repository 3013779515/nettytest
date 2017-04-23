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
		
		//ѭ��������Ϣ��ģ��ճ��������⡣
		for(int i = 0 ; i < 10 ; i ++){
			String expression = random.nextInt(100) + ""
					+ operators[random.nextInt(4)]+(random.nextInt(100)+1);
			sendMsg(expression);
		}
	}
	
	public boolean sendMsg(String msg){
		if(!msg.equals("q")){
			System.out.println("���ݷ��ͣ�"+msg);
			//1.��Ӧ�����LineBasedFrameDecoder�����������ϻ��з�
			//msg = msg + System.getProperty("line.separator");
			//2.��Ӧ�����DelimiterBasedFrameDecoder������������$_�ָ���
			//msg = msg + "$_";
			//3.��Ӧ����˽�����������λ������
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
    	//����StringDecoder�Ľ����msg�Ѿ���String���͡�
		System.out.println("�ͻ����յ����:"+msg);
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}