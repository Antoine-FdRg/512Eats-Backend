package commonlibrary.model.payment;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Payment {
    private int id;
    private double amount;
    private LocalDateTime time;
    private static int idCounter = 0;

    public Payment(double amount, LocalDateTime time) {
        this.id = idCounter++;
        this.amount = amount;
        this.time = time;
    }

    public PaymentDTO convertPaymentToPaymentDto() {
        return new PaymentDTO(id, amount);
    }


}
