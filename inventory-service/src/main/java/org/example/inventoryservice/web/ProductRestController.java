package org.example.inventoryservice.web;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.inventoryservice.entities.Product;
import org.example.inventoryservice.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
//@CrossOrigin("*")
public class ProductRestController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Product> getProducts() {
       return productRepository.findAll();
    }

    @GetMapping("/products/id")
    @PreAuthorize("hasAuthority('USER')")
    public Product getProduct(@PathVariable String id) {
        return productRepository.findById(id).get();
    }

    @GetMapping("/auth")

    public Authentication authentication(Authentication authentication){
         return authentication;
    }

}
