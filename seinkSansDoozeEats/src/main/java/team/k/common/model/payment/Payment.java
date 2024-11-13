package team.k.common.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class Payment {
    private double amount;
    private LocalDateTime time;
}
