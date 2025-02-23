package com.citibank.transactionsapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    private Transaction transaction;
    private TaxRule taxRule;

    @BeforeEach
    void setUp() {
        taxRule = new TaxRule();
        taxRule.setId(1L);
        taxRule.setRate(new BigDecimal("10.00"));

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDescription("Test Transaction");
        transaction.setTaxRule(taxRule);
        transaction.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testTransactionCreation() {
        assertThat(transaction).isNotNull();
        assertThat(transaction.getId()).isEqualTo(1L);
        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(transaction.getDescription()).isEqualTo("Test Transaction");
        assertThat(transaction.getTaxRule()).isEqualTo(taxRule);
        assertThat(transaction.getTimestamp()).isNotNull();
    }

}

