package com.eco_energy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter

@Entity
@Table(name = "JV_GS2_MVC_CUSTOMER")
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "email", nullable = false, length = 70, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 70)
    private String password;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "JV_GS2_MVC_CUSTOMER_ROLE",
            joinColumns = @JoinColumn(name="customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private Set<Role> roles;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Device> devices;

    public Customer(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

}
