package com.mth.eshop.repository;

import com.mth.eshop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {}
