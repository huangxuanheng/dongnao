package com.dongnao.concurrent.period6.locks1;


import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;


public class KodyLock implements Lock {

    //private Thread owner = null;

    //锁拥有者
    AtomicReference<Thread> owner = new AtomicReference<>();

    //等待队列
    private LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    @Override
    public boolean tryLock() {
        /* 存在原子性问题
        if (owner == null){
            owner = Thread.currentThread();
            return true;
        }else{
            return false;
        }
        */

        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public void lock() {
        /*
        if (!tryLock()){
            lock();
        }*/

        while (!tryLock()){

            waiters.offer(Thread.currentThread());
            LockSupport.park();
        }
    }

    @Override
    public void unlock() {
        if (owner.compareAndSet(Thread.currentThread(), null)){
            //这种实现存在惊群效应
           /* Iterator<Thread> itr = waiters.iterator();
            while(itr.hasNext()){
                Thread th =  itr.next();
                LockSupport.unpark(th);
            }*/

           //这种实现，是否能够保证公平呢？
            Thread th =  waiters.poll();
            LockSupport.unpark(th);
        }
    }




    @Override
    public void lockInterruptibly() throws InterruptedException {

    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }
}
