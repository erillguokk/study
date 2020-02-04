package com.guosheng.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {

    private CountDownLatch countDownLatch = null;
    //永久节点
    protected static final String lockPath="/lockPath";
    //当前节点路径
    private String currentPath;
    //前一个节点的路径
    private String beforePath;

    public ZookeeperDistrbuteLock() {
        //如果不存在这个节点，则创建持久节点
        if (!zkClient.exists(lockPath)) {
            zkClient.createPersistent(lockPath);
        }
    }


    @Override
    boolean tryLock() {
        //如果currentPath为空则为第一次尝试加锁，第一次加锁赋值currentPath
        if (null == currentPath || "".equals(currentPath)) {
            //在path下创建一个临时的顺序节点
            currentPath = zkClient.createEphemeralSequential(lockPath+"/", "lock");
        }
        //获取所有的临时节点，并排序
        List<String> childrens = zkClient.getChildren(lockPath);
        Collections.sort(childrens);
        if (currentPath.equals(lockPath+"/"+childrens.get(0))) {
            return true;
        }else {//如果当前节点不是排名第一，则获取它前面的节点名称，并赋值给beforePath
            int pathLength = lockPath.length();
            int wz = Collections.binarySearch(childrens, currentPath.substring(pathLength+1));
            beforePath = lockPath+"/"+childrens.get(wz-1);
        }
        return false;

    }

    @Override
    void waitLock() {
        IZkDataListener lIZkDataListener = new IZkDataListener() {

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                if (null != countDownLatch){
                    countDownLatch.countDown();
                }
            }

            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
        };
        //监听前一个节点的变化
        zkClient.subscribeDataChanges(beforePath, lIZkDataListener);
        if (zkClient.exists(beforePath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(beforePath, lIZkDataListener);

    }

    public static void main(String [] args){
        for (int i=0;i<10;i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ZookeeperAbstractLock zkLock = new ZookeeperDistrbuteLock();
                    //AbstractLock zkLock = new HighPerformanceZkLock();
                    zkLock.getLock();
                    //模拟业务操作
                    try {
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName() +":"+"结束了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    zkLock.unLock();
                }
            });
            thread.start();
        }
    }

}
