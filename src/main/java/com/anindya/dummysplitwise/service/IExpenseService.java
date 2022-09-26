package com.anindya.dummysplitwise.service;

import com.anindya.dummysplitwise.controllers.requests.CreateExpenseRequest;
import com.anindya.dummysplitwise.controllers.responses.FetchExpenseDetailsResponse;
import com.anindya.dummysplitwise.controllers.responses.FetchExpensesResponse;
import com.anindya.dummysplitwise.entities.Expense;

import java.math.BigInteger;
import java.util.List;

public interface IExpenseService {
    Expense createExpense(CreateExpenseRequest createExpenseRequest) throws Exception;
    FetchExpensesResponse fetchExpenses(BigInteger userId, Integer pageNo, Integer pageSize);
    FetchExpenseDetailsResponse fetchExpenseDetails(BigInteger expenseId, BigInteger userId);
}
