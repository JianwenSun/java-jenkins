package com.sunjw.util.concurrent;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.*;

/**
 * Created by Administrator on 5/1/2017.
 */
public class LockCompareTest {
    class BankAccount {
        private long balance;

        public BankAccount(long balance) {
            this.balance = balance;
        }

        public void deposit(long amount) {
            balance += amount;
        }

        public void withdraw(long amount) {
            balance -= amount;
        }

        public long getBalance() {
            return balance;
        }
    }

    class BankAccountSynchronized {
        private long balance;

        public BankAccountSynchronized(long balance) {
            this.balance = balance;
        }

        public synchronized void deposit(long amount) {
            balance += amount;
        }

        public synchronized void withdraw(long amount) {
            balance -= amount;
        }

        public synchronized long getBalance() {
            return balance;
        }
    }

    class BankAccountSynchronizedVolatile {
        private volatile long balance;

        public BankAccountSynchronizedVolatile(long balance) {
            this.balance = balance;
        }

        public synchronized void deposit(long amount) {
            balance += amount;
        }

        public synchronized void withdraw(long amount) {
            balance -= amount;
        }

        public long getBalance() {
            return balance;
        }
    }

    class BankAccountReentrantLock {
        private final Lock lock = new ReentrantLock();
        private long balance;

        public BankAccountReentrantLock(long balance) {
            this.balance = balance;
        }

        public void deposit(long amount) {
            lock.lock();
            try {
                balance += amount;
            } finally {
                lock.unlock();
            }
        }

        public void withdraw(long amount) {
            lock.lock();
            try {
                balance -= amount;
            } finally {
                lock.unlock();
            }
        }

        public long getBalance() {
            lock.lock();
            try {
                return balance;
            } finally {
                lock.unlock();
            }
        }
    }

    class BankAccountReentrantReadWriteLock {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private long balance;

        public BankAccountReentrantReadWriteLock(long balance) {
            this.balance = balance;
        }

        public void deposit(long amount) {
            lock.writeLock().lock();
            try {
                balance += amount;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public void withdraw(long amount) {
            lock.writeLock().lock();
            try {
                balance -= amount;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public long getBalance() {
            lock.readLock().lock();
            try {
                return balance;
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    class BankAccountStampedLock {
        private final StampedLock sl = new StampedLock();
        private long balance;

        public BankAccountStampedLock(long balance) {
            this.balance = balance;
        }

        public void deposit(long amount) {
            long stamp = sl.writeLock();
            try {
                balance += amount;
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        public void withdraw(long amount) {
            long stamp = sl.writeLock();
            try {
                balance -= amount;
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        public long getBalance() {
            long stamp = sl.readLock();
            try {
                return balance;
            } finally {
                sl.unlockRead(stamp);
            }
        }

        public long getBalanceOptimisticRead() {
            long stamp = sl.tryOptimisticRead();
            long balance = this.balance;
            if (!sl.validate(stamp)) {
                stamp = sl.readLock();
                try {
                    balance = this.balance;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            return balance;
        }
    }

    class BankAccountImmutable {
        private final long balance;

        public BankAccountImmutable(long balance) {
            this.balance = balance;
        }

        public BankAccountImmutable deposit(long amount) {
            return new BankAccountImmutable(balance + amount);
        }

        public BankAccountImmutable withdraw(long amount) {
            return new BankAccountImmutable(balance - amount);
        }

        public long getBalance() {
            return balance;
        }
    }

    class BankAccountAtomic {
        private final AtomicLong balance;

        public BankAccountAtomic(long balance) {
            this.balance = new AtomicLong(balance);
        }

        public void deposit(long amount) {
            balance.addAndGet(amount);
        }

        public void withdraw(long amount) {
            balance.addAndGet(-amount);
        }

        public long getBalance() {
            return balance.get();
        }
    }
}
