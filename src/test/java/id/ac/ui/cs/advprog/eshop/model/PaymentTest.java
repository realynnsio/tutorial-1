package id.ac.ui.cs.advprog.eshop.model;


import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }

    @Test
    void testCreatePaymentEmptyPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                    "VOUCHER", "SUCCESS", paymentData);
        });
    }

    @Test
    void testCreatePaymentValidVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                "VOUCHER", "SUCCESS", paymentData);

        assertSame(this.paymentData, payment.getPaymentData());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentNotValidVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC578");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                "VOUCHER", "SUCCESS", paymentData);

        assertSame(this.paymentData, payment.getPaymentData());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentValidCashOnDelivery() {
        paymentData.put("address", "Home");
        paymentData.put("deliveryFee", "Amount");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                "CASH_ON_DELIVERY", "SUCCESS", paymentData);

        assertSame(this.paymentData, payment.getPaymentData());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("CASH_ON_DELIVERY", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentNotValidCashOnDelivery() {
        paymentData.put("address", "Home");
        paymentData.put("deliveryFee", "");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                "CASH_ON_DELIVERY", "SUCCESS", paymentData);

        assertSame(this.paymentData, payment.getPaymentData());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("CASH_ON_DELIVERY", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentMethodMismatch() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                    "CASH_ON_DELIVERY", "SUCCESS", paymentData);
        });
    }

    @Test
    void testCreatePaymentInvalidMethod() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                    "MEOW", "SUCCESS", paymentData);
        });
    }

    @Test
    void testSetStatusToRejected() {
        paymentData.put("voucherCode", "ESHOP1234ABC578");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                "VOUCHER", "SUCCESS", paymentData);

        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC578");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                "VOUCHER", "SUCCESS", paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}
