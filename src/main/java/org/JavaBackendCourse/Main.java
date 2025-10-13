package org.JavaBackendCourse;

import java.util.ArrayList;
import java.util.List;


interface StringBuilderObserver {
    void onChange(String newValue);
}


class ObservableStringBuilder {
    private final StringBuilder builder = new StringBuilder();
    private final List<StringBuilderObserver> observers = new ArrayList<>();


    public void addObserver(StringBuilderObserver observer) {
        observers.add(observer);
    }


    private void notifyObservers() {
        String currentValue = builder.toString();
        for (StringBuilderObserver observer : observers) {
            observer.onChange(currentValue);
        }
    }


    public ObservableStringBuilder append(String str) {
        builder.append(str);
        notifyObservers();
        return this;
    }


    public String toString() {
        return builder.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        ObservableStringBuilder observ = new ObservableStringBuilder();

        observ.addObserver(newValue -> System.out.println("Observ1: new " + newValue));
        observ.addObserver(newValue -> System.out.println("Observ2: change " + newValue));

        observ.append("hello");
        observ.append(" world");
    }
}
