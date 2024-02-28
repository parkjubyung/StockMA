package com.kkongdak.authentication.service;

import com.kkongdak.authentication.domain.Account;
import com.kkongdak.authentication.dto.AddAccountRequest;
import com.kkongdak.authentication.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Account loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public Long save(AddAccountRequest request) {
        int passwordExpiredDays = 90;
        return repository.save(
                Account.builder()
                        .email(request.getEmail())
                        .password(bCryptPasswordEncoder.encode(request.getPassword()))
                        .passwordExpiryDays(passwordExpiredDays)
                        .build()).getId();
    }
}
