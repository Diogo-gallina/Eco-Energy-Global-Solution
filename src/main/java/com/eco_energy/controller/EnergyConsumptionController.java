package com.eco_energy.controller;

import com.eco_energy.dto.energyConsumption.CreateEnergyConsumptionDTO;
import com.eco_energy.dto.energyConsumption.UpdateEnergyConsumptionDTO;
import com.eco_energy.model.Alert;
import com.eco_energy.model.Customer;
import com.eco_energy.model.Device;
import com.eco_energy.model.EnergyConsumption;
import com.eco_energy.model.enums.AlertLevel;
import com.eco_energy.repository.AlertRepository;
import com.eco_energy.repository.DeviceRepository;
import com.eco_energy.repository.EnergyConsumptionRepository;
import com.eco_energy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/energy-consumption")
public class EnergyConsumptionController {

    @Autowired
    EnergyConsumptionRepository energyConsumptionRepository;
    @Autowired
    AlertRepository alertRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    private UserService userService;

    @GetMapping("register")
    public String register(CreateEnergyConsumptionDTO energyConsumptionDTO, Model model, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        model.addAttribute("energyConsumptionDTO", new CreateEnergyConsumptionDTO(null, null));
        model.addAttribute("devices", deviceRepository.findByCustomer(customer));
        return "energy-consumption/register";
    }

    @PostMapping("register")
    @Transactional
    public String register(CreateEnergyConsumptionDTO energyConsumptionDTO, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        Device device = deviceRepository.findByIdAndCustomer(energyConsumptionDTO.deviceId(), customer)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado para o cliente autenticado!"));

        EnergyConsumption energyConsumption = new EnergyConsumption(
                energyConsumptionDTO.usageTime(),
                device
        );

        Double totalKwhConsumption = customer.getDevices().stream()
                .flatMap(d -> d.getEnergyConsumptions().stream())
                .mapToDouble(EnergyConsumption::getKwhConsumption)
                .sum();

        double totalCost = calculateTotalEnergyCostForCustomer(customer);

        if (totalKwhConsumption > 30) {
            this.registerAutomaticAlert(energyConsumption.getDevice(), totalKwhConsumption, totalCost);
        }

        energyConsumptionRepository.save(energyConsumption);
        redirectAttributes.addFlashAttribute("msg", "Consumo de energia registrado com sucesso!");
        return "redirect:/energy-consumption/list";
    }

    @GetMapping("list")
    public String list(Model model, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository.findByDeviceCustomer(customer);
        model.addAttribute("energyConsumptions", energyConsumptions);
        return "energy-consumption/list";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        EnergyConsumption energyConsumption = energyConsumptionRepository.findByIdAndDeviceCustomer(id, customer)
                .orElseThrow(() -> new IllegalArgumentException("Consumo de energia não encontrado!"));

        UpdateEnergyConsumptionDTO energyConsumptionDTO = new UpdateEnergyConsumptionDTO(
                energyConsumption.getId(),
                energyConsumption.getUsageTime(),
                energyConsumption.getDevice().getId()
        );

        model.addAttribute("energyConsumptionDTO", energyConsumptionDTO);
        model.addAttribute("devices", deviceRepository.findByCustomer(customer));
        return "energy-consumption/update";
    }

    @PostMapping("update")
    @Transactional
    public String update(UpdateEnergyConsumptionDTO energyConsumptionDTO, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        EnergyConsumption energyConsumption = energyConsumptionRepository.findByIdAndDeviceCustomer(energyConsumptionDTO.id(), customer)
                .orElseThrow(() -> new IllegalArgumentException("Consumo de energia não encontrado!"));

        Device device = deviceRepository.findByIdAndCustomer(energyConsumptionDTO.deviceId(), customer)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        energyConsumption.setUsageTime(energyConsumptionDTO.usageTime());
        energyConsumption.setKwhConsumption(device);
        Double kwhConsumption = energyConsumption.getKwhConsumption();

        energyConsumption.setEnergyCost(kwhConsumption);
        energyConsumption.setDevice(device);

        Double totalKwhConsumption = customer.getDevices().stream()
                .flatMap(d -> d.getEnergyConsumptions().stream())
                .mapToDouble(EnergyConsumption::getKwhConsumption)
                .sum();

        double totalCost = calculateTotalEnergyCostForCustomer(customer);

        if (totalKwhConsumption > 30) {
            this.registerAutomaticAlert(energyConsumption.getDevice(), totalKwhConsumption, totalCost);
        }

        energyConsumptionRepository.save(energyConsumption);

        redirectAttributes.addFlashAttribute("msg", "Consumo de energia atualizado com sucesso!");
        return "redirect:/energy-consumption/list";
    }

    @PostMapping("delete")
    @Transactional
    public String delete(Long idEnergyConsumption, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        EnergyConsumption energyConsumption = energyConsumptionRepository.findByIdAndDeviceCustomer(idEnergyConsumption, customer)
                .orElseThrow(() -> new IllegalArgumentException("Consumo de energia não encontrado!"));

        energyConsumptionRepository.delete(energyConsumption);
        redirectAttributes.addFlashAttribute("msg", "Consumo de energia excluído com sucesso!");
        return "redirect:/energy-consumption/list";
    }


    private void registerAutomaticAlert(Device device, Double totalKwhConsumption, Double totalCost) {
        AlertLevel alertLevel;

        if (totalKwhConsumption <= 100) {
            alertLevel = AlertLevel.LOW;
        } else if (totalKwhConsumption <= 220) {
            alertLevel = AlertLevel.MEDIUM;
        } else {
            alertLevel = AlertLevel.HIGH;
        }


        String message = String.format(
                "Consumo total de: %.2f kWh registrado. Custo total até o momento: R$ %.2f",
                totalKwhConsumption,
                totalCost
        );

        alertRepository.save(new Alert(message, false, alertLevel, device));
    }

    public double calculateTotalEnergyCostForCustomer(Customer customer) {
        double totalCost = 0.0;

        for (Device device : customer.getDevices()) {
            for (EnergyConsumption energyConsumption : device.getEnergyConsumptions()) {
                totalCost += energyConsumption.getEnergyCost();
            }
        }

        return totalCost;
    }
}
