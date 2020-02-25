package com.dongnao.concurrent.period6_1;

import java.util.concurrent.BrokenBarrierException;

public class KodyCyclicBarrier_Test {
    public static void main(String args[]) throws BrokenBarrierException, InterruptedException {
        KodyCyclicBarrier barrier = new KodyCyclicBarrier(4);

        for (int i=0; i<10; i++){
            Thread th = new Thread(){
                @Override
                public void run() {
                    barrier.await();
                    System.out.println("run...");
                }
            };

            th.start();
            Thread.sleep(1000);
        }

    }



}
