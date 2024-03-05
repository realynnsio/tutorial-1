package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    public Order createOrder(Order order);
    public Order updateStatus(String orderId, String status);
    public Order findById(String orderId);
    public List<Order> findAllByAuthor(String author);
}
