package com.rubal.lld.parkinglot.vehicle;

/**
 * @author vruba
 * @created
 * @project rubal-project
 */
public abstract class Vehicle {
    VehicleType type;
    String registrationNumber;

    public Vehicle(VehicleType type, String registrationNumber) {
        this.type = type;
        this.registrationNumber = registrationNumber;
    }
}
