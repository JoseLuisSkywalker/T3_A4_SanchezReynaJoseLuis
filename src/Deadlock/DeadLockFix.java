package Deadlock;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DeadLockFix {
    private static final Object sameHashCodeLock = new Object();

    public static void transferFunds(final Account fromAccount, final Account toAccount, final BigDecimal amount) {
        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccount);

        if (fromHash < toHash) {
            synchronized (fromAccount) {
                System.out.println(Thread.currentThread().getName() + " acquired lock on " + fromAccount);
                synchronized (toAccount) {
                    transfer(fromAccount, toAccount, amount);
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAccount) {
                System.out.println(Thread.currentThread().getName() + " acquired lock on " + toAccount);
                synchronized (fromAccount) {
                    transfer(fromAccount, toAccount, amount);
                }
            }
        } else {
            synchronized (sameHashCodeLock) {
                synchronized (fromAccount) {
                    System.out.println(Thread.currentThread().getName() + " acquired lock on " + fromAccount);
                    synchronized (toAccount) {
                        transfer(fromAccount, toAccount, amount);
                    }
                }
            }
        }
    }

    public static void transfer(final Account fromAccount, final Account toAccount, final BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient funds.");
        else {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            System.out.println(Thread.currentThread()
                    .getName() + " transferred $" + amount + " from " + fromAccount + " to " + toAccount);
        }
    }

    public static void main(String[] args) {
        final Account acc1 = new Account(1, new BigDecimal("1000"));
        final Account acc2 = new Account(2, new BigDecimal("1000"));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(() -> transferFunds(acc1, acc2, new BigDecimal("100")));
        executor.execute(() -> transferFunds(acc2, acc1, new BigDecimal("50")));

        executor.shutdown();
    }
}