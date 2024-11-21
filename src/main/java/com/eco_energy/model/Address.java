package com.eco_energy.model;

import com.eco_energy.model.enums.BrazilianStates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "JV_GS2_MVC_ADDRESS")
@EntityListeners(AuditingEntityListener.class)
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "address_number", nullable = false, length = 99999)
    private Integer number;

    @Column(name = "cep", nullable = false, length = 8)
    private String postalCode;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Address(String street, Integer number, String postalCode, Customer customer) {
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.createdAt = LocalDateTime.now();
        this.customer = customer;
        customer.getAddresses().add(this);
    }

}
