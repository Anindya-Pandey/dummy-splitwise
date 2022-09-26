package com.anindya.dummysplitwise.controllers.responses;

import com.anindya.dummysplitwise.entities.Expense;
import com.anindya.dummysplitwise.entities.ExpenseSplit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FetchExpenseDetailsResponse {
    private Expense expense;
    private List<ExpenseSplit> expenseSplits;

    public FetchExpenseDetailsResponse() {

    }

    public FetchExpenseDetailsResponse(Expense expense, List<ExpenseSplit> expenseSplits) {
        this.expense = expense;
        this.setExpenseSplits(expenseSplits);
    }
}
