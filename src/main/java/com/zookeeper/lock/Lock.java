package com.zookeeper.lock;

/**
 * Created by lhg on 2020/07/05
 */
public interface Lock {
    //获取到锁的资源
    public void getLock();
    // 释放锁
    public void unLock();
}
