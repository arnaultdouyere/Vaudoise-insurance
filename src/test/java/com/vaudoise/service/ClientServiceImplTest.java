package com.vaudoise.service;

import com.vaudoise.dto.client.ClientCreateRequest;
import com.vaudoise.dto.client.ClientResponse;
import com.vaudoise.dto.client.ClientUpdateRequest;
import com.vaudoise.model.Client;
import com.vaudoise.model.EClientType;
import com.vaudoise.repository.ClientRepository;
import com.vaudoise.repository.ContractRepository;
import com.vaudoise.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private ClientRepository clientRepo;
    private ContractRepository contractRepo;
    private ClientService service;

    @BeforeEach
    void setUp() {
        clientRepo = mock(ClientRepository.class);
        contractRepo = mock(ContractRepository.class);
        service = new ClientServiceImpl(clientRepo, contractRepo);
    }

    @Test
    void create_person_success() {
        ClientCreateRequest req = new ClientCreateRequest();
        req.setClientType(EClientType.PERSON);
        req.setName("Alice");
        req.setEmail("alice@ex.com");
        req.setPhone("+33 6 12 34 56 78");
        req.setBirthDate(LocalDate.of(1995,4,12));

        Client saved = new Client();
        saved.setId(1L);
        saved.setClientType(EClientType.PERSON);
        saved.setName("Alice");
        saved.setEmail("alice@ex.com");
        saved.setPhone("+33 6 12 34 56 78");
        saved.setBirthDate(LocalDate.of(1995,4,12));

        when(clientRepo.save(any())).thenReturn(saved);

        ClientResponse res = service.create(req);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepo).save(captor.capture());

        assertThat(captor.getValue().getClientType()).isEqualTo(EClientType.PERSON);
        assertThat(captor.getValue().getBirthDate()).isEqualTo(LocalDate.of(1995,4,12));
        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getName()).isEqualTo("Alice");
    }

    @Test
    void update_only_mutable_fields() {
        Client existing = new Client();
        existing.setId(10L);
        existing.setClientType(EClientType.COMPANY);
        existing.setName("Old");
        existing.setEmail("old@ex.com");
        existing.setPhone("000");
        existing.setCompanyIdentifier("aaa-123");

        when(clientRepo.findById(10L)).thenReturn(java.util.Optional.of(existing));

        ClientUpdateRequest req = new ClientUpdateRequest();
        req.setName("New");
        req.setEmail("new@ex.com");
        req.setPhone("111");

        ClientResponse res = service.update(10L, req);

        assertThat(existing.getName()).isEqualTo("New");
        assertThat(existing.getEmail()).isEqualTo("new@ex.com");
        assertThat(existing.getPhone()).isEqualTo("111");
        // immutables non modifiés
        assertThat(existing.getCompanyIdentifier()).isEqualTo("aaa-123");
        verify(clientRepo).findById(10L);
    }

    @Test
    void delete_closes_active_contracts_then_deletes_client() {
        Client existing = new Client();
        existing.setId(5L);
        when(clientRepo.findById(5L)).thenReturn(java.util.Optional.of(existing));

        service.delete(5L);

        verify(contractRepo).closeAllActiveForClient(eq(5L), any(LocalDate.class));
        verify(clientRepo).delete(existing);
    }
}
