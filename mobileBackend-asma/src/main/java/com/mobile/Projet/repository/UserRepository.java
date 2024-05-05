package com.mobile.Projet.repository;

import com.mobile.Projet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Long> {

    public boolean existsByEmail(String email);
    public User findByEmailIgnoreCase(String email);
}
