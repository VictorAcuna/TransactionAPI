package com.citibank.transactionsapi.dto;

import java.math.BigDecimal;

public class TransactionDTO {
    private BigDecimal amount;
    private String description;
    private Long tax_rate_id;

    public TransactionDTO(BigDecimal amount, String description, Long tax_rate_id) {
        this.amount = amount;
        this.description = description;
        this.tax_rate_id = tax_rate_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTax_rate_id() {
        return tax_rate_id;
    }

    public void setTax_rate_id(Long tax_rate_id) {
        this.tax_rate_id = tax_rate_id;
    }
}
