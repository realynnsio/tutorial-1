package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public Product create(Product product);
    public void delete(String productId);
    public List<Product> findAll();
    public Product findById(String productId);
    public Product edit(Product product);
}