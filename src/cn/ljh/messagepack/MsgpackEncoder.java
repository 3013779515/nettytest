package cn.ljh.messagepack;

import org.msgpack.MessagePack;

import cn.ljh.utils.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgpackEncoder extends MessageToByteEncoder<Object>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
		MessagePack mPack = new MessagePack();
		byte[] raw = mPack.write(msg);
		out.writeBytes(raw);
	}

}
