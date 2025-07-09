package ru.dzhenbaz.P2PTransactionsTask.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzhenbaz.P2PTransactionsTask.dto.CreateAccountRequest;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;
import ru.dzhenbaz.P2PTransactionsTask.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    public AccountController(AccountService accountService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.validateTokenAndRetrieveClaim(token);
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody @Valid CreateAccountRequest request) {
        Long userId = extractUserId(authHeader);
        accountService.createAccount(userId, request.getInitialBalance());
        return ResponseEntity.ok("Счёт создан");
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(accountService.getAllForUser(userId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable Long accountId) {
        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(accountService.getAccountDtoById(userId, accountId));
    }

    @PostMapping("/{accountId}/close")
    public ResponseEntity<?> closeAccount(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable Long accountId) {
        Long userId = extractUserId(authHeader);
        accountService.closeAccount(userId, accountId);
        return ResponseEntity.ok("Счёт закрыт");
    }
}
