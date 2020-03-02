package com.gaoxinjie.zklock;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

public abstract class ZkAbstractTemplateLock implements ZkLock {


    public static final String ZKSERVER="182.168.230.128:2181";
    public static final int TIME_OUT=10*1000;
    protected String path="/zklock0401";
    protected CountDownLatch countDownLatch=null;

    ZkClient zkClient = new ZkClient(ZKSERVER,TIME_OUT);
    @Override
    public void zkLock() {
        if (tryLock()){

        }else {
            waitLock();
            zkLock();
        }
    }

    public abstract boolean tryLock();

    public abstract void waitLock();

    @Override
    public void zkUnlock() {
        if (zkClient !=null){
            zkClient.close();
        }
    }
}
