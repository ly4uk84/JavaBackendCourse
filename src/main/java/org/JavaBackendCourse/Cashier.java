package org.JavaBackendCourse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Cashier implements Runnable {
    private final int id;
    private final BlockingQueue<Transaction> transactionQueue;
    private final Bank bank;
    private volatile boolean running;

    public Cashier(int id, Bank bank) {
        this.id = id;
        this.bank = bank;
        this.transactionQueue = new LinkedBlockingQueue<>();
        this.running = true;
    }

    public void addTransaction(Transaction transaction) {
        try {
            transactionQueue.put(transaction);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            bank.notifyObservers("CASHIER_ERROR",
                    "Касса " + id + " прервано при добавлении транзакции", false);
        }
    }

    @Override
    public void run() {
        bank.notifyObservers("CASHIER__START", "Касса " + id + " запущена", true);

        while (running || !transactionQueue.isEmpty()) {
            try {
                Transaction transaction = transactionQueue.take();
                bank.notifyObservers("TRANSACTION_START",
                        String.format("Касса %d начинает обработку %s для клиента %d",
                                id, transaction.getType(), transaction.getClientId()), true);

                boolean success = transaction.call();

                bank.notifyObservers("TRANSACTION_END",
                        String.format("Касса %d завершила %s для клиента %d - Успешно: %s",
                                id, transaction.getType(), transaction.getClientId(), success), true);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                bank.notifyObservers("TRANSACTION_ERROR",
                        "Касса " + id + " ошибка: " + e.getMessage(), false);
            }
        }

        bank.notifyObservers("CASHIER_STOP", "Касса " + id + " остановлена", true);
    }

    public void stop() {
        running = false;
    }

    public boolean isQueueEmpty() {
        return transactionQueue.isEmpty();
    }

    public int getQueueSize() {
        return transactionQueue.size();
    }
}
