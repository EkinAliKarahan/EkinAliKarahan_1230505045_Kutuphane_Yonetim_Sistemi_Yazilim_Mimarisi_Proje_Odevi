package com.yourname.library.pattern.observer;

import java.util.ArrayList;
import java.util.List;

public class InventorySubject {

    private final List<IInventoryObserver> observers = new ArrayList<>();

    public void registerObserver(IInventoryObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(IInventoryObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (IInventoryObserver obs : observers) {
            obs.update();
        }
    }
}