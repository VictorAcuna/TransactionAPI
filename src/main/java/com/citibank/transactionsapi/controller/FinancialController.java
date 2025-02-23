package com.citibank.transactionsapi.controller;

import com.citibank.transactionsapi.dto.TransactionDTO;
import com.citibank.transactionsapi.model.TaxRule;
import com.citibank.transactionsapi.model.Transaction;
import com.citibank.transactionsapi.service.FinancialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Validated
public class FinancialController {
    private final FinancialService financialService;

    @Autowired
    public FinancialController(FinancialService financialService) {
        this.financialService = financialService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<Transaction> submitTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        try{
            return ResponseEntity.ok(financialService.processTransaction(transactionDTO));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(financialService.getAllTransactions());
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        try{
            return  ResponseEntity.ok(financialService.getTransaction(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/tax-rule", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public ResponseEntity<TaxRule> submitTaxRule(@RequestBody TaxRule taxRule) {
        return ResponseEntity.ok(financialService.saveTaxRule(taxRule));
    }

    @GetMapping("/taxes/{transactionId}")
    public ResponseEntity<BigDecimal> calculateTax(@PathVariable Long transactionId) {
        return ResponseEntity.ok(financialService.calculateTax(transactionId));
    }

    @GetMapping("/taxes")
    public ResponseEntity<BigDecimal> calculateTaxes() {
        return ResponseEntity.ok(financialService.calculateTaxes());
    }
}

