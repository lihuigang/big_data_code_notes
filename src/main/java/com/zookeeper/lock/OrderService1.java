package com.zookeeper.lock;

import com.zookeeper.lock.OrderNumGenerator;

public class OrderService1 implements Runnable {
    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();

    //private Lock lock = new ZookeeperkLock();

    private Lock lock = new ZookeeperDistrbuteLock2();

    public void run() {
        getNumber();
    }
    public void getNumber() {
        try {
            lock.getLock();
            String number = orderNumGenerator.getNumber();
            System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unLock();
        }
    }
    public static void main(String[] args) {
        System.out.println("####生成唯一订单号###");
//		OrderService orderService = new OrderService();
        for (int i = 0; i < 50; i++) {
            new Thread( new OrderService()).start();
            new Thread( new OrderService1()).start();
        }
    }
}  