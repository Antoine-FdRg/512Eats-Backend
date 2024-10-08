package team.k.order;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Payment {
    private double amount;
    private LocalTime time;
}
