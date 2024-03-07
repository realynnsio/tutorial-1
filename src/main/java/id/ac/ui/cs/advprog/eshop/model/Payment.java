package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    Map<String, String> paymentData;
    String status;
    Order order;

    public Payment(String id, String method, String status, Map<String, String> paymentData){
        this.id = id;
        setMethod(method);
        setStatus(status);

        if (paymentData.isEmpty()) throw new IllegalArgumentException();
        this.paymentData = paymentData;

        if (this.method.equals(PaymentMethod.VOUCHER.getValue())) {
            validateVoucherPayment();
        } else if (this.method.equals(PaymentMethod.CASH_ON_DELIVERY.getValue())) {
            validateCashOnDeliveryPayment();
        }
    }

    public Payment(String id, String method, Map<String, String> paymentData) {
        this(id, method, PaymentStatus.PROCESSING.getValue(), paymentData);
    }

    private void setMethod(String method) {
        if (PaymentMethod.contains(method)) {
            this.method = method;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void validateVoucherPayment() {
        String voucher = this.paymentData.get("voucherCode");

        this.status = PaymentStatus.REJECTED.getValue();
        boolean isNull = voucher == null? true : false;
        boolean isSixteen = voucher.length() == 16? true : false;
        boolean isEshop = voucher.startsWith("ESHOP")? true : false;
        int count = 0;

        for (int i = 0; i < voucher.length(); i++) {
            if (Character.isDigit(voucher.charAt(i))) count++;
        }

        if (!isNull && isSixteen && isEshop && count == 8) {
            this.status = PaymentStatus.SUCCESS.getValue();
        }
    }

    private void validateCashOnDeliveryPayment() {
        String[] keys = {"address", "deliveryFee"};

        for (String key : keys) {
            String value = this.paymentData.get(key);
            if (value == null || value.equals("")) {
                this.status = PaymentStatus.REJECTED.getValue();
            }
        }
    }

    public Order setOrder(Order order) {
        if (order.getId().equals(this.id)) {
            this.order = order;
        } else {
            throw new IllegalArgumentException("Order ID: " + order.getId() + "does not match Payment ID: " + this.id);
        }

        return order;
    }
}
