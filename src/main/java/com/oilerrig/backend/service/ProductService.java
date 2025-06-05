package com.oilerrig.backend.service;

import com.oilerrig.backend.data.dto.ProductDto;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    public ProductDto getProduct(int productId) {
        return null;
    }


    // TODO paginate
    public List<ProductDto> getAllProducts() {
        return null;
    }

    // TODO define via vendor api
    public void getProductDetails(int productId) {

    }

    @PostConstruct
    public void initializeCaches() {

    }

    @Scheduled(fixedDelay = 1000)
    public void updateCaches() {

    }
    
}
