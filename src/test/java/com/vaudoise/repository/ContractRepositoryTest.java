package com.vaudoise.repository;

import com.vaudoise.model.Client;
import com.vaudoise.model.Contract;
import com.vaudoise.model.EClientType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContractRepositoryTest {

    @Autowired ClientRepository clientRepo;
    @Autowired ContractRepository contractRepo;

    @Test
    void active_query_and_sum_work() {
        Client p = new Client();
        p.setClientType(EClientType.PERSON);
        p.setName("Alice");
        p.setEmail("a@ex.com");
        p.setPhone("0000000");
        p.setBirthDate(LocalDate.of(1990,1,1));
        p = clientRepo.save(p);

        Contract c1 = new Contract();
        c1.setClient(p);
        c1.setStartDate(LocalDate.now().minusDays(1));
        c1.setEndDate(LocalDate.now().plusDays(10));
        c1.setCostAmount(new BigDecimal("100.00"));
        contractRepo.save(c1);

        Contract c2 = new Contract();
        c2.setClient(p);
        c2.setStartDate(LocalDate.now().minusDays(1));
        c2.setEndDate(LocalDate.now().plusDays(1));
        c2.setCostAmount(new BigDecimal("50.50"));
        contractRepo.save(c2);

        Contract c3 = new Contract(); // expiré => non actif
        c3.setClient(p);
        c3.setStartDate(LocalDate.now().minusDays(5));
        c3.setEndDate(LocalDate.now().minusDays(1));
        c3.setCostAmount(new BigDecimal("999.99"));
        contractRepo.save(c3);

        List<Contract> active = contractRepo.findActiveByClientAndUpdatedBetween(
                p.getId(), LocalDate.now(),
                OffsetDateTime.now().minusYears(1), OffsetDateTime.now().plusYears(1));

        assertThat(active).hasSize(2);
        assertThat(contractRepo.sumActiveCostByClient(p.getId(), LocalDate.now()))
                .isEqualByComparingTo(new BigDecimal("150.50"));
    }
}
