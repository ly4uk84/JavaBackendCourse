package org.JavaBackendCourse;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private final int id;
    private final String name;
    private final Map<Currency, Double> balances;
    private final Lock lock;

    public Client(int id, String name, double initialBalanceUSD) {
        this.id = id;
        this.name = name;
        this.balances = new EnumMap<>(Currency.class);
        this.lock = new ReentrantLock();

        for (Currency currency : Currency.values()) {
            balances.put(currency, 0.0);
        }
        balances.put(Currency.USD, initialBalanceUSD);
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public double getBalance(Currency currency) {
        return balances.getOrDefault(currency, 0.0);
    }

    public Map<Currency, Double> getBalances() {
        return new EnumMap<>(balances);
    }

    public void setBalance(Currency currency, double amount) {
        balances.put(currency, amount);
    }

    public void addToBalance(Currency currency, double amount) {
        balances.put(currency, balances.get(currency) + amount);
    }

    public void subtractFromBalance(Currency currency, double amount) {
        balances.put(currency, balances.get(currency) - amount);
    }

    public boolean hasSufficientFunds(Currency currency, double amount) {
        return getBalance(currency) >= amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Клиент: %d [%s] ", id, name));
        balances.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .forEach(entry -> sb.append(String.format("%s: %.2f ", entry.getKey(), entry.getValue())));
        return sb.toString();
    }
}