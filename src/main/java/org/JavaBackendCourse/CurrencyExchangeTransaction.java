package org.JavaBackendCourse;

public class CurrencyExchangeTransaction implements Transaction {
    private final int clientId;
    private final Currency fromCurrency;
    private final Currency toCurrency;
    private final double amount;
    private final Bank bank;

    public CurrencyExchangeTransaction(int clientId, Currency fromCurrency,
                                       Currency toCurrency, double amount, Bank bank) {
        this.clientId = clientId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.bank = bank;
    }

    @Override
    public Boolean call() {
        Client client = bank.getClient(clientId);
        if (client == null) {
            bank.notifyObservers("CURRENCY_EXCHANGE",
                    "Клиент " + clientId + " не найден", false);
            return false;
        }

        if (fromCurrency == toCurrency) {
            bank.notifyObservers("CURRENCY_EXCHANGE",
                    "Невозможно обменять одну и ту же валюту: " + fromCurrency, false);
            return false;
        }

        client.lock();
        try {
            if (!client.hasSufficientFunds(fromCurrency, amount)) {
                bank.notifyObservers("CURRENCY_EXCHANGE",
                        String.format("Клиент %d недостаточно средств %s: %.2f (доступно: %.2f)",
                                clientId, fromCurrency, amount,
                                client.getBalance(fromCurrency)), false);
                return false;
            }

            double rate = bank.getExchangeRate(fromCurrency, toCurrency);
            double convertedAmount = amount * rate;

            client.subtractFromBalance(fromCurrency, amount);
            client.addToBalance(toCurrency, convertedAmount);

            bank.notifyObservers("CURRENCY_EXCHANGE",
                    String.format("Клиент %d меняет %.2f %s на %.2f %s (коэффициент: %.4f)",
                            clientId, amount, fromCurrency,
                            convertedAmount, toCurrency, rate), true);
            return true;

        } finally {
            client.unlock();
        }
    }

    @Override
    public int getClientId() { return clientId; }

    @Override
    public String getType() { return "CURRENCY_EXCHANGE"; }
}