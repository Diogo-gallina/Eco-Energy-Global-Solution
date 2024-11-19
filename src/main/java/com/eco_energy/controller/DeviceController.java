package com.eco_energy.controller;

import com.eco_energy.dto.device.CreateDeviceDTO;
import com.eco_energy.dto.device.UpdateDeviceDTO;
import com.eco_energy.model.Customer;
import com.eco_energy.model.Device;
import com.eco_energy.repository.AlertRepository;
import com.eco_energy.repository.CustomerRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EnergyConsumptionRepository energyConsumptionRepository;
    @Autowired
    private AlertRepository alertRepository;


    @GetMapping("register")
    public String register(CreateDeviceDTO deviceDTO, Model model) {
        model.addAttribute("deviceDTO", new CreateDeviceDTO("", 0, null, null));
        model.addAttribute("customers", customerRepository.findAll());
        return "device/register";
    }

    @PostMapping("register")
    @Transactional
    public String register(CreateDeviceDTO deviceDTO, RedirectAttributes redirectAttributes) {
        Customer customer = customerRepository.findById(deviceDTO.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado!"));

        Device device = new Device(
                deviceDTO.name(),
                deviceDTO.electricalPower(),
                deviceDTO.usageFrequency(),
                customer
        );

        deviceRepository.save(device);
        redirectAttributes.addFlashAttribute("msg", "Dispositivo registrado com sucesso!");
        return "redirect:/device/register";
    }

    @GetMapping("list")
    public String list(Model model) {
        List<Device> devices = deviceRepository.findAll();
        model.addAttribute("devices", devices);
        return "device/list";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        UpdateDeviceDTO deviceDTO = new UpdateDeviceDTO(
                device.getId(),
                device.getName(),
                device.getElectricalPower(),
                device.getUsageFrequency()
        );

        model.addAttribute("deviceDTO", deviceDTO);
        model.addAttribute("customers", customerRepository.findAll());
        return "device/update";
    }

    @PostMapping("update")
    @Transactional
    public String update(UpdateDeviceDTO deviceDTO, RedirectAttributes redirectAttributes) {
        Device device = deviceRepository.findById(deviceDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        device.setName(deviceDTO.name());
        device.setElectricalPower(deviceDTO.electricalPower());
        device.setUsageFrequency(deviceDTO.usageFrequency());


        device.setUpdatedAt(LocalDateTime.now());
        deviceRepository.save(device);

        redirectAttributes.addFlashAttribute("msg", "Dispositivo atualizado com sucesso!");
        return "redirect:/device/list";
    }

    @PostMapping("delete")
    @Transactional
    public String delete(Long idDevice, RedirectAttributes redirectAttributes) {
        deviceRepository.deleteById(idDevice);
        redirectAttributes.addFlashAttribute("msg", "Dispositivo excluído com sucesso!");
        return "redirect:/device/list";
    }

}
