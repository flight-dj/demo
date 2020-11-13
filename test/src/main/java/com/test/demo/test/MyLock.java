package com.test.demo.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyLock implements Lock {

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }


    private class Helper extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            return super.tryAcquire(arg);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return super.tryReleaseShared(arg);
        }
    }

}

class TestMyLock {

    private int value;

    private MyLock myLock = new MyLock();

    public int getValue() {
        myLock.lock();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        value ++;
        myLock.unlock();
        return value;
    }

    public void a() {
        myLock.lock();
        System.out.println("a");
        b();
        myLock.unlock();
    }

    public void b() {
        myLock.lock();
        System.out.println("b");
        myLock.unlock();
    }

    public static void main(String[] args) {
        TestMyLock testMyLock = new TestMyLock();

        //检测是否会并发
        Runnable runnable = () -> {
            for (int i = 0; i<100; i++) {
                System.out.println(Thread.currentThread().getId()+",值：" + testMyLock.getValue());
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();

        //检测重入锁
        Runnable runnable1 = () -> {
            testMyLock.a();
        };
        new Thread(runnable1).start();
    }

}