package cn.ljh.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter {
	
	ChannelHandlerContext ctx;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
	}
	
	public boolean sendMsg(String msg){
		System.out.println("数据发送："+msg);
		byte[] req = msg.getBytes();
		ByteBuf m = Unpooled.buffer(req.length);
		m.writeBytes(req);
		ctx.writeAndFlush(m);
		return msg.equals("q")?false:true;
	}
	

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req,"utf-8");
		System.out.println("客户端收到结果:"+body);
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}