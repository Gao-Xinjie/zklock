package com.gaoxinjie.zklock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

public class ZkDistributedLock extends ZkAbstractTemplateLock {
    @Override
    public boolean tryLock() {
        try {
            zkClient.createEphemeral(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void waitLock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                if (countDownLatch!=null){
                    countDownLatch.countDown();
                }

            }
        };
        zkClient.subscribeDataChanges(path, iZkDataListener);

        if (zkClient.exists(path)){
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zkClient.unsubscribeDataChanges(path, iZkDataListener);
        }

    }
}
