package com.anindya.dummysplitwise.service.request.validators.impl;

import com.anindya.dummysplitwise.constants.ExpenseSplitType;
import com.anindya.dummysplitwise.controllers.requests.CreateExpenseRequest;
import com.anindya.dummysplitwise.controllers.requests.ExpenseRecord;
import com.anindya.dummysplitwise.service.request.validators.IRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
@Slf4j
public class CreateExpenseRequestValidator implements IRequestValidator {
    @Override
    public void validateRequest(Object request) throws Exception {
        if (request == null) {
            throw new Exception("request is null");
        }

        CreateExpenseRequest createExpenseRequest = (CreateExpenseRequest) request;

        if (createExpenseRequest.getAmount() == null) {
            throw new Exception("amount is null");
        }

        if (StringUtils.isEmpty(createExpenseRequest.getDescription())) {
            throw new Exception("description is empty");
        }

        if (CollectionUtils.isEmpty(createExpenseRequest.getExpenseRecords())) {
            throw new Exception("expenseRecords is empty");
        }

        if (createExpenseRequest.getExpenseSplitType() == null) {
            throw new Exception("expenseSplitType is null");
        }

        if (createExpenseRequest.getExpenseSplitType() == ExpenseSplitType.EQUAL) {
            validateRequestForEqualSplit(createExpenseRequest);
            return;
        }

        if (createExpenseRequest.getExpenseSplitType() == ExpenseSplitType.MANUAL) {
            validateRequestForManualSplit(createExpenseRequest);
            return;
        }

        if (createExpenseRequest.getExpenseSplitType() == ExpenseSplitType.PERCENTAGE) {
            validateRequestForPercentageSplit(createExpenseRequest);
        }
    }

    private void validateRequestForEqualSplit(CreateExpenseRequest createExpenseRequest) throws Exception {
        BigDecimal amount = createExpenseRequest.getAmount();

        BigDecimal expectedContribution = amount.divide(
            BigDecimal.valueOf(createExpenseRequest.getExpenseRecords().size()), 2);

        BigDecimal contributionSum = BigDecimal.ZERO;

        for (ExpenseRecord expenseRecord: createExpenseRequest.getExpenseRecords()) {
            expenseRecord.setExpectedContribution(expectedContribution);

            contributionSum = contributionSum.add(expenseRecord.getContribution());
        }

        if (contributionSum.compareTo(amount) != 0) {
            throw new Exception("contributions in expenseRecords are not valid");
        }
    }

    private void validateRequestForManualSplit(CreateExpenseRequest createExpenseRequest) throws Exception {
        BigDecimal amount = createExpenseRequest.getAmount();

        BigDecimal expectedContributionSum = BigDecimal.ZERO;
        BigDecimal contributionSum = BigDecimal.ZERO;

        for (ExpenseRecord expenseRecord: createExpenseRequest.getExpenseRecords()) {
            if (expenseRecord.getExpectedContribution() == null) {
                throw new Exception("expectedContribution is null for some expense record");
            }

            expectedContributionSum = expectedContributionSum.add(expenseRecord.getExpectedContribution());
            contributionSum = contributionSum.add(expenseRecord.getContribution());
        }

        if (contributionSum.compareTo(amount) != 0) {
            throw new Exception("contributions in expenseRecords are not valid");
        }

        if (expectedContributionSum.compareTo(amount) != 0) {
            throw new Exception("expected contributions in expenseRecords are not valid");
        }
    }

    private void validateRequestForPercentageSplit(CreateExpenseRequest createExpenseRequest) throws Exception {
        BigDecimal amount = createExpenseRequest.getAmount();

        BigDecimal expectedContributionSum = BigDecimal.ZERO;
        BigDecimal contributionSum = BigDecimal.ZERO;

        for (ExpenseRecord expenseRecord: createExpenseRequest.getExpenseRecords()) {
            if (expenseRecord.getExpectedContributionAsPercent() == null) {
                throw new Exception("expectedContributionAsPercent is null for some expense record");
            }

            expenseRecord.setExpectedContribution(amount
                .multiply(expenseRecord.getExpectedContributionAsPercent())
                .divide(BigDecimal.valueOf(100), 2));

            expectedContributionSum = expectedContributionSum.add(expenseRecord.getExpectedContribution());
            contributionSum = contributionSum.add(expenseRecord.getContribution());
        }

        if (contributionSum.compareTo(amount) != 0) {
            throw new Exception("contributions in expenseRecords are not valid");
        }

        if (expectedContributionSum.compareTo(amount) != 0) {
            throw new Exception("expected contributions in expenseRecords are not valid");
        }
    }
}
