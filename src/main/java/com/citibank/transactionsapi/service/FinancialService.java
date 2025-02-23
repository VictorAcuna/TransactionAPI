package com.citibank.transactionsapi.service;

import com.citibank.transactionsapi.dto.TransactionDTO;
import com.citibank.transactionsapi.exception.TransactionProcessingException;
import com.citibank.transactionsapi.model.TaxRule;
import com.citibank.transactionsapi.model.Transaction;
import com.citibank.transactionsapi.model.TransactionStatus;
import com.citibank.transactionsapi.repository.TaxRuleRepository;
import com.citibank.transactionsapi.repository.TransactionRepository;
import com.citibank.transactionsapi.exception.ResourceNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FinancialService {
    private final TransactionRepository transactionRepository;
    private final TaxRuleRepository taxRuleRepository;

    @Autowired
    public FinancialService(TransactionRepository transactionRepository,
                            TaxRuleRepository taxRuleRepository) {
        this.transactionRepository = transactionRepository;
        this.taxRuleRepository = taxRuleRepository;
    }

    @CircuitBreaker(name = "processTransaction", fallbackMethod = "processTransactionFallback")
    @Retry(name = "processTransaction")
    public Transaction processTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();

        try {
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setStatus(TransactionStatus.PROCESSING);
            transaction.setTaxRule(taxRuleRepository.getReferenceById(transactionDTO.getTax_rate_id()));
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDescription(transactionDTO.getDescription());
            transaction.setStatus(TransactionStatus.COMPLETED);

            return transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error("Error processing transaction", e);
            transaction.setStatus(TransactionStatus.FAILED);
            throw new TransactionProcessingException("Failed to process transaction", e);
        }
    }

    public Transaction processTransactionFallback(Transaction transaction, Exception e) {
        transaction.setStatus(TransactionStatus.PENDING_RETRY);
        return transactionRepository.save(transaction);
    }

    @CircuitBreaker(name = "calculateTax", fallbackMethod = "calculateTaxFallback")
    public BigDecimal calculateTax(Long transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            return null;
        }
        Transaction transaction = transactionOpt.get();
        return transaction.getAmount().multiply(transaction.getTaxRule().getRate().divide(new BigDecimal("100.00")));
    }

    public BigDecimal calculateTaxFallback(Long transactionId, Exception e) {
        log.error("Failed to calculate tax for transaction {}", transactionId, e);
        return BigDecimal.ZERO;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            return Collections.emptyList();
        }
        return transactions;
    }

    public Transaction getTransaction(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id " + id + " not found"));
    }

    public TaxRule saveTaxRule(TaxRule taxRule) {
        return taxRuleRepository.save(taxRule);
    }

    public BigDecimal calculateTaxes() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return transactions.stream()
                .map(transaction -> transaction.getAmount().multiply(transaction.getTaxRule().getRate().divide(new BigDecimal("100.00"))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

