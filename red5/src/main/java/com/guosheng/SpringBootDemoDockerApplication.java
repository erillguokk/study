package com.guosheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @package: com.xkcoding.docker
 * @description: 启动器
 * @author: yangkai.shen
 * @date: Created in 2018-11-29 14:59
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration(exclude = {JmxAutoConfiguration.class})
public class SpringBootDemoDockerApplication {

    public static void main(String[] args) throws  Exception{
        SpringApplication.run(SpringBootDemoDockerApplication.class, args);
        //视频推流
       // PushRTMP.run();
    }
}
