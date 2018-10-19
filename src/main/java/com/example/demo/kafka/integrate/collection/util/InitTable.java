package com.example.demo.kafka.integrate.collection.util;

import java.io.IOException;

import com.example.demo.kafka.integrate.collection.util.HBaseHelper;
import com.topscore.integrate.entity.MsgEntityStatus;
import com.topscore.integrate.hbase.HBaseConnectionPool;

public class InitTable {
	static{
		HBaseConnectionPool.init("172.16.35.57:60000", "172.16.34.200,172.16.34.201,172.16.34.202");
	}
    public static void main(String[] args) {
    	msgEntityColfams(args);
    }
    

    public static void msgEntityColfams(String[] args) {
        HBaseHelper helper = HBaseHelper.getHBaseHelper();
        try {
            helper.dropTable(MsgEntityStatus.tableName);
            helper.createTable(MsgEntityStatus.tableName, MsgEntityStatus.family);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
