package com.example.demo.kafka.integrate.collection.server;

import com.example.demo.kafka.integrate.collection.kafka.log.LogConstant;
import com.example.demo.kafka.integrate.collection.kafka.log.LogModel;
import com.example.demo.kafka.integrate.collection.netty.Header;
import com.example.demo.kafka.integrate.collection.netty.MessageType;
import com.example.demo.kafka.integrate.collection.netty.NettyMessage;
import com.example.demo.kafka.integrate.collection.service.MsgService;
import com.example.demo.kafka.integrate.collection.service.factory.ServiceFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MsgServerHandler extends ChannelInboundHandlerAdapter{ 
	
	Logger logger=LoggerFactory.getLogger(MsgServerHandler.class);
	static int count=0;
	MsgService msgService;
	
	public MsgServerHandler() {
		msgService= ServiceFactory.buildMsgService();
	}
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { 
    	NettyMessage message=(NettyMessage)msg;
    	ObjectMapper mapper=new ObjectMapper();
    	String temp=mapper.writeValueAsString(message.getBody());
    	LogModel logModel= 	mapper.readValue(temp, LogModel.class);
    	Map<String, String> attributeMap=new HashMap<String, String>();
    	attributeMap.put("topic", logModel.getTopic());
      	attributeMap.put("messageId", logModel.getMessageId());
      	attributeMap.put("operationEnumName", logModel.getOperationEnum().name());
     	attributeMap.put("ip", logModel.getIp());
     	attributeMap.put("recordTime", String.valueOf(logModel.getRecordTime()));
    	NettyMessage nettyMessage=buildMsgMessage(attributeMap);
    	String response=mapper.writeValueAsString(nettyMessage);
		Channel incoming = ctx.channel();
        incoming.writeAndFlush(response+ LogConstant.delimiter);
        incoming.flush();
        logger.info((++count)+"response....."+ response);
        
        msgService.saveMsg(logModel);
	}
    private NettyMessage buildMsgMessage(Map<String, String> attributeMap) {
 			NettyMessage message = new NettyMessage();
 			Header header = new Header();
 			header.setAttributeMap(attributeMap);
 			header.setType(MessageType.SERVICE_RESP.value());
 			message.setHeader(header);
 			return message;
 	    }
}