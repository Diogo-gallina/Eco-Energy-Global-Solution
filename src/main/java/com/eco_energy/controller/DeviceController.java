package com.eco_energy.controller;

import com.eco_energy.dto.device.CreateDeviceDTO;
import com.eco_energy.dto.device.UpdateDeviceDTO;
import com.eco_energy.model.Customer;
import com.eco_energy.model.Device;
import com.eco_energy.model.enums.UsageFrequency;
import com.eco_energy.repository.AlertRepository;
import com.eco_energy.repository.CustomerRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping("register")
    public String register(CreateDeviceDTO deviceDTO, Model model) {
        model.addAttribute("deviceDTO", new CreateDeviceDTO("", 0, null));
        model.addAttribute("usageFrequencies", UsageFrequency.values());
        return "device/register";
    }

    @PostMapping("register")
    @Transactional
    public String register(CreateDeviceDTO deviceDTO, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        if(customer == null) new IllegalArgumentException("Cliente não encontrado!");
        assert customer != null;

        Device device = new Device(
                deviceDTO.name(),
                deviceDTO.electricalPower(),
                deviceDTO.usageFrequency(),
                customer
        );

        deviceRepository.save(device);
        redirectAttributes.addFlashAttribute("msg", "Dispositivo registrado com sucesso!");
        return "redirect:/device/list";
    }

    @GetMapping("list")
    public String list(Model model, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        List<Device> devices = deviceRepository.findByCustomer(customer);
        model.addAttribute("devices", devices);
        return "device/list";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        Device device = deviceRepository.findByIdAndCustomer(id, customer)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        UpdateDeviceDTO deviceDTO = new UpdateDeviceDTO(
                device.getId(),
                device.getName(),
                device.getElectricalPower(),
                device.getUsageFrequency()
        );

        model.addAttribute("deviceDTO", deviceDTO);
        model.addAttribute("usageFrequencies", UsageFrequency.values());
        model.addAttribute("customers", customerRepository.findAll());
        return "device/update";
    }

    @PostMapping("update")
    @Transactional
    public String update(UpdateDeviceDTO deviceDTO, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        Device device = deviceRepository.findByIdAndCustomer(deviceDTO.id(), customer)
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
    public String delete(Long idDevice, RedirectAttributes redirectAttributes, Authentication authentication) {
        Customer customer = userService.getAuthenticatedCustomer(authentication);
        Device device = deviceRepository.findByIdAndCustomer(idDevice, customer)
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        deviceRepository.delete(device);
        redirectAttributes.addFlashAttribute("msg", "Dispositivo excluído com sucesso!");
        return "redirect:/device/list";
    }

}
