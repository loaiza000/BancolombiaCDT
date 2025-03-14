package finalCDT.finalCDT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalCDT.finalCDT.dto.TransferDTO;
import finalCDT.finalCDT.helpers.ApiResponse;
import finalCDT.finalCDT.services.TransferService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> transfer(@Valid @RequestBody TransferDTO transferDTO) {
        log.info("Transfer request received from account {} to account {}", 
            transferDTO.getSourceAccountNumber(), transferDTO.getTargetAccountNumber());
        
        transferService.transfer(transferDTO);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Transfer completed successfully"));
    }
}
