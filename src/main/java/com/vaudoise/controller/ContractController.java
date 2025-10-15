package com.vaudoise.controller;

import com.vaudoise.dto.contract.ContractCreateRequest;
import com.vaudoise.dto.contract.ContractResponse;
import com.vaudoise.dto.contract.ContractUpdateAmountRequest;
import com.vaudoise.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContractController {
    private final ContractService service;
    public ContractController(ContractService service) { this.service = service; }

    @PostMapping("/clients/{clientId}/contracts")
    public ResponseEntity<ContractResponse> create(
            @PathVariable Long clientId, @RequestBody ContractCreateRequest req) {
        var res = service.create(clientId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping("/contracts/{contractId}/amount")
    public ContractResponse updateAmount(
            @PathVariable Long contractId, @RequestBody ContractUpdateAmountRequest req) {
        return service.updateAmount(contractId, req);
    }

    @GetMapping("/clients/{clientId}/contracts")
    public List<ContractResponse> list(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "true") boolean active,
            @RequestParam(required = false) OffsetDateTime updatedAfter,
            @RequestParam(required = false) OffsetDateTime updatedBefore) {
        if (!active) throw new IllegalArgumentException("Only active=true is supported by spec");
        return service.listActive(clientId, updatedAfter, updatedBefore);
    }

    @GetMapping("/clients/{clientId}/contracts/active/sum")
    public BigDecimal sum(@PathVariable Long clientId) {
        return service.sumActive(clientId);
    }
}
