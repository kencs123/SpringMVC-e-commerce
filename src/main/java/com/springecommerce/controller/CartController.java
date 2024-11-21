package com.springecommerce.controller;

import com.springecommerce.model.Order;
import com.springecommerce.model.OrderProduct;
import com.springecommerce.model.Product;
import com.springecommerce.repository.OrderProductRepository;
import com.springecommerce.repository.OrderRepository;
import com.springecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
@SessionAttributes("order")
public class CartController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @ModelAttribute("order")
    public Order getOrder() {
        Order order = new Order();
        order.setStatus("PENDING");
        return order;
    }

    @GetMapping
    public String viewCart(@ModelAttribute("order") Order order, Model model) {
        model.addAttribute("cartItems", order.getOrderProducts());;
        return "cart/view";

    }
    @PostMapping("/add/{id}")
    public String AddToCart(@ModelAttribute("order") Order order, @PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        Optional<OrderProduct> existing = order.getOrderProducts().stream().filter(op -> op.getProduct().getId().equals(product.getId())).findFirst();
        if (existing.isPresent()){
            existing.get().setQuantity(existing.get().getQuantity() + 1);
        }
        else{
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(1);
            orderProduct.setOrder(order);
            order.getOrderProducts().add(orderProduct);
        }
        return "redirect:/cart";
    }
    @PostMapping("/update/{id}")
    public String editCart(@ModelAttribute("order") Order order, @PathVariable Long id, @RequestParam int quantity) {
       order.getOrderProducts().stream().filter(op -> op.getProduct().getId().equals(id)).findFirst().ifPresent(op -> op.setQuantity(quantity));
        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String deleteCart(@ModelAttribute("order") Order order, @PathVariable Long id) {
        order.getOrderProducts().removeIf(op -> op.getProduct().getId().equals(id));
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("order") Order order, @RequestParam String name, @RequestParam String address) {
        Logger logger = LoggerFactory.getLogger(CartController.class);
        try {
            if (name == null || name.isEmpty() || address == null || address.isEmpty()) {
                throw new IllegalArgumentException("Name and address must not be empty");
            }

            order.setName(name);
            order.setAddress(address);
            orderRepository.save(order);

            // Save order products
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                Product product = productRepository.findById(orderProduct.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                orderProduct.setOrder(order);

                orderProduct.setProduct(product);
                orderProductRepository.save(orderProduct);
                orderProductRepository.saveAndFlush(orderProduct);
            }

            return "redirect:/orders";
        } catch (Exception e) {
            logger.error("Error during checkout", e);
            return "error/500";
        }
    }


}



