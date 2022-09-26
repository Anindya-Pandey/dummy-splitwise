package com.anindya.dummysplitwise.repository.customresults;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ITotalOwedAmount {
    BigInteger getUserId();
    BigDecimal getTotalOwed();
}
