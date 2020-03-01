package com.guosheng.demo.remoting.server;


import com.guosheng.demo.remoting.common.HelloService;

public class Server {
 
    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println("please using command: java Server <rmi_host> <rmi_port>");
//            System.exit(-1);
//        }
// 
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
// 
    	
        String host = "192.168.133.1";//虚拟网卡可以和linux通讯的地址 
        int port = Integer.parseInt("11214");//每启动一个server，端口号递增，手动启动
        ServiceProvider provider = new ServiceProvider();
 
        HelloService helloService = new HelloServiceImpl();
        provider.publish(helloService, host, port);
 
        Thread.sleep(Long.MAX_VALUE);
    }
}