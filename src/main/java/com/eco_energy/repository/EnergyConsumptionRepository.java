package com.eco_energy.repository;

import com.eco_energy.model.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {
}
