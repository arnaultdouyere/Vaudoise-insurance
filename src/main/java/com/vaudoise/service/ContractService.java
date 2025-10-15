package com.vaudoise.service;

import com.vaudoise.dto.contract.ContractCreateRequest;
import com.vaudoise.dto.contract.ContractResponse;
import com.vaudoise.dto.contract.ContractUpdateAmountRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface ContractService {
    ContractResponse create(Long clientId, ContractCreateRequest req);
    ContractResponse updateAmount(Long contractId, ContractUpdateAmountRequest req);
    List<ContractResponse> listActive(Long clientId, OffsetDateTime updatedAfter, OffsetDateTime updatedBefore);
    BigDecimal sumActive(Long clientId);
}
