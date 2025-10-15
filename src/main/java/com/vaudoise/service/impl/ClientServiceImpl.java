package com.vaudoise.service.impl;

import com.vaudoise.dto.client.ClientCreateRequest;
import com.vaudoise.dto.client.ClientResponse;
import com.vaudoise.dto.client.ClientUpdateRequest;
import com.vaudoise.model.Client;
import com.vaudoise.model.EClientType;
import com.vaudoise.repository.ClientRepository;
import com.vaudoise.repository.ContractRepository;
import com.vaudoise.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepo;
    private final ContractRepository contractRepo;

    public ClientServiceImpl(ClientRepository clientRepo, ContractRepository contractRepo) {
        this.clientRepo = clientRepo;
        this.contractRepo = contractRepo;
    }

    @Override
    @Transactional
    public ClientResponse create(ClientCreateRequest req) {
        Client client = new Client();
        client.setClientType(req.getClientType());
        client.setName(req.getName());
        client.setEmail(req.getEmail());
        client.setPhone(req.getPhone());
        if (req.getClientType() == EClientType.PERSON) client.setBirthDate(req.getBirthDate());
        else client.setCompanyIdentifier(req.getCompanyIdentifier());
        client = clientRepo.save(client);
        return toResponse(client);
    }

    @Override
    @Transactional
    public ClientResponse get(Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("client not found"));
        return toResponse(client);
    }

    @Override
    @Transactional
    public List<ClientResponse> getAll() {
        return clientRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ClientResponse update(Long id, ClientUpdateRequest req) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("client not found"));
        client.setName(req.getName());
        client.setEmail(req.getEmail());
        client.setPhone(req.getPhone());
        return toResponse(client);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client c = clientRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("client not found"));
        contractRepo.closeAllActiveForClient(c.getId(), LocalDate.now());
        clientRepo.delete(c);
    }

    private ClientResponse toResponse(Client client) {
        ClientResponse request = new ClientResponse();
        request.setId(client.getId());
        request.setType(client.getClientType());
        request.setName(client.getName());
        request.setEmail(client.getEmail());
        request.setPhone(client.getPhone());
        request.setBirthDate(client.getBirthDate());
        request.setCompanyIdentifier(client.getCompanyIdentifier());
        request.setCreatedAt(client.getCreatedAt());
        request.setUpdatedAt(client.getUpdatedAt());
        return request;
    }
}

