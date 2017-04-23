package cn.ljh.protobuf;

import java.util.ArrayList;  
import java.util.List;  
  
/**  
 * Created by vixuan-008 on 2015/6/24.  
 */  
public class TestSubscribeReq {  
    public static void main(String[] args)throws Exception{ 
    	
        SubscribeReqProto.SubscribeReq req =createSubscribeReq();  
        System.out.println("Before encode:"+req.toString());  
  
        SubscribeReqProto.SubscribeReq result=decode(encode(req));  
        System.out.println("decode cotent is:"+result.toString());  
    }  
    private static byte[] encode(SubscribeReqProto.SubscribeReq req){  
    	    //编码通过调用SubscribeReqProto.SubscribeReq实例的toByteArray方法，
    		//即可将SubscribeReq对象编码为byte数组，使用非常方便。
            return req.toByteArray();  
    }  
    private static  SubscribeReqProto.SubscribeReq decode(byte[] body) throws Exception{  
    	//解码通过调用SubscribeReqProto.SubscribeReq
    	//的静态方法parseFrom将二进制数组解码为原始数据对象。
        return SubscribeReqProto.SubscribeReq.parseFrom(body);  
    }  
  
    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){  
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();  
        builder.setSubReqId(1);  
        builder.setUserName("zhouzhigang");  
        builder.setProductName("Netty Book");  
        List<String> address=new ArrayList<String>();  
        address.add("湖南长沙");  
        address.add("湖南株洲");  
        address.add("湖南湘潭");  
        builder.addAllAddress(address);  
        return builder.build();  
  
    }  
}  