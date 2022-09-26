package com.anindya.dummysplitwise.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@ToString
public class UserIdAmountOwedIntermediateDataBO {
    private BigInteger userId;
    private BigDecimal owedAmount;

    public UserIdAmountOwedIntermediateDataBO(BigInteger userId, BigDecimal owedAmount) {
        this.userId = userId;
        this.owedAmount = owedAmount;
    }
}
