package com.springecommerce.service;

import com.springecommerce.dto.ProductDTO;
import com.springecommerce.mapper.ProductMapper;
import com.springecommerce.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springecommerce.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public ProductMapper productMapper;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("product not found"));
        return productMapper.toDTO(product);
    }
    public ProductDTO saveProduct (ProductDTO productDTO){
        Product product = productMapper.toEntity(productDTO);
        return productMapper.toDTO(productRepository.save(product));
    }
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
