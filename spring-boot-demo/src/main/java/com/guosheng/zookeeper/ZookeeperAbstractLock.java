package com.guosheng.zookeeper;


import org.I0Itec.zkclient.ZkClient;

public abstract class ZookeeperAbstractLock implements ExtLock {
    private static final String CONNECTION="192.168.43.243:2181";
    public static final int SESSION_TIMEOUT = 10000;
    public ZkClient zkClient = new ZkClient(CONNECTION, SESSION_TIMEOUT);
    //获取锁
    public void getLock() {
        //1、连接zkClient 创建一个/lock的临时节点
        // 2、 如果节点创建成果，直接执行业务逻辑，如果节点创建失败，进行等待
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName()+"#####成功获取锁######");
        }else {
            //进行等待
            waitLock();
        }

    }

    //创建失败 进行等待
    abstract void waitLock();


    abstract boolean tryLock();


    //释放锁
    public void unLock() {
        //执行完毕 直接连接
        if (zkClient != null) {
            zkClient.close();
            System.out.println(Thread.currentThread().getName()+"######释放锁完毕######");
        }

    }

}
