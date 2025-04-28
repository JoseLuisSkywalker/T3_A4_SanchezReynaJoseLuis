package Deadlock;


import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//CODIGO PROPORCIONADO POR LA PAGINA DE INTERNET COMO EJMEPLO DE
//LA SIMULACION DE UN DEADLOCK.

class Account{
    private BigDecimal balance;
    private final int id;

    public Account(int id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;

    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);

    }

    public synchronized void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", balance=" + balance + '}';

    }

}



public class PruebaDeadlock {

    private static void transferFunds(final Account fromAccount,
                                      final Account toAccount, final BigDecimal amount) {
        synchronized (fromAccount) {
            System.out.println(Thread.currentThread().getName() + "acquiered lock on " + fromAccount);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            synchronized (toAccount) {
                System.out.println(Thread.currentThread().getName() + " acquired lock on " + toAccount);
                transfer(fromAccount, toAccount, amount);

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
