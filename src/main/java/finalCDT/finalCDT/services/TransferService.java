package finalCDT.finalCDT.services;

import finalCDT.finalCDT.dto.TransferDTO;
import finalCDT.finalCDT.entitys.Account;
import finalCDT.finalCDT.entitys.Transfer;
import finalCDT.finalCDT.exceptions.InsufficientFundsException;
import finalCDT.finalCDT.exceptions.ResourceNotFoundException;
import finalCDT.finalCDT.repositories.AccountRepository;
import finalCDT.finalCDT.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Transactional
    public Transfer transfer(TransferDTO transferDTO) {
        log.info("Processing transfer from account {} to account {}", 
            transferDTO.getSourceAccountNumber(), transferDTO.getTargetAccountNumber());

        Account sourceAccount = accountRepository.findByAccountNumber(transferDTO.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account targetAccount = accountRepository.findByAccountNumber(transferDTO.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Target account not found"));

        if (!sourceAccount.isActive()) {
            throw new IllegalStateException("Source account is not active");
        }

        if (!targetAccount.isActive()) {
            throw new IllegalStateException("Target account is not active");
        }

        BigDecimal transferAmount = BigDecimal.valueOf(transferDTO.getAmount());
        if (!validateSufficientFunds(sourceAccount, transferAmount)) {
            throw new InsufficientFundsException("Insufficient funds in source account");
        }

        // Create and save the transfer
        Transfer transfer = new Transfer();
        transfer.setSourceAccount(sourceAccount);
        transfer.setTargetAccount(targetAccount);
        transfer.setAmount(transferAmount);
        transfer.setDateTime(LocalDateTime.now());
        transfer.setDescription(transferDTO.getDescription());

        // Update account balances
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
        targetAccount.setBalance(targetAccount.getBalance().add(transferAmount));

        // Save the updated accounts
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        log.info("Transfer completed successfully");
        return transferRepository.save(transfer);
    }

    public List<Transfer> findBySourceAccountNumber(String accountNumber) {
        return transferRepository.findBySourceAccount_AccountNumber(accountNumber);
    }

    public List<Transfer> findByTargetAccountNumber(String accountNumber) {
        return transferRepository.findByTargetAccount_AccountNumber(accountNumber);
    }

    private boolean validateSufficientFunds(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) >= 0;
    }
}
