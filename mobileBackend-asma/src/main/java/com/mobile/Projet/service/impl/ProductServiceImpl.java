package com.mobile.Projet.service.impl;

import com.mobile.Projet.model.Product;
import com.mobile.Projet.repository.ProductRepository;
import com.mobile.Projet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Override
    public List<Product> findProductsByCategoryId(long id) {
        List<Product> result= new ArrayList<>();
        List<Product> existingProducts=productRepository.findAll();
        for (Product product: existingProducts){
            if(product.getCategory().getCategoryId()==id){
                result.add(product);
            }
        }
        return result;
    }
}
