package com.anindya.dummysplitwise.service;

import com.anindya.dummysplitwise.controllers.requests.ExpenseRecord;
import com.anindya.dummysplitwise.entities.ExpenseSplit;

import java.math.BigInteger;
import java.util.List;

public interface IExpenseSplitService {
    List<ExpenseSplit> createExpenseSplitRecords(List<ExpenseRecord> expenseRecords, BigInteger expenseId);
    Long getExpenseSplitCountForUserId(BigInteger userId);
    List<BigInteger> getExpenseIdsForUserId(BigInteger userId, Integer pageNo, Integer pageSize);
    List<ExpenseSplit> getAllExpenseSplitsForExpenseIdAndUserId(BigInteger expenseId, BigInteger userId);
}
