package com.mobile.Projet.service;

import com.mobile.Projet.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    public List<Product> findProductsByCategoryId(long id);
}
