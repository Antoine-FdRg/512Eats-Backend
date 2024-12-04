package commonlibrary.dto;

import commonlibrary.model.payment.Payment;

import java.time.LocalDateTime;

public record PaymentDTO(int id, double amount) {
    private static int id_counter = 0;
    public PaymentDTO(double amount) {
        this(id_counter++, amount);
    }

    /**
     * Convert PaymentDTO to Payment
     *
     * @return un Payment
     */
    public Payment convertPaymentDtoToPayment() {
        return new Payment(id, amount, LocalDateTime.now());
    }
}
