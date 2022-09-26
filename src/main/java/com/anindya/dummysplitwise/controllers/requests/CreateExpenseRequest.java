package com.anindya.dummysplitwise.controllers.requests;

import com.anindya.dummysplitwise.constants.ExpenseSplitType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateExpenseRequest {
    private String description;
    private BigDecimal amount;
    private List<ExpenseRecord> expenseRecords;
    private ExpenseSplitType expenseSplitType;
}
