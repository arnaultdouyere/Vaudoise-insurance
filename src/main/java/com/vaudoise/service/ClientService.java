package com.vaudoise.service;

import com.vaudoise.dto.client.ClientCreateRequest;
import com.vaudoise.dto.client.ClientResponse;
import com.vaudoise.dto.client.ClientUpdateRequest;

import java.util.List;

public interface ClientService {
    ClientResponse create(ClientCreateRequest req);
    ClientResponse get(Long id);
    List<ClientResponse> getAll();
    ClientResponse update(Long id, ClientUpdateRequest req);
    void delete(Long id);
}
