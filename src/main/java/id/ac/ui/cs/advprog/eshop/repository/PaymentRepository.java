package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class PaymentRepository {
    private List<Payment> paymentsData = new ArrayList<>();

    public Payment save(Payment payment) {
        for (Payment savedPayment : paymentsData) {
            if (savedPayment.getId().equals(payment.getId())) {
                throw new IllegalStateException();
            }
        }

        paymentsData.add(payment);
        return payment;
    }

    public Payment setStatus(Payment payment, String status) {
        Payment changeStatus = findById(payment.getId());

        if (changeStatus == null) {
            throw new NoSuchElementException();
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
        return paymentsData;
    }
}
