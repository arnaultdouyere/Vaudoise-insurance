package com.vaudoise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppIntegrationTest {

    @Autowired MockMvc mvc;

    @Test
    void person_flow_create_contract_then_sum() throws Exception {
        // 1) Create CLIENT (PERSON)
        String clientBody = """
      {"type":"PERSON","name":"Alice","email":"alice@ex.com","phone":"+33 6 12 34 56 78","birthDate":"1995-04-12"}
      """;
        String clientJson = mvc.perform(post("/api/clients")
                        .contentType(APPLICATION_JSON)
                        .content(clientBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long clientId = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(clientJson).get("id").asLong();

        // 2) Create CONTRACT for this client
        String contractBody = """
      {"costAmount":120.50,"endDate":"2099-12-31"}
      """;
        mvc.perform(post("/api/clients/" + clientId + "/contracts")
                        .contentType(APPLICATION_JSON)
                        .content(contractBody))
                .andExpect(status().isCreated());

        // 3) SUM active contracts
        mvc.perform(get("/api/clients/" + clientId + "/contracts/active/sum"))
                .andExpect(status().isOk())
                .andExpect(content().string("120.50"));
    }
}
