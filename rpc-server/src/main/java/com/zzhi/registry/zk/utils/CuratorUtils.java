package com.zzhi.registry.zk.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Curator工具类
 *
 * @author zzhi
 * @date 2022/08/03
 */
@Slf4j
public class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;

    private static final String ZK_REGISTER_ROOT_PATH = new String("/rpc");
    private static final CuratorFramework ZK_CLIENT;


    static {
        log.info("首先启动咱们的注册中心");
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        ZK_CLIENT = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retry)
                .build();
        log.info("zookeeper 启动成功");
        ZK_CLIENT.start();
    }

    public static void createPersistentNode(CuratorFramework zkClient, String serviceName, String inetSocketAddress) {
        String path = ZK_REGISTER_ROOT_PATH + "/" + serviceName + inetSocketAddress;

        try {
            Stat stat = zkClient.checkExists().creatingParentsIfNeeded().forPath(path);
            if (stat != null) {
                log.info("成功在 {} 中 添加 {} 服务地址", serviceName, inetSocketAddress);
                return;
            }
            zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
            log.info("成功在 {} 中 添加 {} 服务地址", serviceName, inetSocketAddress);
        } catch (Exception e) {
            log.error("为服务添加注册发送异常");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<InetSocketAddress> getAddressByServiceName(CuratorFramework zkClient, String serviceName) {
        List<String> stringList = null;
        try {
            stringList = zkClient.getChildren().forPath(ZK_REGISTER_ROOT_PATH + "/" + serviceName);
        } catch (Exception e) {
            log.error("获取子节点失败");
            throw new RuntimeException(e);
        }
        List<InetSocketAddress> addressList = new ArrayList<>(stringList.size());
        for (int i = 0; i < stringList.size(); i++) {
            String[] inetSocketAddressArray = stringList.get(i).split(":");
            String host = inetSocketAddressArray[0];
            int port = Integer.parseInt(inetSocketAddressArray[1]);
            addressList.add(new InetSocketAddress(host, port));
        }
        log.info("{} 服务下的注册地址有 {}", serviceName, addressList);
        return addressList;
    }
    public static CuratorFramework getZkClient() {
        return ZK_CLIENT;
    }

}
