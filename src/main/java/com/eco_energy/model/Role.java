package com.eco_energy.model;

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
@Getter @Setter

@Entity
@Table(name = "JV_GS2_MVC_ROLE")
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "nm_role", nullable = false, unique = true)
    private String name;

    @Column(name = "ds_label", nullable = false)
    private String label;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Role(String name, String user) {
        this.name = name;
        this.label = user;
        this.createdAt = LocalDateTime.now();
    }
}
