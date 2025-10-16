package com.vaudoise.service;

import com.vaudoise.dto.contract.ContractCreateRequest;
import com.vaudoise.dto.contract.ContractResponse;
import com.vaudoise.dto.contract.ContractUpdateAmountRequest;
import com.vaudoise.model.Client;
import com.vaudoise.model.Contract;
import com.vaudoise.repository.ClientRepository;
import com.vaudoise.repository.ContractRepository;
import com.vaudoise.service.impl.ContractServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ContractServiceImplTest {

    private ContractRepository contractRepo;
    private ClientRepository clientRepo;
    private ContractService service;

    @BeforeEach
    void setUp() {
        contractRepo = mock(ContractRepository.class);
        clientRepo   = mock(ClientRepository.class);
        service = new ContractServiceImpl(contractRepo, clientRepo);
    }

    @Test
    void create_sets_today_when_startDate_is_null() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));

        Contract saved = new Contract();
        saved.setId(99L);
        saved.setClient(client);
        saved.setStartDate(LocalDate.now());
        saved.setCostAmount(new BigDecimal("100.00"));

        when(contractRepo.save(any(Contract.class))).thenReturn(saved);

        ContractCreateRequest req = new ContractCreateRequest();
        req.setCostAmount(new BigDecimal("100.00"));
        // startDate null → doit être "today" côté service

        ContractResponse res = service.create(1L, req);

        verify(contractRepo).save(argThat(c ->
                c.getClient().getId().equals(1L) &&
                        LocalDate.now().equals(c.getStartDate()) &&
                        new BigDecimal("100.00").compareTo(c.getCostAmount()) == 0
        ));
        assertThat(res.getId()).isEqualTo(99L);
    }

    @Test
    void updateAmount_updates_amount_and_updateDate() {
        Contract c = new Contract();
        c.setId(7L);
        c.setStartDate(LocalDate.now());
        c.setCostAmount(new BigDecimal("50.00"));

        Client owner = new Client();
        owner.setId(123L);
        c.setClient(owner);

        when(contractRepo.findById(7L)).thenReturn(Optional.of(c));
        when(contractRepo.save(any(Contract.class))).thenAnswer(inv -> inv.getArgument(0));

        ContractUpdateAmountRequest req = new ContractUpdateAmountRequest();
        req.setCostAmount(new BigDecimal("120.00"));

        ContractResponse res = service.updateAmount(7L, req);

        assertThat(c.getCostAmount()).isEqualByComparingTo("120.00");
        assertThat(c.getUpdateDate()).isNotNull();
        assertThat(res.getId()).isEqualTo(7L);
    }

    @Test
    void listActive_calls_repo_with_filters() {
        when(contractRepo.findActiveByClientAndUpdatedBetween(
                eq(2L), any(LocalDate.class), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(List.of());

        service.listActive(2L, OffsetDateTime.now().minusDays(1), OffsetDateTime.now());
        verify(contractRepo).findActiveByClientAndUpdatedBetween(eq(2L), any(LocalDate.class), any(), any());
    }

    @Test
    void sumActive_delegates_to_repo() {
        when(contractRepo.sumActiveCostByClient(eq(3L), any(LocalDate.class)))
                .thenReturn(new BigDecimal("321.45"));

        assertThat(service.sumActive(3L)).isEqualTo(new BigDecimal("321.45"));
    }
}
