package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(order.getId(), method, paymentData);
        Payment savedPayment = paymentRepository.save(payment);
        savedPayment.setOrder(order);
        setStatus(savedPayment, savedPayment.getStatus());

        return savedPayment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {

        Payment changedStatusPayment = getPayment(payment.getId());

        changedStatusPayment.setStatus(status);

        if (status.equals(PaymentStatus.SUCCESS.getValue())) {
            changedStatusPayment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (status.equals(PaymentStatus.REJECTED.getValue())) {
            changedStatusPayment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }

        return changedStatusPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
