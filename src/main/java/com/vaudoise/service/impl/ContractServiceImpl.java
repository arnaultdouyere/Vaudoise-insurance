package com.vaudoise.service.impl;

import com.vaudoise.dto.client.ClientResponse;
import com.vaudoise.dto.contract.ContractCreateRequest;
import com.vaudoise.dto.contract.ContractResponse;
import com.vaudoise.dto.contract.ContractUpdateAmountRequest;
import com.vaudoise.model.Client;
import com.vaudoise.model.Contract;
import com.vaudoise.repository.ClientRepository;
import com.vaudoise.repository.ContractRepository;
import com.vaudoise.service.ContractService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepo;
    private final ClientRepository clientRepo;

    public ContractServiceImpl(ContractRepository contractRepo, ClientRepository clientRepo) {
        this.contractRepo = contractRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    @Transactional
    public ContractResponse create(Long clientId, ContractCreateRequest req) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("client not found"));
        Contract contract = new Contract();
        contract.setClient(client);
        contract.setStartDate(req.getStartDate() != null ? req.getStartDate() : LocalDate.now());
        contract.setEndDate(req.getEndDate());
        contract.setCostAmount(req.getCostAmount());
        contract = contractRepo.save(contract);
        return toResponse(contract);
    }

    @Override
    @Transactional
    public ContractResponse updateAmount(Long contractId, ContractUpdateAmountRequest req) {
        Contract contract = contractRepo.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("contract not found"));
        contract.setCostAmount(req.getCostAmount());
        contract.setUpdateDate(OffsetDateTime.now());
        return toResponse(contract);
    }

    @Override
    @Transactional
    public List<ContractResponse> listActive(Long clientId, OffsetDateTime updatedAfter, OffsetDateTime updatedBefore) {
        return contractRepo
                .findActiveByClientAndUpdatedBetween(clientId, LocalDate.now(), updatedAfter, updatedBefore)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public BigDecimal sumActive(Long clientId) {
        return contractRepo.sumActiveCostByClient(clientId, LocalDate.now());
    }

    private ContractResponse toResponse(Contract contract) {
        ContractResponse request = new ContractResponse();
        request.setId(contract.getId());
        request.setClientId(contract.getClient().getId());
        request.setStartDate(contract.getStartDate());
        request.setEndDate(contract.getEndDate());
        request.setCostAmount(contract.getCostAmount());
        return request;
    }
}
