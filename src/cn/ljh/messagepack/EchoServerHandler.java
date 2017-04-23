package cn.ljh.messagepack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.ljh.utils.UserInfo;

/**
 * Handles a server-side channel.
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		UserInfo uInfo = (UserInfo)msg;
		System.out.println("������յ���" + uInfo.toString());
		uInfo.setName(uInfo.getName()+"*");
		//�첽����Ӧ����Ϣ���ͻ���
		ctx.writeAndFlush(uInfo);
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