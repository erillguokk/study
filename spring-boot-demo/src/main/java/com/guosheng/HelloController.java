package com.guosheng;

import com.guosheng.zookeeper.ZookeeperAbstractLock;
import com.guosheng.zookeeper.ZookeeperDistrbuteLock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Hello Controller
 * </p>
 *
 * @package: com.xkcoding.docker.controller
 * @description: Hello Controller
 * @author: yangkai.shen
 * @date: Created in 2018-11-29 14:58
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
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
        return "Hello,From Docker!";
    }
}
