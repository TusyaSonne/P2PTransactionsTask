package ru.dzhenbaz.P2PTransactionsTask.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzhenbaz.P2PTransactionsTask.dto.TransferRequest;
import ru.dzhenbaz.P2PTransactionsTask.security.JwtUtil;
import ru.dzhenbaz.P2PTransactionsTask.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    public TransactionController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody @Valid TransferRequest request) {
        Long userId = extractUserId(authHeader);
        String message = transactionService.transfer(userId, request);
        return ResponseEntity.ok(message);
    }

    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.validateTokenAndRetrieveClaim(token);
    }
}
