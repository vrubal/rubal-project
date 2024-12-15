package com.rubal.transformers.dp.observer;

import java.util.ArrayList;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ObserverDemo {
    public static void main(String[] args) {
        Subject subject = new ConcreteSubject(new ArrayList<>());
        subject.addObserver(new ConcreteObserver());
        subject.addObserver(new ConcreteObserver());
        subject.addObserver(new ConcreteObserver());

        subject.notifyObservers();
    }
}
