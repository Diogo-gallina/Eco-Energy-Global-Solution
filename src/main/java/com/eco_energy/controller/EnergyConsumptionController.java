package com.eco_energy.controller;

import com.eco_energy.dto.energyConsumption.CreateEnergyConsumptionDTO;
import com.eco_energy.dto.energyConsumption.UpdateEnergyConsumptionDTO;
import com.eco_energy.model.Device;
import com.eco_energy.model.EnergyConsumption;
import com.eco_energy.repository.DeviceRepository;
import com.eco_energy.repository.EnergyConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("register")
    public String register(CreateEnergyConsumptionDTO energyConsumptionDTO, Model model) {
        model.addAttribute("energyConsumptionDTO", new CreateEnergyConsumptionDTO(null, null, null, null));
        model.addAttribute("devices", deviceRepository.findAll());
        return "energy-consumption/register";
    }

    @PostMapping("register")
    @Transactional
    public String register(CreateEnergyConsumptionDTO energyConsumptionDTO, RedirectAttributes redirectAttributes) {
        Device device = deviceRepository.findById(energyConsumptionDTO.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

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
    public String list(Model model) {
        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository.findAll();
        model.addAttribute("energyConsumptions", energyConsumptions);
        return "energy-consumption/list";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        EnergyConsumption energyConsumption = energyConsumptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consumo de energia não encontrado!"));

        UpdateEnergyConsumptionDTO energyConsumptionDTO = new UpdateEnergyConsumptionDTO(
                energyConsumption.getId(),
                energyConsumption.getUsageTime(),
                energyConsumption.getKwhConsumption(),
                energyConsumption.getEnergyCost(),
                energyConsumption.getDevice().getId()
        );

        model.addAttribute("energyConsumptionDTO", energyConsumptionDTO);
        model.addAttribute("devices", deviceRepository.findAll());
        return "energy-consumption/update";
    }

    @PostMapping("update")
    @Transactional
    public String update(UpdateEnergyConsumptionDTO energyConsumptionDTO, RedirectAttributes redirectAttributes) {
        EnergyConsumption energyConsumption = energyConsumptionRepository.findById(energyConsumptionDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Consumo de energia não encontrado!"));

        Device device = deviceRepository.findById(energyConsumptionDTO.deviceId())
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
    public String delete(Long idEnergyConsumption, RedirectAttributes redirectAttributes) {
        energyConsumptionRepository.deleteById(idEnergyConsumption);
        redirectAttributes.addFlashAttribute("msg", "Consumo de energia excluído com sucesso!");
        return "redirect:/energy-consumption/list";
    }

}
