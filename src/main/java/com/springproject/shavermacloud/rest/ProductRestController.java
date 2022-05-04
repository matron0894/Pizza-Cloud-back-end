package com.springproject.shavermacloud.rest;

import com.springproject.shavermacloud.domain.Product;
import com.springproject.shavermacloud.repos.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/products", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
public class ProductRestController {

    private final ProductRepository productRepo;

    public ProductRestController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping(params = "recent")
    public Iterable<Product> recentProducts() {
        PageRequest page = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        return productRepo.findAll(page).getContent();
    }

    @GetMapping
    public Iterable<Product> allProducts() {
        return productRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> productById(@PathVariable("id") Long id) {
        Optional<Product> opt = productRepo.findById(id);
        return opt.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }


    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Product postProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }

}
