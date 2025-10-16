package com.vaudoise.controller;

import com.vaudoise.dto.client.ClientCreateRequest;
import com.vaudoise.dto.client.ClientResponse;
import com.vaudoise.dto.client.ClientUpdateRequest;
import com.vaudoise.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody ClientCreateRequest req) {
        var res = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}")
    public ClientResponse get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable Long id, @RequestBody ClientUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
