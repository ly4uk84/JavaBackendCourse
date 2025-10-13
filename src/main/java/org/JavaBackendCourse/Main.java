package org.JavaBackendCourse;

import java.util.Scanner;


enum Operation {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
}

class Operand {
    protected int value;

    Operand(String token) {
        this.value = Integer.parseInt(token);
    }
}

class Calculator {
    protected Calculator(String mathExpression) {
        String[] tokens = mathExpression.split(" ");
        Operand operand1 = new Operand(tokens[0]);
        Operand operand2 = new Operand(tokens[2]);
        String stringOperator = tokens[1];
        // валидируем данные
        Operation operator = validateOperation(operand2.value, stringOperator);
        // считаем и выводим
        calculate(operand1, operand2, operator);
    }

    private Operation validateOperation(int operand2, String stringOperator) {
        // проверяем на соответствие операциям
        Operation operator;
        switch (stringOperator) {
            case "+":
                operator = Operation.ADDITION;
                break;
            case "-":
                operator = Operation.SUBTRACTION;
                break;
            case "*":
                operator = Operation.MULTIPLICATION;
                break;
            case "/":
                operator = Operation.DIVISION;
                // проверяем что не делим на ноль
                if (operand2 == 0) {
                    throw new IllegalArgumentException("can't divide by 0");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + stringOperator);
        }
        // Возвращаем корректный оператор
        return operator;
    }

    private void calculate(Operand operand1, Operand operand2, Operation operator) {
        switch (operator) {
            case ADDITION:
                System.out.println("Result: " + (operand1.value + operand2.value));
                break;
            case SUBTRACTION:
                System.out.println("Result: " + (operand1.value - operand2.value));
                break;
            case MULTIPLICATION:
                System.out.println("Result: " + (operand1.value * operand2.value));
                break;
            case DIVISION:
                System.out.println("Result: " + (operand1.value / (float) operand2.value));
                break;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // для пул реквеста
        String exit = "exit";
        String mathExpression = "";
        // цикл для выполнения пока не получим команду "exit" от пользователя
        while (!mathExpression.equals(exit)) {
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Input math expression (example: -7 + 2, operations: +, -, *, /) : ");
                mathExpression = in.nextLine();
                // создаем класс калькулятор и валидируем полученные данные, если получаем ошибку то начинаем сначало
                Calculator calculator = new Calculator(mathExpression);
            } catch (Exception e) {
                System.out.println("Error, " + e.getMessage());
            }
        }
        // получили команду "exit"
        System.out.println("exit....................");
    }
}

