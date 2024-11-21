package com.eco_energy.repository;

import com.eco_energy.model.Customer;
import com.eco_energy.model.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {

    List<EnergyConsumption> findByDeviceCustomer(Customer customer);

    Optional<EnergyConsumption> findByIdAndDeviceCustomer(Long id, Customer customer);

}
