package org.JavaBackendCourse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TransactionLogger implements TransactionObserver {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void update(String transactionType, String message, boolean success) {
        String status = success ? "SUCCESS" : "FAILED";
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] [%s] [%s] %s",
                timestamp, transactionType, status, message);

        System.out.println(logMessage);
    }
}
