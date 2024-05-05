package com.mobile.Projet.repository;

import com.mobile.Projet.model.Category;
import com.mobile.Projet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
