package com.eco_energy.repository;

import com.eco_energy.model.Address;
import com.eco_energy.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCustomer(Customer customer);

    Optional<Address> findByIdAndCustomer(Long id, Customer customer);

}
