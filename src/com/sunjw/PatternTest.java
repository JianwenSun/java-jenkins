package com.sunjw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 4/30/2017.
 */

public class PatternTest {
    public static void main(String[] args) {
        ProducerConsumer producerConsumer = new ProducerConsumer(15);
        producerConsumer.start();

        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ProducerConsumer {
    Thread producerThread;
    Thread[] consumerThreads;
    ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    Thread infoThread;
    ThreadGroup group;

    BlockingQueue<String> queue = new LinkedBlockingDeque();

    private volatile boolean isInfoRuning = false;
    private volatile boolean isProducerRuning = false;
    private volatile boolean isConsumerRunning = false;

    AtomicInteger lag = new AtomicInteger(0);
    int totalProduced = 0;

    protected ProducerConsumer(int consumers) {
        group = new ThreadGroup("ProducerConsumer");

        producerThread = new Thread(group, () -> this.produce());
        producerThread.setName("Producer");

        consumerThreads = new Thread[consumers];
        for (int i = 0; i < consumers; i++) {
            Thread consumerThread = new Thread(group, () -> this.consume());
            consumerThread.setName("Consumer:" + i + "");
            consumerThreads[i] = consumerThread;
        }

        infoThread = new Thread(group, () -> this.info());
        infoThread.setName("Info");
    }

    public void start() {
        startInfo();
        startProducer();
        startConsumers();
    }

    void startInfo() {
        isInfoRuning = true;
        infoThread.start();
    }

    void startProducer() {
        isProducerRuning = true;
        producerThread.start();
    }

    void startConsumers() {
        isConsumerRunning = true;
        for (Thread consumerThread : consumerThreads) {
            consumerThread.start();
        }
    }

    void produce() {
        int i = 0;
        while (isProducerRuning) {
            try {
                int number = ThreadLocalRandom.current().nextInt(20);
                int producers = number;
                //System.out.println("Produce: " + number + " messages.");
                while (producers-- > 0) {
                    queue.add(String.format("Message time: %d", i++));
                    lag.getAndIncrement();
                }
                totalProduced += number;
            } finally {
            }
        }
    }

    void consume() {
        while (isConsumerRunning) {
            try {
                String item = null;
                if (!queue.isEmpty()) {
                    item = queue.poll(100, TimeUnit.MILLISECONDS);
                }

                if (item != null) {
                    counter.set(counter.get() + 1);
                    lag.getAndDecrement();
                    //System.out.println(String.format("Thread: %s Total: %d , Current - %s", Thread.currentThread().getName(), counter.get(), item));
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    void info() {
        while (isInfoRuning) {
            try {
                System.out.println(String.format("Total: " + totalProduced + ", Lag: " + lag.get() + ""));
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}