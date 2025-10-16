package com.vaudoise.controller;

import com.vaudoise.dto.client.ClientCreateRequest;
import com.vaudoise.dto.client.ClientResponse;
import com.vaudoise.model.EClientType;
import com.vaudoise.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired MockMvc mvc;
    @MockBean
    ClientService service;

    @Test
    void getAll_returns_list_200() throws Exception {
        ClientResponse r = new ClientResponse();
        r.setId(1L);
        r.setName("Alice");
        r.setType(EClientType.PERSON);

        when(service.getAll()).thenReturn(List.of(r));

        mvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Alice")));
    }

    @Test
    void create_returns_201() throws Exception {
        ClientResponse out = new ClientResponse();
        out.setId(10L);
        out.setName("Alice");
        out.setType(EClientType.PERSON);

        when(service.create(any(ClientCreateRequest.class))).thenReturn(out);

        String body = """
      {"type":"PERSON","name":"Alice","email":"alice@ex.com","phone":"+33 6 12 34 56 78","birthDate":"1995-04-12"}
      """;

        mvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"id\":10")));
    }

    @Test
    void get_by_id_200() throws Exception {
        ClientResponse out = new ClientResponse();
        out.setId(42L);
        out.setName("Bob");
        out.setType(EClientType.COMPANY);

        when(service.get(42L)).thenReturn(out);

        mvc.perform(get("/api/clients/42"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":42")));
    }
}
