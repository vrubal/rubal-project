package com.rubal.transformers.dp.observer;

/**
 * @author
 * @created
 * @project rubal-project
 */
/*
    Object being observed
 */
public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
