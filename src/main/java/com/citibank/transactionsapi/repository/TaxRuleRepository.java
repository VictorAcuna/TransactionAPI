package com.citibank.transactionsapi.repository;

import com.citibank.transactionsapi.model.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {

    List<TaxRule> findByActiveTrue();
}

