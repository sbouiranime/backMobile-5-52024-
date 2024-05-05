package com.mobile.Projet.controller;

import com.mobile.Projet.model.Product;
import com.mobile.Projet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ProductService productService;
    @GetMapping(value = "/showCategoryProducts/{categoryID}")
    public List<Product> showCategoryProducts(@PathVariable long categoryID) {
        return productService.findProductsByCategoryId(categoryID);
    }
}
