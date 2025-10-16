package com.vaudoise.controller;

import com.vaudoise.dto.contract.ContractResponse;
import com.vaudoise.service.ContractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContractController.class)
class ContractControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ContractService service;

    @Test
    void sum_active_by_client_ok() throws Exception {
        when(service.sumActive(2L)).thenReturn(new BigDecimal("150.75"));

        mvc.perform(get("/api/clients/2/contracts/active/sum"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.75"));
    }

    @Test
    void list_active_by_client_ok() throws Exception {
        ContractResponse resp = new ContractResponse();
        resp.setId(1L); resp.setClientId(2L);

        when(service.listActive(2L, null, null)).thenReturn(List.of(resp));

        mvc.perform(get("/api/clients/2/contracts"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")));
    }

    @Test
    void patch_amount_ok() throws Exception {
        ContractResponse resp = new ContractResponse(); resp.setId(9L);
        when(service.updateAmount(org.mockito.ArgumentMatchers.eq(9L), org.mockito.ArgumentMatchers.any()))
                .thenReturn(resp);

        mvc.perform(patch("/api/contracts/9/amount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"costAmount\":200.00}"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("\"id\":9")));
    }
}
