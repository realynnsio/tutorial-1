package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;
    List<Payment> paymentList;
    List<Order> orderList;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        orderList = new ArrayList<>();
        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        orderList.add(order1);
        Order order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products, 1708570000L, "Safira Sudrajat");
        orderList.add(order2);

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        paymentList = new ArrayList<>();
        Payment payment1 = new Payment(order1.getId(),
                PaymentMethod.VOUCHER.getValue(), paymentData);
        paymentList.add(payment1);

        paymentData.clear();
        paymentData.put("address", "Home");
        paymentData.put("deliveryFee", "Amount");

        Payment payment2 = new Payment(order2.getId(),
                PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);
        paymentList.add(payment2);
    }

    @Test
    void testAddPayment() {
        Payment payment = paymentList.get(0);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment addedPayment = paymentService.addPayment(orderList.get(0),
                payment.getMethod(), payment.getPaymentData());

        assertEquals(addedPayment.getId(), orderList.get(0).getId());
        assertEquals(addedPayment.getId(), payment.getId());
        assertEquals(addedPayment.getMethod(), payment.getMethod());
        assertEquals(addedPayment.getStatus(), payment.getStatus());
        assertEquals(addedPayment.getPaymentData(), payment.getPaymentData());

        verify(paymentService, times(1)).addPayment(any(Order.class), any(String.class), any(Map.class));
    }

}
