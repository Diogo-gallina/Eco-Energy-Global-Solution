package com.eco_energy.controller;

import com.eco_energy.dto.energyConsumption.CreateEnergyConsumptionDTO;
import com.eco_energy.dto.energyConsumption.UpdateEnergyConsumptionDTO;
import com.eco_energy.model.Customer;
import com.eco_energy.model.Device;
import com.eco_energy.model.EnergyConsumption;
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
    DeviceRepository deviceRepository;
    @Autowired
    private UserService userService;

    @GetMapping("register")
    public String register(CreateEnergyConsumptionDTO energyConsumptionDTO, Model model, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        model.addAttribute("energyConsumptionDTO", new CreateEnergyConsumptionDTO(null, null, null, null));
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
                energyConsumptionDTO.kwhConsumption(),
                energyConsumptionDTO.energyCost(),
                device
        );

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
                energyConsumption.getKwhConsumption(),
                energyConsumption.getEnergyCost(),
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
        energyConsumption.setKwhConsumption(energyConsumptionDTO.kwhConsumption());
        energyConsumption.setEnergyCost(energyConsumptionDTO.energyCost());
        energyConsumption.setDevice(device);

        energyConsumptionRepository.save(energyConsumption);

        redirectAttributes.addFlashAttribute("msg", "Consumo de energia atualizado com sucesso!");
        return "redirect:/energy-consumption/list";
    }

    @PostMapping("delete")
    @Transactional
    public String delete(Long idEnergyConsumption, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        EnergyConsumption energyConsumption = energyConsumptionRepository.findByIdAndDeviceCustomer(idEnergyConsumption, customer)
                .orElseThrow(() -> new IllegalArgumentException("Consumo de energia não encontrado para o cliente autenticado!"));

        energyConsumptionRepository.delete(energyConsumption);
        redirectAttributes.addFlashAttribute("msg", "Consumo de energia excluído com sucesso!");
        return "redirect:/energy-consumption/list";
    }

}
