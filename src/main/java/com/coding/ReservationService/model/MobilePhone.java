package com.coding.ReservationService.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class MobilePhone {

    private String name;
    private boolean available;
    private Instant bookedDate;
    private Tester bookedBy;
}
