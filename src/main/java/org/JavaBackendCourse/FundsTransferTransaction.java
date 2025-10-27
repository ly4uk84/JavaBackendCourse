package org.JavaBackendCourse;

public class FundsTransferTransaction implements Transaction {
    private final int senderId;
    private final int receiverId;
    private final double amount;
    private final Bank bank;
    private final Currency currency;

    public FundsTransferTransaction(int senderId, int receiverId,
                                    double amount, Bank bank, Currency currency) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.bank = bank;
        this.currency = currency;
    }

    @Override
    public Boolean call() {
        if (senderId == receiverId) {
            bank.notifyObservers("FUNDS_TRANSFER",
                    "Отправитель и получатель не могут быть одним и тем же", false);
            return false;
        }

        Client sender = bank.getClient(senderId);
        Client receiver = bank.getClient(receiverId);

        if (sender == null || receiver == null) {
            bank.notifyObservers("FUNDS_TRANSFER",
                    "Отправитель или получатель не найден", false);
            return false;
        }

        Client firstLock = senderId < receiverId ? sender : receiver;
        Client secondLock = senderId < receiverId ? receiver : sender;

        firstLock.lock();
        secondLock.lock();

        try {
            double senderBalance = sender.getBalance(currency);
            if (senderBalance < amount) {
                bank.notifyObservers("FUNDS_TRANSFER",
                        String.format("У отправителя  %d недостаточно средств: %.2f %s",
                                senderId, amount, currency), false);
                return false;
            }

            sender.setBalance(currency, senderBalance - amount);
            receiver.setBalance(currency, receiver.getBalance(currency) + amount);

            bank.notifyObservers("FUNDS_TRANSFER",
                    String.format("Перевод %.2f %s от: %d ,кому: %d",
                            amount, currency, senderId, receiverId), true);
            return true;

        } finally {
            secondLock.unlock();
            firstLock.unlock();
        }
    }

    @Override
    public int getClientId() { return senderId; }

    @Override
    public String getType() { return "FUNDS_TRANSFER"; }
}
