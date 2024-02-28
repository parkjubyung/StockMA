package com.kkongdak.authentication.service;


import com.kkongdak.authentication.domain.Account;
import com.kkongdak.authentication.dto.AddAccountRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AccountService extends UserDetailsService {

    @Override
    Account loadUserByUsername(String email) throws UsernameNotFoundException;

    Long save(AddAccountRequest request);
}
