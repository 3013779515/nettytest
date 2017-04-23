package cn.ljh.messagepack;

import java.util.List;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.ljh.utils.UserInfo;

public class EchoClientHandler extends ChannelHandlerAdapter{
    private final int sendNumber;
    public EchoClientHandler(int sendNumber){
        this.sendNumber=sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        UserInfo [] infos = UserInfo();
        for(UserInfo infoE : infos){
            ctx.writeAndFlush(infoE);
            System.out.println("客户端发送：" + infoE.toString());
        }
        ctx.flush();
    }
    private UserInfo[] UserInfo(){
    	
        UserInfo [] userInfos=new UserInfo[sendNumber];
        UserInfo userInfo=null;
        
        for(int i=0;i<sendNumber;i++){
            userInfo=new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABCDEFG --->"+i);
            userInfos[i]=userInfo;
        }
        return userInfos;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
    	UserInfo uInfo = (UserInfo)msg;
		System.out.println("客户端收到：" + uInfo.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}