package com.example.demo.kafka.integrate.collection.service.impl;

import com.example.demo.kafka.integrate.collection.dao.proxy.DaoProxyFactory;
import com.example.demo.kafka.integrate.collection.entity.MsgEntity;
import com.example.demo.kafka.integrate.collection.entity.MsgEntityStatus;
import com.example.demo.kafka.integrate.collection.kafka.log.LogModel;
import com.example.demo.kafka.integrate.collection.kafka.log.MsgTypeEnum;
import com.example.demo.kafka.integrate.collection.service.MsgService;
import com.example.demo.kafka.integrate.collection.util.DateUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by lkl on 2017/12/8.
 */

public class MsgServiceImpl implements MsgService {
    @Override
    public void saveMsg(LogModel logModel) {
    	
    	MsgEntity msgEntity=new MsgEntity();
    	
    	String segment= MsgEntityStatus.SEGMENT;
    	String time=DateUtil.convert2String(logModel.getRecordTime(), DateUtil.SHORT_DAY_FORMAT);
    	String id=logModel.getTopic()+segment+time+segment+logModel.getMessageId();
    	msgEntity.setId(id);
        MsgEntity entity = DaoProxyFactory.buildMsgDao().getByRowKey(msgEntity.getId());
        if(entity==null){//生产
        	entity=msgEntity;
        	entity.setCreateTime(System.currentTimeMillis());
        }else{
        	entity.setUpdateTime(System.currentTimeMillis());
        }
        entity.setContent(logModel.getMessageBody());
    	if(logModel.getOperationEnum().name().equals(MsgTypeEnum.SEND.name())){
    		entity.setSendRecordTime(logModel.getRecordTime());
    		entity.setSendRequestTime(logModel.getRequestTime());
    		entity.setSendIp(logModel.getIp());
    		entity.setSend(MsgTypeEnum.SEND.name());
    	}else{
    		entity.setReceiveRecordTime(logModel.getRecordTime());
    		entity.setReceiveRequestTime(logModel.getRequestTime());
    		entity.setReceiveIp(logModel.getIp());
    		entity.setReceive(MsgTypeEnum.RECEIVED.name());
    	}
    	if(StringUtils.isNotBlank(logModel.getKeyword())){
    		entity.setKeyword(logModel.getKeyword());
    	}
    	if(StringUtils.isNotBlank(logModel.getKeyword2())){
    		entity.setKeyword2(logModel.getKeyword2());
    	}
    	 DaoProxyFactory.buildMsgDao().put(entity);
    }
}
