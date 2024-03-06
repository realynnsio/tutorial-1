package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    Map<String, String> paymentData;
    String status;

    public Payment(String id, String method, String status, Map<String, String> paymentData){
        this.id = id;
        setMethod(method);
        setStatus(status);

        if (paymentData.isEmpty()) throw new IllegalArgumentException();
        this.paymentData = paymentData;

        if (this.method.equals("VOUCHER")) {
            checkPaymentVoucher();
        } else if (this.method.equals("CASH_ON_DELIVERY")) {
            checkPaymentCashOnDelivery();
        }
    }

    private void setMethod(String method) {
        String[] methods = {"VOUCHER", "CASH_ON_DELIVERY"};

        if (Arrays.stream(methods).noneMatch(item -> (item.equals(method)))) {
            throw new IllegalArgumentException();
        } else {
            this.method = method;
        }
    }

    public void setStatus(String status) {
        String[] statusList = {"SUCCESS", "REJECTED"};

        if (Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))) {
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
    }

    private void checkPaymentVoucher() {
        String voucher = this.paymentData.get("voucherCode");

        this.status = "REJECTED";
        boolean isNull = voucher == null? true : false;
        boolean isSixteen = voucher.length() == 16? true : false;
        boolean isEshop = voucher.startsWith("ESHOP")? true : false;
        int count = 0;

        for (int i = 0; i < voucher.length(); i++) {
            if (Character.isDigit(voucher.charAt(i))) count++;
        }

        if (!isNull && isSixteen && isEshop && count == 8) {
            this.status = "SUCCESS";
        }
    }

    private void checkPaymentCashOnDelivery() {
        String[] keys = {"address", "deliveryFee"};

        for (String key : keys) {
            String value = this.paymentData.get(key);
            if (value == null || value.equals("")) {
                this.status = "REJECTED";
            }
        }
    }
}
