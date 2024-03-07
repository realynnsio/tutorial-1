package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;
    List<Product> products;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        this.payments = new ArrayList<>();
        Payment payment1 = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                PaymentMethod.VOUCHER.getValue(), paymentData);
        this.payments.add(payment1);

        Payment payment2 = new Payment("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                PaymentMethod.VOUCHER.getValue(), paymentData);
        this.payments.add(payment2);

        paymentData.clear();
        paymentData.put("address", "Home");
        paymentData.put("deliveryFee", "Amount");

        Payment payment3 = new Payment("e334ef40-9eff-4da8-9487-8ee697ecbf1e",
                PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);
        this.payments.add(payment3);

        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);

        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);

        this.products.add(product1);
        this.products.add(product2);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(1);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payment.getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testSaveDuplicateId() {
        Payment payment1 = payments.get(1);
        paymentRepository.save(payment1);

        Payment payment2 = payments.get(2);
        Payment newPayment = new Payment(payment1.getId(), payment2.getMethod(),
                payment2.getPaymentData());

        assertThrows(IllegalStateException.class, () -> {
            paymentRepository.save(newPayment);
        });
    }

    @Test
    void testSetStatusIfPaymentExist() {
        Payment payment1 = payments.get(1);

        paymentRepository.save(payment1);
        paymentRepository.setStatus(payment1, PaymentStatus.REJECTED.getValue());

        Payment findResult = paymentRepository.findById(payment1.getId());
        assertEquals(PaymentStatus.REJECTED.getValue(), findResult.getStatus());
    }

    @Test
    void testSetStatusIfPaymentDoesNotExist() {
        Payment payment1 = payments.get(1);

        assertThrows(NoSuchElementException.class, () -> {
            paymentRepository.setStatus(payment1, PaymentStatus.REJECTED.getValue());
        });
    }

    @Test
    void testFindByIdIfIdFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findResult.getId());
        assertEquals(payments.get(1).getStatus(), findResult.getStatus());
        assertEquals(payments.get(1).getMethod(), findResult.getMethod());
        assertEquals(payments.get(1).getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById("zczc");
        assertNull(findResult);
    }

    @Test
    void testFindAllPayments() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(3, paymentList.size());
    }

    @Test
    void testSetOrderIfIdFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }
    }

}
