package com.springecommerce.mapper;

import com.springecommerce.dto.ProductDTO;
import com.springecommerce.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDTO toDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }
    public Product toEntity(ProductDTO productDTO){
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        return product;
    }

}
