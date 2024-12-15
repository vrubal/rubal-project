package com.rubal.transformers.dp.observer;

import java.util.List;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class ConcreteSubject implements Subject{
    private List<Observer> observers;

    public ConcreteSubject(List<Observer> observers) {
        this.observers = observers;
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers() {
        observers.forEach(e->e.update("newstate"));
    }
}
