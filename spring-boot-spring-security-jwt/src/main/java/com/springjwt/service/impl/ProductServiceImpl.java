package com.springjwt.service.impl;

import com.springjwt.models.Product;
import com.springjwt.models.User;
import com.springjwt.repository.ProductRepo;
import com.springjwt.security.services.UserDetailsImpl;
import com.springjwt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public void save(Product p) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User newUser = new User();
        if (principal instanceof UserDetailsImpl) {
            newUser.setId(((UserDetailsImpl) principal).getId());
            newUser.setEmail(((UserDetailsImpl) principal).getEmail());
            newUser.setUsername(((UserDetailsImpl) principal).getUsername());
            p.setUser(newUser);
        }
        productRepo.save(p);
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<Product>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            Long id = ((UserDetailsImpl) principal).getId();
            productRepo.findByUser_Id(id).forEach(p-> {
                products.add(p);
            });
        }
        return products;
    }
}
