package com.vaudoise.dto.client;

import com.vaudoise.model.EClientType;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreateRequest {

    private EClientType clientType;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String companyIdentifier;
}
