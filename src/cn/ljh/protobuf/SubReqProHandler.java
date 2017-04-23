package cn.ljh.protobuf;

import io.netty.channel.ChannelHandlerAdapter;  
import io.netty.channel.ChannelHandlerContext;  
  
import java.util.ArrayList;  
import java.util.List;  
  
/**  
 * Created by vixuan-008 on 2015/6/24.  
 */  
public class SubReqProHandler extends ChannelHandlerAdapter {  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
      ctx.close();  
    }  
  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
        for(int i=0;i<10;i++){  
            ctx.write(subReq(i));  
        }  
        ctx.flush();  
    }  
  
    private SubscribeReqProto.SubscribeReq subReq(int i){  
        SubscribeReqProto.SubscribeReq.Builder req=SubscribeReqProto.SubscribeReq.newBuilder();  
        req.setProductName("Netty Book");  
        req.setUserName("zhouzhigang");  
        req.setSubReqId(i);  
        List<String> address=new ArrayList<String>();  
        address.add("china");  
        address.add("usa");  
        address.add("france");  
        req.addAllAddress(address);  
        return req.build();  
    }  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
       System.out.println(msg);  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        ctx.flush();  
    }  
}  