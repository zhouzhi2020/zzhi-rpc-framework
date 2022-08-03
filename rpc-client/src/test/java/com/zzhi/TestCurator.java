package com.zzhi;


import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketImpl;
import java.util.List;

public class TestCurator {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;

    private CuratorFramework zkClient;
    @Before
    public void testCreate() {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retry)
                .build();
        zkClient.start();
    }

    @After
    public void close() {
        zkClient.close();
    }

    @Test
    public void testCreateData() {
        try {
            zkClient.create().creatingParentsIfNeeded().forPath("/node1/n1", "java".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testSetData() {
        try {
            zkClient.setData().forPath("/node1", "java".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    @Test
    public void testDeleteData() {
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath("/rpc");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testGetChild() {
        try {
            List<String> strings = zkClient.getChildren().forPath("/node1");
            System.out.println(strings);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testGetData() {
        try {
            byte[] bytes = zkClient.getData().forPath("/node1");
            System.out.println(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testWatcher() {
        try {
            String path = "/node1";
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true);

            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    System.out.println(pathChildrenCacheEvent.getType());
                    if (PathChildrenCacheEvent.Type.CHILD_ADDED.equals(pathChildrenCacheEvent.getType())) {
                        System.out.println("发生子节点添加事件");
                    }
                }
            });
            pathChildrenCache.start();
            zkClient.delete().forPath(path.concat("/child1"));
            zkClient.create().creatingParentsIfNeeded().forPath(path.concat("/child1"), "i am child".getBytes());

            Thread.sleep(1000);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
