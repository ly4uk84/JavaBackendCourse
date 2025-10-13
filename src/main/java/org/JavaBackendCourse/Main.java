package org.JavaBackendCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


abstract class Transport {
    protected String model;
    protected Engine engine;

    public Transport(String model, Engine engine) {
        this.model = model;
        this.engine = engine;
    }

    public abstract void start();

    public abstract void stop();

    public abstract String getInfo();

    public String getModel() {
        return model;
    }
}


sealed class Engine permits CarEngine, PlaneEngine, ShipEngine, BikeEngine {
    protected FuelType fuelType;
    protected int power;

    public Engine(FuelType fuelType, int power) {
        this.fuelType = fuelType;
        this.power = power;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public int getPower() {
        return power;
    }
}

enum FuelType {
    GASOLINE,
    DIESEL,
    ELECTRIC,
    JET_FUEL,
    SAIL
}


final class CarEngine extends Engine {
    public CarEngine(FuelType fuelType, int power) {
        super(fuelType, power);
    }
}

final class PlaneEngine extends Engine {
    public PlaneEngine(FuelType fuelType, int power) {
        super(fuelType, power);
    }
}

final class ShipEngine extends Engine {
    public ShipEngine(FuelType fuelType, int power) {
        super(fuelType, power);
    }
}

final class BikeEngine extends Engine {
    public BikeEngine(FuelType fuelType, int power) {
        super(fuelType, power);
    }
}


class Car extends Transport {
    public Car(String model, Engine engine) {
        super(model, engine);
    }

    @Override
    public void start() {
        System.out.println("Автомобиль " + model + " заводится");
    }

    @Override
    public void stop() {
        System.out.println("Автомобиль " + model + " останавливается");
    }

    @Override
    public String getInfo() {
        return "Автомобиль " + model +
                "\nДвигатель: " + engine.getPower() + " л.с." +
                "\nТип топлива: " + engine.getFuelType();
    }
}

class Plane extends Transport {
    public Plane(String model, Engine engine) {
        super(model, engine);
    }

    @Override
    public void start() {
        System.out.println("Самолет " + model + " взлетает");
    }

    @Override
    public void stop() {
        System.out.println("Самолет " + model + " приземляется");
    }

    @Override
    public String getInfo() {
        return "Самолет " + model +
                "\nДвигатель: " + engine.getPower() + " л.с." +
                "\nТип топлива: " + engine.getFuelType();
    }
}

class Ship extends Transport {
    public Ship(String model, Engine engine) {
        super(model, engine);
    }

    @Override
    public void start() {
        System.out.println("Корабль " + model + " отплывает");
    }

    @Override
    public void stop() {
        System.out.println("Корабль " + model + " причаливает");
    }

    @Override
    public String getInfo() {
        return "Корабль " + model +
                "\nДвигатель: " + engine.getPower() + " л.с." +
                "\nТип топлива: " + engine.getFuelType();
    }
}

class Bicycle extends Transport {
    public Bicycle(String model, Engine engine) {
        super(model, engine);
    }

    @Override
    public void start() {
        System.out.println("Велосипед " + model + " начинает движение");
    }

    @Override
    public void stop() {
        System.out.println("Велосипед " + model + " останавливается");
    }

    @Override
    public String getInfo() {
        return "Велосипед " + model +
                "\nТип привода: " + (engine == null ? "Механический" : "С двигателем") +
                "\nМощность: " + (engine == null ? "0" : engine.getPower()) + " л.с.";
    }
}


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Transport> transportList = new ArrayList<>();

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nМеню:");
            System.out.println("1. Создать новый транспорт");
            System.out.println("2. Просмотреть информацию о транспорте");
            System.out.println("3. Запустить транспорт");
            System.out.println("4. Остановить транспорт");
            System.out.println("5. Выйти");

            int choice = getUserChoice(1, 5);

            switch (choice) {
                case 1:
                    createTransport();
                    break;
                case 2:
                    showTransportInfo();
                    break;
                case 3:
                    startTransport();
                    break;
                case 4:
                    stopTransport();
                    break;
                case 5:
                    exit = true;
                    break;
            }
        }
    }

    private static void createTransport() {
        System.out.println("Выберите тип транспорта:");
        System.out.println("1. Автомобиль");
        System.out.println("2. Самолет");
        System.out.println("3. Корабль");
        System.out.println("4. Велосипед");

        int type = getUserChoice(1, 4);
        String model = getUserInputModelTransport();

        Engine engine = null;
        if (type != 4) {
            int power = getUserInputEnginePower();
            System.out.println("Выберите тип топлива:");
            for (int i = 0; i < FuelType.values().length; i++) {
                System.out.println((i + 1) + ". " + FuelType.values()[i]);
            }
            int fuelChoice = getUserChoice(1, FuelType.values().length);
            FuelType fuelType = FuelType.values()[fuelChoice - 1];

            switch (type) {
                case 1:
                    engine = new CarEngine(fuelType, power);
                    break;
                case 2:
                    engine = new PlaneEngine(fuelType, power);
                    break;
                case 3:
                    engine = new ShipEngine(fuelType, power);
                    break;
            }
        }

        switch (type) {
            case 1:
                transportList.add(new Car(model, engine));
                break;
            case 2:
                transportList.add(new Plane(model, engine));
                break;
            case 3:
                transportList.add(new Ship(model, engine));
                break;
            case 4:
                transportList.add(new Bicycle(model, engine));
                break;
        }

        System.out.println("Транспорт успешно создан!");
    }

    private static void showTransportInfo() {
        if (transportList.isEmpty()) {
            System.out.println("Нет созданного транспорта");
            return;
        }

        System.out.println("Выберите транспорт для просмотра информации:");
        for (int i = 0; i < transportList.size(); i++) {
            System.out.println((i + 1) + ". " + transportList.get(i).getModel());
        }

        int choice = getUserChoice(1, transportList.size());
        System.out.println("\n" + transportList.get(choice - 1).getInfo());
    }

    private static void startTransport() {
        if (transportList.isEmpty()) {
            System.out.println("Нет созданного транспорта");
            return;
        }

        System.out.println("Выберите транспорт для запуска:");
        for (int i = 0; i < transportList.size(); i++) {
            System.out.println((i + 1) + ". " + transportList.get(i).getModel());
        }

        int choice = getUserChoice(1, transportList.size());
        transportList.get(choice - 1).start();
    }

    private static void stopTransport() {
        if (transportList.isEmpty()) {
            System.out.println("Нет созданного транспорта");
            return;
        }

        System.out.println("Выберите транспорт для остановки:");
        for (int i = 0; i < transportList.size(); i++) {
            System.out.println((i + 1) + ". " + transportList.get(i).getModel());
        }

        int choice = getUserChoice(1, transportList.size());
        transportList.get(choice - 1).stop();
    }

    private static int getUserChoice(int min, int max) {
        int choice;
        while (true) {
            try {
                System.out.print("Введите номер: ");
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Неверный ввод. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Введите число.");
            }
        }
    }

    private static String getUserInputModelTransport() {
        System.out.print("Введите модель транспорта: ");
        return scanner.nextLine();
    }

    private static int getUserInputEnginePower() {
        while (true) {
            try {
                System.out.print("Введите мощность двигателя (л.с.): ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Введите число.");
            }
        }
    }
}

