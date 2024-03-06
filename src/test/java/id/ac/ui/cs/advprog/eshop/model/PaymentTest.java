package id.ac.ui.cs.advprog.eshop.model;


import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
                    PaymentMethod.VOUCHER.getValue(),
                    PaymentStatus.SUCCESS.getValue(), paymentData);
        });
    }

    @Test
    void testCreatePaymentValidVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.REJECTED.getValue(), paymentData);

        assertSame(this.paymentData, payment.getPaymentData());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals(PaymentMethod.VOUCHER.getValue(), payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentNotValidVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC578");
        Payment payment1 = new Payment("id1",
                PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        paymentData.put("voucherCode", "ESHOA1234ABC5678");
        Payment payment2 = new Payment("id2",
                PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        paymentData.put("voucherCode", "ESHOP1234ABC5D78");
        Payment payment3 = new Payment("id3",
                PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment1.getStatus());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment2.getStatus());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment3.getStatus());

    }

    @Test
    void testCreatePaymentValidCashOnDelivery() {
        paymentData.put("address", "Home");
        paymentData.put("deliveryFee", "Amount");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        assertSame(this.paymentData, payment.getPaymentData());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals(PaymentMethod.CASH_ON_DELIVERY.getValue(), payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentNotValidCashOnDelivery() {
        paymentData.put("address", "Home");
        paymentData.put("deliveryFee", "");

        Payment payment1 = new Payment("id1",
                PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        paymentData.remove("deliveryFee");
        Payment payment2 = new Payment("id2",
                PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment1.getStatus());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment2.getStatus());
    }

    @Test
    void testCreatePaymentMethodMismatch() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                PaymentMethod.CASH_ON_DELIVERY.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentInvalidMethod() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                    "MEOW", PaymentStatus.SUCCESS.getValue(), paymentData);
        });
    }

    @Test
    void testSetStatusToRejected() {
        paymentData.put("voucherCode", "ESHOP1234ABC578");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC578");

        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6",
                PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}
