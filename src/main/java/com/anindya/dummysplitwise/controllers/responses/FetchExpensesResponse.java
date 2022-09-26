package com.anindya.dummysplitwise.controllers.responses;

import com.anindya.dummysplitwise.entities.Expense;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FetchExpensesResponse {
    Long count;
    List<Expense> expenses;

    public FetchExpensesResponse() {}

    public FetchExpensesResponse(Long count, List<Expense> expenses) {
        this.count = count;
        this.expenses = expenses;
    }
}
