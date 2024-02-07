package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;
    private int idCount = 0;

    @GetMapping("/")
    public String goToHomePage(Model model){
        return "homePage";
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        product.setProductId(String.valueOf(idCount));
        idCount++;
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/edit/{productId}")
    public String editProductPage(@PathVariable String productId, Model model) {
        try {
            Product product = service.findById(productId);
            model.addAttribute("product", product);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        return "editProduct";
    }

    @PostMapping("/edit-product/{productId}")
    public String editProductPost(@ModelAttribute Product product, Model model, @PathVariable String productId) {
        product.setProductId(productId);
        service.edit(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProductPost(@PathVariable String productId, Model model) {
        service.delete(productId);
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }
}