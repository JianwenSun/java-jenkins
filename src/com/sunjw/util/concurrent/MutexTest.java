package com.sunjw.util.concurrent;

import sun.awt.Mutex;

/**
 * Created by Administrator on 5/2/2017.
 */
public class MutexTest {

    public static void main(String[] args) {
        SemaphoreTest.MyATMThread t1 = new SemaphoreTest.MyATMThread("A");
        t1.start();

        SemaphoreTest.MyATMThread t2 = new SemaphoreTest.MyATMThread("B");
        t2.start();

        SemaphoreTest.MyATMThread t3 = new SemaphoreTest.MyATMThread("C");
        t3.start();

        SemaphoreTest.MyATMThread t4 = new SemaphoreTest.MyATMThread("D");
        t4.start();

        SemaphoreTest.MyATMThread t5 = new SemaphoreTest.MyATMThread("E");
        t5.start();

        SemaphoreTest.MyATMThread t6 = new SemaphoreTest.MyATMThread("F");
        t6.start();

    }
}
