package com.dongnao.concurrent.period6.locks2;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;


public class KodyLock implements Lock {

    //private Thread owner = null;

    //锁拥有者
    volatile AtomicReference<Thread> owner = new AtomicReference<>();

    //等待队列
    private LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    //记录重入的次数
    volatile AtomicInteger count = new AtomicInteger(0);

    @Override
    public boolean tryLock() {
        //判断count是否为0，若count！=0，说明锁被占用
        int ct = count.get();
        if (ct!=0){
            if (owner.get() == Thread.currentThread()){
                count.set(ct +1);
                return true;
            }
        }else{
            //如count==0，说明锁未未被占用，CAS操作来抢锁，修改count的值
            if (count.compareAndSet( ct, ct +1)){
                owner.set(Thread.currentThread());
                return true;
            }
        }
        return false;
    }

    @Override
    public void lock() {
        if (!tryLock()){
            waiters.offer(Thread.currentThread());

            //自旋抢锁
            for (;;){
                Thread head = waiters.peek();
                if (head == Thread.currentThread()){
                    if (!tryLock()){
                        LockSupport.park();
                    }else{
                        waiters.poll();
                        return;
                    }
                }else{
                    LockSupport.park();
                }
            }
        }
    }

    @Override
    public void unlock() {
        if (tryUnlock()){
            Thread th = waiters.peek();
            if (th !=null){
                LockSupport.unpark(th);
            }
        }
    }


    public boolean tryUnlock(){
        //判断owner是不是自己
        if (owner.get() != Thread.currentThread()){
            throw new IllegalMonitorStateException();
        }else{
            //释放锁，count-1，
            int ct = count.get();
            int nextc = ct -1;
            count.set(nextc);

            //是不是一定将onwer该问null
            if (nextc == 0){
                owner.compareAndSet(Thread.currentThread(),null);
                return true;
            }else{
                return false;
            }
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
