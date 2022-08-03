package com.zzhi;


import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestZooKeeper {
    private ZooKeeper zooKeeper;

    @Before
    public void testConnect() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, x -> {
            if (x.getState() == Watcher.Event.KeeperState.SyncConnected) {
                log.info("连接成功 ...");
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        log.info("会话id {}", zooKeeper.getSessionId());
    }

    @After
    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void testCreate() throws InterruptedException {

        zooKeeper.create("/hadoop", "node12".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback(){
            /**
             * @param rc 状态，0 则为成功，以下的所有示例都是如此
             * @param path 路径
             * @param ctx 上下文参数
             * @param name 路径
             */
            public void processResult(int rc, String path, Object ctx, String name){
                log.info(rc + " " + path + " " + name +  " " + ctx);
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(1);
        log.info("结束");
    }

    @Test
    public void testSet() {
        zooKeeper.setData("/hadoop", "hadoop-1".getBytes(), -1 , (rc, path, ctx, stat) -> {
            // 讲道理，要判空
            log.info(rc + " " + path + " " + stat.getVersion() +  " " + ctx);
        }, "I am context");
    }

    @Test
    public void testGet() throws InterruptedException, KeeperException {
        Stat stat = new Stat();

        byte[] data = zooKeeper.getData("/hadoop", false, stat);
        log.info(new String(data));

        log.info(String.valueOf(stat.getCtime()));
    }
    @Test
    public void testGet2() throws InterruptedException {
        zooKeeper.getData("/hadoop", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
                // 判空
                if(stat != null) {
                    log.info(String.format("%d %s %s %s %d", rc, path, ctx, new String(bytes), stat.getCzxid()));
                }
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(3);
    }
}
