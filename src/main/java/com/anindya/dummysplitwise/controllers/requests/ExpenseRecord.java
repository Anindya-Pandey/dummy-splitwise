package com.anindya.dummysplitwise.controllers.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
public class ExpenseRecord {
    private BigInteger userId;
    private BigDecimal contribution;
    private BigDecimal expectedContribution;
    private BigDecimal expectedContributionAsPercent;
}
