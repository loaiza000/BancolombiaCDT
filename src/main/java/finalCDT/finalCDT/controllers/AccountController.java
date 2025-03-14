package finalCDT.finalCDT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import finalCDT.finalCDT.dto.AccountDTO;
import finalCDT.finalCDT.dto.AccountResponseDTO;
import finalCDT.finalCDT.helpers.ApiResponse;
import finalCDT.finalCDT.services.AccountService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponseDTO>> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountResponseDTO account = accountService.createAccount(accountDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Account created successfully", account));
    }

    @GetMapping("/{number}")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> getAccountByNumber(@PathVariable String number) {
        AccountResponseDTO account = accountService.getAccountByNumber(number);
        return ResponseEntity.ok(new ApiResponse<>(true, "Account retrieved successfully", account));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<ApiResponse<List<AccountResponseDTO>>> getAccountsByUserEmail(@PathVariable String email) {
        List<AccountResponseDTO> accounts = accountService.getAccountsByUserEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Accounts retrieved successfully", accounts));
    }

    @PutMapping("/{number}/balance")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> updateBalance(
            @PathVariable String number,
            @RequestParam Double amount) {
        AccountResponseDTO account = accountService.updateBalance(number, amount);
        return ResponseEntity.ok(new ApiResponse<>(true, "Balance updated successfully", account));
    }

    @PutMapping("/{number}/deactivate")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> deactivateAccount(@PathVariable String number) {
        AccountResponseDTO account = accountService.deactivateAccount(number);
        return ResponseEntity.ok(new ApiResponse<>(true, "Account deactivated successfully", account));
    }
}
