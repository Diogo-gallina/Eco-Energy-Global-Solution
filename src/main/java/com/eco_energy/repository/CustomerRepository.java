package com.eco_energy.repository;

import com.eco_energy.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUsername(String username);

}
