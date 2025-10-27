package org.JavaBackendCourse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Random;

public class Bank {
    private final Map<Integer, Client> clients;
    private final Map<String, Double> exchangeRates;
    private final List<TransactionObserver> observers;
    private final Random random;

    public Bank() {
        this.clients = new ConcurrentHashMap<>();
        this.exchangeRates = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
        this.random = new Random();
        initializeExchangeRates();
    }

    private void initializeExchangeRates() {
        for (Currency from : Currency.values()) {
            for (Currency to : Currency.values()) {
                if (from != to) {
                    String key = generateExchangeKey(from, to);
                    exchangeRates.put(key, generateRandomRate(from, to));
                }
            }
        }
    }

    private double generateRandomRate(Currency from, Currency to) {
        Map<String, Double> baseRates = Map.of("USD_EUR", 0.92, "USD_GBP", 0.79, "USD_JPY", 148.50, "USD_CHF", 0.88, "EUR_GBP", 0.86, "EUR_JPY", 161.50, "EUR_CHF", 0.96, "GBP_JPY", 187.80, "GBP_CHF", 1.11, "JPY_CHF", 0.0059);

        String key = from.getCode() + "_" + to.getCode();
        Double baseRate = baseRates.get(key);

        if (baseRate == null) {
            String reverseKey = to.getCode() + "_" + from.getCode();
            Double reverseRate = baseRates.get(reverseKey);
            baseRate = reverseRate != null ? 1.0 / reverseRate : 1.0 + (random.nextDouble() - 0.5) * 0.5;
        }

        return baseRate * (0.95 + random.nextDouble() * 0.1);
    }

    private String generateExchangeKey(Currency from, Currency to) {
        return from.getCode() + "_" + to.getCode();
    }

    public void addClient(Client client) {
        clients.put(client.getId(), client);
        notifyObservers("CLIENT_REGISTRATION", "Клиент " + client.getId() + " (" + client.getName() + ") зарегестрирован", true);
    }

    public Client getClient(int clientId) {
        return clients.get(clientId);
    }

    public double getExchangeRate(Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency == toCurrency) {
            return 1.0;
        }
        String key = generateExchangeKey(fromCurrency, toCurrency);
        return exchangeRates.getOrDefault(key, 1.0);
    }

    public void updateExchangeRates() {
        for (Currency from : Currency.values()) {
            for (Currency to : Currency.values()) {
                if (from != to) {
                    String key = generateExchangeKey(from, to);
                    double currentRate = exchangeRates.get(key);
                    double change = (random.nextDouble() - 0.5) * 0.02;
                    exchangeRates.put(key, currentRate * (1 + change));
                }
            }
        }

        notifyObservers("RATE_UPDATE", "Курсы обмена обновлены для всех валютных пар", true);
        printCurrentRates();
    }

    private void printCurrentRates() {
        System.out.println("\n=== CURRENT EXCHANGE RATES ===");
        for (Currency from : Currency.values()) {
            for (Currency to : Currency.values()) {
                if (from != to) {
                    String key = generateExchangeKey(from, to);
                    double rate = exchangeRates.get(key);
                    System.out.printf("%s/%s: %.4f ", from, to, rate);
                }
            }
            System.out.println();
        }
    }


    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String transactionType, String message, boolean success) {
        for (TransactionObserver observer : observers) {
            observer.update(transactionType, message, success);
        }
    }

    public Map<Integer, Client> getClients() {
        return clients;
    }
}