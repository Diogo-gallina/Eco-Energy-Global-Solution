package com.eco_energy.repository;

import com.eco_energy.model.Alert;
import com.eco_energy.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByDeviceCustomer(Customer customer);

    Optional<Alert> findByIdAndDeviceCustomer(Long id, Customer customer);

}
