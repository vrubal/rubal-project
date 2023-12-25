package com.rubal.lld.parkinglot;

import java.util.List;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ParkingFloor {
    int floor;
    int capacity;
    int available;
    Map<ParkingSpotType, List<ParkingSpot>> parkingSpots;

    public ParkingFloor(int floor, int capacity, int available, Map<ParkingSpotType, List<ParkingSpot>> parkingSpots) {
        this.floor = floor;
        this.capacity = capacity;
        this.available = available;
        this.parkingSpots = parkingSpots;
    }

    public void display(){
        System.out.println("Floor number:"+floor);

    }
}
