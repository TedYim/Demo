package com.example.demo.zk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 测试Curator事件监听
 */
public class CarutorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();

        client.create().creatingParentsIfNeeded().forPath("/zk-ted/znode", "watcher-test".getBytes());

        /**
         * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
         */
        ExecutorService pool = Executors.newFixedThreadPool(2);

        /**
         * 监听数据节点的变化情况
         */
        final NodeCache nodeCache = new NodeCache(client, "/zk-ted/znode", false/*数据是否被压缩*/);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(
                new NodeCacheListener() {
                    @Override
                    public void nodeChanged() throws Exception {
                        System.err.println("Node data is changed, new data: " +
                                new String(nodeCache.getCurrentData().getData()));
                    }
                },
                pool
        );

        /**
         * 监听子节点的变化情况
         */
        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/zk-ted", true);
        childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                            throws Exception {
                        switch (event.getType()) {
                            case CHILD_ADDED:
                                System.err.println("CHILD_ADDED: " + event.getData().getPath());
                                break;
                            case CHILD_REMOVED:
                                System.err.println("CHILD_REMOVED: " + event.getData().getPath());
                                break;
                            case CHILD_UPDATED:
                                System.err.println("CHILD_UPDATED: " + event.getData().getPath());
                                break;
                            default:
                                break;
                        }
                    }
                },
                pool
        );

        client.setData().forPath("/zk-ted/znode", "world".getBytes());

        Thread.sleep(Integer.MAX_VALUE);
        pool.shutdown();
        client.close();
    }
}
