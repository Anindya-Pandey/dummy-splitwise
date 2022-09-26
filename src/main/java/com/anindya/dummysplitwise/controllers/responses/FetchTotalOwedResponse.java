package com.anindya.dummysplitwise.controllers.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class FetchTotalOwedResponse {
    private BigDecimal totalOwed;

    public FetchTotalOwedResponse(BigDecimal totalOwed) {
        this.totalOwed = totalOwed;
    }
}
