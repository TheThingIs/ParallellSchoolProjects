package main.java.model;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
