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
    	    //����ͨ������SubscribeReqProto.SubscribeReqʵ����toByteArray������
    		//���ɽ�SubscribeReq�������Ϊbyte���飬ʹ�÷ǳ����㡣
            return req.toByteArray();  
    }  
    private static  SubscribeReqProto.SubscribeReq decode(byte[] body) throws Exception{  
    	//����ͨ������SubscribeReqProto.SubscribeReq
    	//�ľ�̬����parseFrom���������������Ϊԭʼ���ݶ���
        return SubscribeReqProto.SubscribeReq.parseFrom(body);  
    }  
  
    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){  
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();  
        builder.setSubReqId(1);  
        builder.setUserName("zhouzhigang");  
        builder.setProductName("Netty Book");  
        List<String> address=new ArrayList<String>();  
        address.add("���ϳ�ɳ");  
        address.add("��������");  
        address.add("������̶");  
        builder.addAllAddress(address);  
        return builder.build();  
  
    }  
}  