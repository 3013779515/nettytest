package cn.ljh.decoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.ljh.utils.Calculator;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		//����StringDecoder�Ľ����msg�Ѿ���String���͡�
		System.out.println("�������յ���Ϣ:"+msg);
		
		String calrResult = null;
		try{
			calrResult = Calculator.Instance.cal(((String)msg).trim()).toString();
		}catch(Exception e){
			calrResult = "�������" + e.getMessage();
		}
		//1.��ӦLineBasedFrameDecoder�����������ϻ��з�
		//calrResult = calrResult + System.getProperty("line.separator");
		//2.��ӦDelimiterBasedFrameDecoder������������$_�ָ���
		calrResult = calrResult + "$_";
		//�첽����Ӧ����Ϣ���ͻ���
		ctx.write(Unpooled.copiedBuffer(calrResult.getBytes()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}
}