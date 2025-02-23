package com.citibank.transactionsapi.service;

import com.citibank.transactionsapi.exception.ResourceNotFoundException;
import com.citibank.transactionsapi.model.Transaction;
import com.citibank.transactionsapi.model.TaxRule;
import com.citibank.transactionsapi.repository.TaxRuleRepository;
import com.citibank.transactionsapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TaxRuleRepository taxRuleRepository;

    @InjectMocks
    private FinancialService financialService;

    private Transaction testTransaction;
    private TaxRule testTaxRule;

    @BeforeEach
    void setUp() {
        testTaxRule = new TaxRule();
        testTaxRule.setId(1L);
        testTaxRule.setRate(new BigDecimal("10.00"));
        testTaxRule.setActive(true);

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setAmount(new BigDecimal("500.00"));
        testTransaction.setTaxRule(testTaxRule);
    }

    @Test
    void getAllTransactions_Success() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction));

        List<Transaction> results = financialService.getAllTransactions();

        assertThat(results).hasSize(1);
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getTransaction_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));

        Transaction result = financialService.getTransaction(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getTransaction_NotFound_ThrowsException() {
        when(transactionRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> financialService.getTransaction(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

