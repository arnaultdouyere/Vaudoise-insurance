package com.vaudoise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "contract")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="client_id")
    private Client client;

    @Column(nullable=false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable=false)
    private BigDecimal costAmount;

    @Column(nullable=false)
    private OffsetDateTime updateDate = OffsetDateTime.now();

    @PrePersist @PreUpdate
    void touchUpdateDate() {
        if (updateDate == null) updateDate = OffsetDateTime.now();
    }
}
