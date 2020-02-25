package com.dongnao.concurrent.period6;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Demo6_ReadWrite02 {


    volatile long i = 0;

    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void read() {
        rwLock.readLock().lock();

        long iValue = i;

        rwLock.readLock().unlock();
    }

    public void write() {
        rwLock.writeLock().lock();

        i++;

        rwLock.writeLock().lock();
    }


    public static void main(String[] args) throws InterruptedException {
        final Demo5_ReadWrite01 demo = new Demo5_ReadWrite01();

        for (int i=0;i<=10; i++){
            int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long starttime = System.currentTimeMillis();

                    while (System.currentTimeMillis() - starttime < 2000) {
                        if (n==0) demo.write();
                        else demo.read();
                    }
                }
            }).start();
        }

        Thread.sleep(4000L);

        System.out.println(demo.i);
    }


}