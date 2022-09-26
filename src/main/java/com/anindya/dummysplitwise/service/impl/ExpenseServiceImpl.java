package com.anindya.dummysplitwise.service.impl;

import com.anindya.dummysplitwise.controllers.requests.CreateExpenseRequest;
import com.anindya.dummysplitwise.controllers.requests.ExpenseRecord;
import com.anindya.dummysplitwise.controllers.responses.FetchExpenseDetailsResponse;
import com.anindya.dummysplitwise.controllers.responses.FetchExpensesResponse;
import com.anindya.dummysplitwise.entities.Expense;
import com.anindya.dummysplitwise.entities.ExpenseSplit;
import com.anindya.dummysplitwise.entities.UserLedger;
import com.anindya.dummysplitwise.repository.IExpenseRepository;
import com.anindya.dummysplitwise.repository.IExpenseSplitRepository;
import com.anindya.dummysplitwise.repository.IUserLedgerRepository;
import com.anindya.dummysplitwise.service.IExpenseService;
import com.anindya.dummysplitwise.service.IExpenseSplitService;
import com.anindya.dummysplitwise.service.IUserLedgerService;
import com.anindya.dummysplitwise.service.bo.UserIdAmountOwedIntermediateDataBO;
import com.anindya.dummysplitwise.service.request.validators.IRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
@Slf4j
public class ExpenseServiceImpl implements IExpenseService {
    @Autowired private IExpenseRepository expenseRepository;

    @Autowired private IExpenseSplitService expenseSplitService;
    @Autowired private IUserLedgerService userLedgerService;

    @Autowired
    @Qualifier("createExpenseRequestValidator")
    private IRequestValidator createExpenseRequestValidator;

    @Override
    @Transactional
    public Expense createExpense(CreateExpenseRequest createExpenseRequest) throws Exception {
        createExpenseRequestValidator.validateRequest(createExpenseRequest);

        Expense expense = createExpenseRecord(createExpenseRequest);

        List<ExpenseSplit> expenseSplits = expenseSplitService.createExpenseSplitRecords(
            createExpenseRequest.getExpenseRecords(), expense.getId());

        userLedgerService.createOrUpdateUserLedgerForExpenseSplits(expenseSplits);

        return expense;
    }

    private Expense createExpenseRecord(CreateExpenseRequest createExpenseRequest) {
        log.info("Inside createExpenseRecord");

        Expense expense = new Expense();
        expense.setDescription(createExpenseRequest.getDescription());
        expense.setAmount(createExpenseRequest.getAmount());

        return expenseRepository.save(expense);
    }

    @Override
    public FetchExpensesResponse fetchExpenses(BigInteger userId, Integer pageNo, Integer pageSize) {
        log.info("Inside fetchExpenses: userId: {}, pageNo: {}, pageSize: {}", userId, pageNo, pageSize);

        Long count = expenseSplitService.getExpenseSplitCountForUserId(userId);

        List<BigInteger> expenseIds = expenseSplitService.getExpenseIdsForUserId(userId, pageNo, pageSize);

        List<Expense> expenses = expenseRepository.findAllByIdInOrderByIdAsc(expenseIds);

        return new FetchExpensesResponse(count, expenses);
    }

    @Override
    public FetchExpenseDetailsResponse fetchExpenseDetails(BigInteger expenseId, BigInteger userId) {
        List<ExpenseSplit> expenseSplits = expenseSplitService.getAllExpenseSplitsForExpenseIdAndUserId(expenseId,
            userId);

        if (CollectionUtils.isEmpty(expenseSplits)) {
            return new FetchExpenseDetailsResponse();
        }

        Expense expense = expenseRepository.findAllByIdInOrderByIdAsc(Arrays.asList(expenseId)).get(0);

        return new FetchExpenseDetailsResponse(expense, expenseSplits);
    }
}
