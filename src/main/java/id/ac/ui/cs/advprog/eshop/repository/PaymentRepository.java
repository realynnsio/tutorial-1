package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaymentRepository {
    private Set<String> paymentIds = new HashSet<>();
    private List<Payment> paymentsData = new ArrayList<>();

    public Payment save(Payment payment) {
        if (!paymentIds.add(payment.getId())) {
            throw new IllegalStateException("Payment with ID " + payment.getId() + " already exists.");
        }

        paymentsData.add(payment);
        return payment;
    }

    public Payment setStatus(Payment payment, String status) {
        Payment changeStatus = findById(payment.getId());

        if (changeStatus == null) {
            throw new NoSuchElementException("Payment with ID " + payment.getId() + " not found.");
        }

        changeStatus.setStatus(status);
        return changeStatus;
    }
    
    public Payment findById(String id) {
        for (Payment savedPayment : paymentsData) {
            if (savedPayment.getId().equals(id)) {
                return savedPayment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentsData);
    }
}
