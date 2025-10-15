package com.vaudoise.dto.client;

import com.vaudoise.model.EClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {
    private Long id;
    private EClientType type;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String companyIdentifier;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
