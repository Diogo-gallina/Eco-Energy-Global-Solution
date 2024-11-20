package com.eco_energy.controller;

import com.eco_energy.dto.alert.CreateAlertDTO;
import com.eco_energy.dto.alert.UpdateAlertDTO;
import com.eco_energy.model.Alert;
import com.eco_energy.model.Device;
import com.eco_energy.model.enums.AlertLevel;
import com.eco_energy.repository.AlertRepository;
import com.eco_energy.repository.DeviceRepository;
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
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    AlertRepository alertRepository;
    @Autowired
    DeviceRepository deviceRepository;

    @GetMapping("register")
    public String register(CreateAlertDTO alertDTO, Model model) {
        model.addAttribute("alertDTO", new CreateAlertDTO("", false, null, null));
        model.addAttribute("alertLevel", AlertLevel.values());
        model.addAttribute("devices", deviceRepository.findAll());
        return "alert/register";
    }

    @PostMapping("register")
    @Transactional
    public String register(CreateAlertDTO alertDTO, RedirectAttributes redirectAttributes) {
        Device device = deviceRepository.findById(alertDTO.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        Alert alert = new Alert(
                alertDTO.message(),
                alertDTO.wasResolved(),
                alertDTO.alertLevel(),
                device
        );

        alertRepository.save(alert);
        redirectAttributes.addFlashAttribute("msg", "Alerta registrado com sucesso!");
        return "redirect:/alert/list";
    }

    @GetMapping("list")
    public String list(Model model) {
        List<Alert> alerts = alertRepository.findAll();
        model.addAttribute("alerts", alerts);
        return "alert/list";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado!"));

        UpdateAlertDTO alertDTO = new UpdateAlertDTO(
                alert.getId(),
                alert.getMessage(),
                alert.getWasResolved(),
                alert.getAlertLevel(),
                alert.getDevice().getId()
        );

        model.addAttribute("alertDTO", alertDTO);
        model.addAttribute("alertLevel", AlertLevel.values());
        model.addAttribute("devices", deviceRepository.findAll());
        return "alert/update";
    }

    @PostMapping("update")
    @Transactional
    public String update(UpdateAlertDTO alertDTO, RedirectAttributes redirectAttributes) {
        Alert alert = alertRepository.findById(alertDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado!"));

        Device device = deviceRepository.findById(alertDTO.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Dispositivo não encontrado!"));

        alert.setMessage(alertDTO.message());
        alert.setWasResolved(alertDTO.wasResolved());
        alert.setAlertLevel(alertDTO.alertLevel());
        alert.setDevice(device);

        alertRepository.save(alert);

        redirectAttributes.addFlashAttribute("msg", "Alerta atualizado com sucesso!");
        return "redirect:/alert/list";
    }

    @PostMapping("delete")
    @Transactional
    public String delete(Long idAlert, RedirectAttributes redirectAttributes) {
        alertRepository.deleteById(idAlert);
        redirectAttributes.addFlashAttribute("msg", "Alerta excluído com sucesso!");
        return "redirect:/alert/list";
    }

}
