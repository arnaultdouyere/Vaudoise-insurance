package com.vaudoise.repository;

import com.vaudoise.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("""
    select c from Contract c
    where c.client.id = :clientId
      and (c.endDate is null or c.endDate > :today)
      and (:updatedAfter is null or c.updateDate >= :updatedAfter)
      and (:updatedBefore is null or c.updateDate < :updatedBefore)
    """)
    List<Contract> findActiveByClientAndUpdatedBetween(
            @Param("clientId") Long clientId,
            @Param("today") LocalDate today,
            @Param("updatedAfter") OffsetDateTime updatedAfter,
            @Param("updatedBefore") OffsetDateTime updatedBefore);

    @Query("""
    select coalesce(sum(c.costAmount), 0) from Contract c
    where c.client.id = :clientId
      and (c.endDate is null or c.endDate > :today)
    """)
    BigDecimal sumActiveCostByClient(@Param("clientId") Long clientId, @Param("today") LocalDate today);

    @Modifying
    @Query("update Contract c set c.endDate = :today where c.client.id = :clientId and (c.endDate is null or c.endDate > :today)")
    int closeAllActiveForClient(@Param("clientId") Long clientId, @Param("today") LocalDate today);
}
