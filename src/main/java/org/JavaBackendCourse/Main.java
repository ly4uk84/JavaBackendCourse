package org.JavaBackendCourse;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

class BankApplication {
    private static final int NUM_CASH_DESKS = 3;
    private static final int NUM_CLIENTS = 10;
    private static final String[] CLIENT_NAMES = {
            "Alice", "Bob", "Charlie", "Diana", "Eve",
            "Frank", "Grace", "Henry", "Ivan", "Julia"
    };

    private final Bank bank;
    private final Cashier[] cashiers;
    private final ExecutorService cashierExecutor;
    private final ScheduledExecutorService rateUpdateExecutor;
    private final Random random;

    public BankApplication() {
        this.bank = new Bank();
        this.cashiers = new Cashier[NUM_CASH_DESKS];
        this.cashierExecutor = Executors.newFixedThreadPool(NUM_CASH_DESKS);
        this.rateUpdateExecutor = Executors.newScheduledThreadPool(1);
        this.random = new Random();

        initializeBank();
    }

    private void initializeBank() {
        bank.addObserver(new TransactionLogger());

        for (int i = 0; i < NUM_CLIENTS; i++) {
            String name = CLIENT_NAMES[i % CLIENT_NAMES.length] + (i / CLIENT_NAMES.length + 1);
            Client client = new Client(i + 1, name, 1000.0 + random.nextDouble() * 5000);
            bank.addClient(client);
        }

        for (int i = 0; i < NUM_CASH_DESKS; i++) {
            cashiers[i] = new Cashier(i + 1, bank);
            cashierExecutor.execute(cashiers[i]);
        }

        rateUpdateExecutor.scheduleAtFixedRate(() -> bank.updateExchangeRates(),
                5, 15, TimeUnit.SECONDS);
    }

    public void exchangeCurrency(int clientId, Currency fromCurrency, Currency toCurrency, double amount) {
        Transaction transaction = new CurrencyExchangeTransaction(clientId, fromCurrency, toCurrency, amount, bank);
        assignTransactionToCashDesk(transaction);
    }

    public void transferFunds(int senderId, int receiverId, double amount, Currency currency) {
        Transaction transaction = new FundsTransferTransaction(senderId, receiverId, amount, bank, currency);
        assignTransactionToCashDesk(transaction);
    }

    private void assignTransactionToCashDesk(Transaction transaction) {
        Cashier selectedCashier = cashiers[0];
        for (Cashier cashier : cashiers) {
            if (cashier.getQueueSize() < selectedCashier.getQueueSize()) {
                selectedCashier = cashier;
            }
        }
        selectedCashier.addTransaction(transaction);
    }

    public void simulateRandomTransactions(int numTransactions) {
        Currency[] currencies = Currency.values();

        for (int i = 0; i < numTransactions; i++) {
            int transactionType = random.nextInt(2);

            if (transactionType == 0) {
                int clientId = random.nextInt(NUM_CLIENTS) + 1;
                Currency fromCurrency = currencies[random.nextInt(currencies.length)];
                Currency toCurrency;
                do {
                    toCurrency = currencies[random.nextInt(currencies.length)];
                } while (toCurrency == fromCurrency);

                double amount = 10 + random.nextDouble() * 500;
                exchangeCurrency(clientId, fromCurrency, toCurrency, amount);

            } else {
                int senderId = random.nextInt(NUM_CLIENTS) + 1;
                int receiverId;
                do {
                    receiverId = random.nextInt(NUM_CLIENTS) + 1;
                } while (receiverId == senderId);

                Currency currency = currencies[random.nextInt(currencies.length)];
                double amount = 10 + random.nextDouble() * 300;
                transferFunds(senderId, receiverId, amount, currency);
            }

            try {
                Thread.sleep(100 + random.nextInt(400));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void printClientBalances() {
        System.out.println("\n=== CURRENT CLIENT BALANCES ===");
        bank.getClients().values().forEach(client -> {
            System.out.println(client);
        });
    }

    public void shutdown() {
        for (Cashier cashier : cashiers) {
            cashier.stop();
        }

        cashierExecutor.shutdown();
        rateUpdateExecutor.shutdown();

        try {
            if (!cashierExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                cashierExecutor.shutdownNow();
            }
            if (!rateUpdateExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                rateUpdateExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cashierExecutor.shutdownNow();
            rateUpdateExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Приложение юанк завершено");
    }

    public static void main(String[] args) {
        BankApplication app = new BankApplication();

        System.out.println("=== BANK APPLICATION STARTED ===");
        System.out.println("Клиенты: " + NUM_CLIENTS);
        System.out.println("Кассы: " + NUM_CASH_DESKS);
        System.out.println("Валюты: " + java.util.Arrays.toString(Currency.values()));

        app.simulateRandomTransactions(25);

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        app.printClientBalances();

        app.shutdown();
    }
}