package com.rubal.lld.parkinglot.ticket;

import com.rubal.lld.parkinglot.vehicle.Vehicle;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface TicketingService {

    ParkingTicket issueTicket(Vehicle vehicle);
    void closeTicket(ParkingTicket ticket);
}
