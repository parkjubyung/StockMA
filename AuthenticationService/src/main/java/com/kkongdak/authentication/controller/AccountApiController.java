package com.kkongdak.authentication.controller;

import com.kkongdak.authentication.dto.AddAccountRequest;
import com.kkongdak.authentication.service.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AccountApiController {

    private final AccountServiceImpl accountService;

    @PostMapping("/account")
    public ResponseEntity<Long> signup(@Valid @RequestBody AddAccountRequest accountRequest,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long id = accountService.save(accountRequest);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }


}
