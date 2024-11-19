package com.eco_energy.model;

import com.eco_energy.model.enums.AlertLevel;
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
@Table(name = "JV_GS2_MVC_ALERT")
@EntityListeners(AuditingEntityListener.class)
public class Alert {

    @Id
    @GeneratedValue
    @Column(name = "alert_id")
    private Long id;

    @Column(name = "message", nullable = false, length = 100)
    private String message;

    @Column(name = "was_resolved", nullable = false)
    private Boolean wasResolved;

    @Column(name = "alert_level", nullable = false)
    private AlertLevel alertLevel;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    public Alert(String message, Boolean wasResolved, AlertLevel alertLevel, Device device) {
        this.message = message;
        this.wasResolved = wasResolved;
        this.alertLevel = alertLevel;
        this.device = device;
        device.getAlerts().add(this);
    }
}
