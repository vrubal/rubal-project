package com.rubal.lld.parkinglot.ticket;

import com.rubal.lld.parkinglot.vehicle.Vehicle;
import jdk.jshell.Snippet;

import java.time.LocalDateTime;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ParkingTicket {
    String id;
    LocalDateTime start;
    LocalDateTime end;
    TicketStatus status;
    Vehicle vehicle;

}
