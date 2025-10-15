package com.vaudoise.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String companyIdentifier;
}