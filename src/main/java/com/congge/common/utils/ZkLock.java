package com.congge.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ZkLock implements Watcher,Closeable {

    private ZooKeeper zooKeeper;

    private String znode;

    public ZkLock() {
        String connectionUrl = "localhost:2181";
        int sessionTimeout = 10000;
        try {
            this.zooKeeper = new ZooKeeper(connectionUrl, sessionTimeout, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getLock(String businessCode) {
        try {
            //创建业务根节点
            Stat stat = zooKeeper.exists("/" + businessCode, false);
            if (stat == null) {
                zooKeeper.create("/" + businessCode, businessCode.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //创建临时节点,这里的命名可以根据业务规则定制，只要满足有序即可
            znode = zooKeeper.create("/" + businessCode + "/" + businessCode + "_", businessCode.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            //判断当前这个节点是否是所有节点中序号最小的那一个
            List<String> childrenNodes = zooKeeper.getChildren("/" + businessCode, false);
            Collections.sort(childrenNodes);
            String firstNode = childrenNodes.get(0);
            //如果说所有的子节点中排在第一位的那个节点正好就是，说明获取到了锁
            if (znode.endsWith(firstNode)) {
                return true;
            }
            //如果不是第一个子节点，则监听前一个节点
            String lastNode = firstNode;
            for (String node : childrenNodes) {
                if (znode.endsWith(node)) {
                    zooKeeper.exists("/" + businessCode + "/" + lastNode, true);
                    break;
                } else {
                    lastNode = node;
                }
            }
            //等待前一个节点唤醒
            synchronized (this) {
                wait();
            }
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        try {
            zooKeeper.delete(znode,-1);
            zooKeeper.close();
            log.info("我已经释放了锁");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听删除事件
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }
}
