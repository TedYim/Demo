package com.example.demo.kafka.integrate.collection.dao.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.example.demo.kafka.integrate.collection.dao.MsgDao;
import com.example.demo.kafka.integrate.collection.dao.share.ThreadShare;
import com.example.demo.kafka.integrate.collection.hbase.HBaseConnectionPool;
import org.apache.hadoop.hbase.client.Connection;


public class DaoProxy implements InvocationHandler {

    private MsgDao dao;

    public DaoProxy(MsgDao dao) {
        this.dao = dao;
        if (dao == null) {
            throw new RuntimeException("dao is null");
        }
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args)
            throws Throwable {
        Connection connection= ThreadShare.getConnection();
        boolean keepalived=true;//会话保持
        if(connection==null){
            HBaseConnectionPool pool = HBaseConnectionPool.getConnectionPool();
            connection = pool.getConnection();
            ThreadShare.setConnection(connection);
            keepalived=false;
        }
        Object result = method.invoke(dao, args);
        if(!keepalived){
            connection.close();
            ThreadShare.setConnection(null);
        }
        return result;
    }

}
