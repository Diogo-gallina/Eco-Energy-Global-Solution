package com.eco_energy.repository;

import com.eco_energy.model.Customer;
import com.eco_energy.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByCustomer(Customer customer);

    Optional<Device> findByIdAndCustomer(Long id, Customer customer);

}
