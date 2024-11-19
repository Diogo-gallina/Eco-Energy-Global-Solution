package com.eco_energy.controller;

import com.eco_energy.dto.address.CreateAddressDTO;
import com.eco_energy.dto.address.UpdateAddressDTO;
import com.eco_energy.model.Address;
import com.eco_energy.model.Customer;
import com.eco_energy.repository.AddressRepository;
import com.eco_energy.repository.CustomerRepository;
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
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("register")
    public String register(CreateAddressDTO addressDTO, Model model){
        model.addAttribute("addressDTO", new CreateAddressDTO("", null, "", null));
        return "address/register";
    }

    @PostMapping("register")
    @Transactional
    public String register(CreateAddressDTO addressDTO, RedirectAttributes redirectAttributes){
        Customer customer = customerRepository.findById(addressDTO.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado!"));

        Address address = new Address(
                addressDTO.street(),
                addressDTO.number(),
                addressDTO.postalCode(),
                customer
        );

        addressRepository.save(address);
        redirectAttributes.addFlashAttribute("msg", "Endereço registrado com sucesso!");
        return "redirect:/address/register";
    }

    @GetMapping("list")
    public String list(Model model) {
        List<Address> addresses = addressRepository.findAll();
        model.addAttribute("addresses", addresses);
        return "address/list";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado!"));

        UpdateAddressDTO addressDTO = new UpdateAddressDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getPostalCode(),
                address.getCustomer().getId()
        );

        model.addAttribute("addressDTO", addressDTO);
        model.addAttribute("customers", customerRepository.findAll());
        return "address/update";
    }

    @PostMapping("update")
    @Transactional
    public String update(UpdateAddressDTO addressDTO, RedirectAttributes redirectAttributes) {
        Address address = addressRepository.findById(addressDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado!"));

        address.setStreet(addressDTO.street());
        address.setNumber(addressDTO.number());
        address.setPostalCode(addressDTO.postalCode());

        Customer customer = customerRepository.findById(addressDTO.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado!"));
        address.setCustomer(customer);

        address.setUpdatedAt(LocalDateTime.now());

        addressRepository.save(address);

        redirectAttributes.addFlashAttribute("msg", "Endereço atualizado com sucesso!");
        return "redirect:/address/list";
    }

    @PostMapping("delete")
    @Transactional
    public String delete(Long idAddress, RedirectAttributes redirectAttributes) {
        addressRepository.deleteById(idAddress);
        redirectAttributes.addFlashAttribute("msg", "Endereço excluído com sucesso!");
        return "redirect:/address/list";
    }

}
