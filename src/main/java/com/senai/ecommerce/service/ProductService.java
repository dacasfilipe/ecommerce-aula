package com.senai.ecommerce.service;

import com.senai.ecommerce.dtos.CategoryDTO;
import com.senai.ecommerce.dtos.ProductDTO;
import com.senai.ecommerce.entities.Category;
import com.senai.ecommerce.entities.Product;
import com.senai.ecommerce.repository.CategoryRepository;
import com.senai.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return productToProductDTO(product);

    }
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::productToProductDTO).toList();
    }
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImgUrl(productDTO.getImgUrl());

        Set<Category> categories = productDTO.getCategories().stream()
                .map(categoryDTO -> categoryRepository.findById(categoryDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + categoryDTO.getId())))
                .collect(Collectors.toSet());

        product.setCategories(categories);

// Agora salva apenas uma vez
        product = productRepository.save(product);

        return productToProductDTO(product);

    }


    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        Product product = productDTOToProduct(productDTO);
        product.setId(id);
        // o repository não consegue salvar um DTO, ele salva entity
        product = productRepository.save(product);
        return productToProductDTO(product);
    }

    @Transactional
    public void delete(Long id){
        productRepository.deleteById(id);
    }


    private ProductDTO productToProductDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                product.getCategories().stream()
                        .map(category -> new CategoryDTO(category.getId(), category.getName()))
                        .toList()
        );
    }

    private Product productDTOToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImgUrl(productDTO.getImgUrl());

        // Buscando categorias existentes no banco
        Set<Category> categories = productDTO.getCategories().stream()
                .map(categoryDTO -> categoryRepository.findById(categoryDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + categoryDTO.getId())))
                .collect(Collectors.toSet());

        product.setCategories(categories);
        return product;
    }

//    private ProductDTO toDTO(Product product) {
//        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getImgUrl(), product.getCategories());
//    }
//    private Product toEntity(ProductDTO productDTO) {
//        return new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getImgUrl(), productDTO.getCategories());
//    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream().map(this::productToProductDTO).collect(Collectors.toList());
    }
}
