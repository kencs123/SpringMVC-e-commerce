package com.springecommerce.controller;

import com.springecommerce.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.springecommerce.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }
    @GetMapping ("/create")
    public String showCreateForm(Model model){
        model.addAttribute("product", new ProductDTO());
        return "products/form";
    }
    @PostMapping
    public String saveProduct (@ModelAttribute ProductDTO productDTO){        productService.saveProduct(productDTO);
        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        model.addAttribute("product",productService.getProductById(id));
        return "products/form";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }

}
