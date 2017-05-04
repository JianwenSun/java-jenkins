package com.sunjw.util.concurrent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Administrator on 5/1/2017.
 */
public class ReentrantReadWriteLockExample {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private static String message = "a";

    static class ThreadHolder {
        public int index;

        public ThreadHolder(int index) {
            this.index = index;
        }
    }

    static class MyThreadFactory implements ThreadFactory {
        public final ThreadLocal<ThreadHolder> holder = new ThreadLocal<>();
        int index = 0;

        ThreadHolder getResource() {
            return new ThreadHolder(index++);
        }

        void releaseResource(ThreadHolder holder) {

        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r) {
                public void run() {
                    holder.set(getResource());
                    try {
                        super.run();
                    } finally {
                        releaseResource(holder.get());
                    }
                }
            };
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Stream.Builder<Thread> builer = Stream.builder();
        MyThreadFactory factory = new MyThreadFactory();

        List<Thread> threads = Stream.concat(IntStream.of(0).mapToObj(i -> factory.newThread(new Read(factory.holder))),
                IntStream.of(0, 1).mapToObj(i -> factory.newThread(new Write(factory.holder))))
                .collect(Collectors.toList());

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    static class Read implements Runnable {
        ThreadLocal<ThreadHolder> holder;

        public Read(ThreadLocal<ThreadHolder> holder) {
            this.holder = holder;
        }

        public void run() {
            for (int i = 0; i <= 10; i++) {
                if (lock.isWriteLocked()) {
                    System.out.println("I'll take the lock from Write");
                }
                lock.readLock().lock();
                System.out.println("ReadThread " + holder.get().index + " ---> Message is " + message);
                Thread.yield();
                lock.readLock().unlock();
            }
        }
    }

    static class Write implements Runnable {
        ThreadLocal<ThreadHolder> holder;

        public Write(ThreadLocal<ThreadHolder> holder) {
            this.holder = holder;
        }

        public void run() {
            for (int i = 0; i <= 10; i++) {
                try {
                    lock.writeLock().lock();
                    message = message.concat("" + holder.get().index + "");
                    Thread.yield();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        }
    }
}
