package com.dongnao.concurrent.period6;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
  既有读，又有写时，
  读也需要加锁
 */
public class Demo5_ReadWrite01 {

    volatile long i = 0;

    //Lock lock = new ReentrantLock();
    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    //为什么读也要加锁？？？
    public void read() {
        rwLock.readLock().lock();

        long a = i;

        rwLock.readLock().unlock();
    }

    public void write() {
        rwLock.writeLock().lock();

        i++;

        rwLock.writeLock().unlock();
    }


    public static void main(String[] args) throws InterruptedException {
        final Demo5_ReadWrite01 demo = new Demo5_ReadWrite01();

        for (int i=0;i<=10; i++){
            int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long starttime = System.currentTimeMillis();
                    int i = 0;
                    while (System.currentTimeMillis() - starttime < 2000) {
                        if (n==0) {
                            demo.write();
                        }
                        else{
                            demo.read();
                        }
                        i++;
                    }
                }
            }).start();
        }

        Thread.sleep(4000L);

        System.out.println(demo.i);
    }


}
