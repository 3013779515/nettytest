package cn.ljh.protobuf;

import io.netty.channel.ChannelHandlerAdapter;  
import io.netty.channel.ChannelHandlerContext;  
  
/**  
 * Created by vixuan-008 on 2015/6/24.  
 */  
public class SubRespProHandler extends ChannelHandlerAdapter {  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
       ctx.close();  
    }  
  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
        super.channelActive(ctx);  
    }  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        SubscribeReqProto.SubscribeReq req=(SubscribeReqProto.SubscribeReq)msg;  
        System.out.println(req.toString());  
        ctx.writeAndFlush(resp(req.getSubReqId()));  
    }  
  
    private SubscribeRespProto.SubscribeResp resp(int subReqId)throws Exception{  
        SubscribeRespProto.SubscribeResp.Builder resp =
        		SubscribeRespProto.SubscribeResp.newBuilder();  
        resp.setSubReqId(subReqId);  
        resp.setRespCode(0);  
        resp.setDesc("Netty Book order succeed 3 day later,sent to the designated adderss");  
        return resp.build();  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        super.channelReadComplete(ctx);  
    }  
}