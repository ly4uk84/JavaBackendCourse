package org.JavaBackendCourse;

public interface TransactionObserver {
    void update(String transactionType, String message, boolean success);
}
