package finalCDT.finalCDT.services;

import finalCDT.finalCDT.dto.AccountDTO;
import finalCDT.finalCDT.dto.AccountResponseDTO;
import finalCDT.finalCDT.entitys.Account;
import finalCDT.finalCDT.entitys.User;
import finalCDT.finalCDT.exceptions.ResourceNotFoundException;
import finalCDT.finalCDT.repositories.AccountRepository;
import finalCDT.finalCDT.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AccountResponseDTO createAccount(AccountDTO accountDTO) {
        User user = userRepository.findByEmail(accountDTO.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (accountRepository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }

        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setBalance(accountDTO.getBalance());
        account.setActive(true);
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);
        return convertToResponseDTO(savedAccount);
    }

    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return convertToResponseDTO(account);
    }

    public List<AccountResponseDTO> getAccountsByUserEmail(String email) {
        List<Account> accounts = accountRepository.findByUserEmail(email);
        return accounts.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponseDTO updateBalance(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.setBalance(BigDecimal.valueOf(amount));
        Account updatedAccount = accountRepository.save(account);
        return convertToResponseDTO(updatedAccount);
    }

    @Transactional
    public AccountResponseDTO deactivateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.setActive(false);
        Account updatedAccount = accountRepository.save(account);
        return convertToResponseDTO(updatedAccount);
    }

    private AccountResponseDTO convertToResponseDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setActive(account.isActive());
        dto.setUserEmail(account.getUser().getEmail());
        return dto;
    }
}
