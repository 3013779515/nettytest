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
		
		//经过StringDecoder的解码后msg已经是String类型。
		System.out.println("服务器收到消息:"+msg);
		
		String calrResult = null;
		try{
			calrResult = Calculator.Instance.cal(((String)msg).trim()).toString();
		}catch(Exception e){
			calrResult = "计算错误：" + e.getMessage();
		}
		//1.对应LineBasedFrameDecoder解码器，加上换行符
		//calrResult = calrResult + System.getProperty("line.separator");
		//2.对应DelimiterBasedFrameDecoder解码器，加上$_分隔符
		calrResult = calrResult + "$_";
		//异步发送应答消息给客户端
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