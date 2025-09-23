package org.JavaBackendCourse;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
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
                calculator.calculate();
            } catch (Exception e) {
                System.out.println("Error, " + e.getMessage());
            }
        }
        // получили команду "exit"
        System.out.println("exit....................");
    }
}

class Calculator {
    int num1;
    int num2;
    String operator;

    public Calculator(String mathExpression) {
        String[] tokens = mathExpression.split(" ");
        this.num1 = Integer.parseInt(tokens[0]);
        this.num2 = Integer.parseInt(tokens[2]);
        this.operator = tokens[1];
        // валидируем данные
        validate(num2, operator);
    }

    public void validate(int num2, String operator) {
        String matchOperation = "+-*/";
        // проверяем на соответствие операциям
        boolean contains = matchOperation.contains(operator);
        if (!contains) { throw new IllegalArgumentException("Invalid operator: " + operator);}

        // проверяем что не делим на ноль
        if (operator.equals("/") && num2 == 0) { throw new IllegalArgumentException("can't divide by 0"); }
    }

    // проверям операцию и в зависимости от нее вызываем нужный метод
    public void calculate() {
        switch (operator) {
            case "+":
                addition(num1, num2);
                break;
            case "-":
                subtraction(num1, num2);
                break;
            case "*":
                multiplication(num1, num2);
                break;
            case "/":
                division(num1, num2);
                break;
        }
    }

    // сложкение
    public void addition(int num1, int num2) {
        System.out.println("Result: " + (num1 + num2));
    }

    // вычитание
    public void subtraction(int num1, int num2) {
        System.out.println("Result: " + (num1 - num2));
    }

    // умножение
    public void multiplication(int num1, int num2) {
        System.out.println("Result: " + (num1 * num2));
    }

    // деление
    public void division(int num1, int num2) {
        System.out.println("Result: " + (num1 / (float) num2));
    }
}
