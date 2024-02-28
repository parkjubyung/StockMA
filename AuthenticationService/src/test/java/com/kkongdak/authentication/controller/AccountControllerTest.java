package com.kkongdak.authentication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkongdak.authentication.domain.Account;
import com.kkongdak.authentication.dto.AddAccountRequest;
import com.kkongdak.authentication.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        accountRepository.deleteAll();
    }

    @Test
    public void signUp() throws Exception {
        final String url = "/api/account";
        final String email = "aabb@naver.com";
        final String password = "12345678";

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        final AddAccountRequest request = new AddAccountRequest(email, password);
        final String requestBody = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Account> accountList = accountRepository.findAll();

        assertThat(accountList.size()).isEqualTo(1);
        assertThat(accountList.get(0).getEmail()).isEqualTo(email);
        assertThat(bCryptPasswordEncoder.matches(password, accountList.get(0).getPassword()));
    }

}
