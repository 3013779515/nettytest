package cn.ljh.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
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
		ByteBuf in = (ByteBuf) msg;
		byte[] req = new byte[in.readableBytes()];
		in.readBytes(req);
		String body = new String(req,"utf-8");
		System.out.println("�������յ���Ϣ:"+body);
		String calrResult = null;
		try{
			calrResult = Calculator.Instance.cal(body).toString();
		}catch(Exception e){
			calrResult = "�������" + e.getMessage();
		}
		//�첽����Ӧ����Ϣ���ͻ���
		ctx.write(Unpooled.copiedBuffer(calrResult.getBytes()));
		//������Ϻ�ͶϿ����ӣ������Ӷ����ӵ�ʹ�ã�
		//.addListener(ChannelFutureListener.CLOSE);
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