package com.vaudoise.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractUpdateAmountRequest {
    private BigDecimal costAmount;
}
