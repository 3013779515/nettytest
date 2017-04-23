package cn.ljh.messagepack;

import java.util.List;

import org.msgpack.MessagePack;

import cn.ljh.utils.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		final byte[] array;
        final int length=msg.readableBytes();
        array=new byte[length];
        msg.getBytes(msg.readerIndex(), array,0,length);
        MessagePack msgpack=new MessagePack();
        //必须使用Userinfo.class，不然解码后不能直接使用对象。
        out.add(msgpack.read(array,UserInfo.class));
		
	}

}
