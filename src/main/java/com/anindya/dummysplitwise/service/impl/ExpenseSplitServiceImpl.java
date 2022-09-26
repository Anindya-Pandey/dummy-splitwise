package com.anindya.dummysplitwise.service.impl;

import com.anindya.dummysplitwise.controllers.requests.ExpenseRecord;
import com.anindya.dummysplitwise.entities.Expense;
import com.anindya.dummysplitwise.entities.ExpenseSplit;
import com.anindya.dummysplitwise.repository.IExpenseSplitRepository;
import com.anindya.dummysplitwise.service.IExpenseSplitService;
import com.anindya.dummysplitwise.service.bo.UserIdAmountOwedIntermediateDataBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExpenseSplitServiceImpl implements IExpenseSplitService {
    @Autowired private IExpenseSplitRepository expenseSplitRepository;

    @Override
    public List<ExpenseSplit> createExpenseSplitRecords(List<ExpenseRecord> expenseRecords, BigInteger expenseId) {
        log.info("Inside createExpenseSplitRecords");

        List<ExpenseSplit> expenseSplits = populateExpenseSplits(expenseRecords, expenseId);

        expenseSplitRepository.saveAll(expenseSplits);

        return expenseSplits;
    }

    private void populateLenderAndBorrowerRecords(List<ExpenseRecord> expenseRecords,
        List<UserIdAmountOwedIntermediateDataBO> lenderRecords,
        List<UserIdAmountOwedIntermediateDataBO> borrowerRecords) {

        for (ExpenseRecord expenseRecord: expenseRecords) {
            if (expenseRecord.getContribution().compareTo(expenseRecord.getExpectedContribution()) > 0) {
                lenderRecords.add(new UserIdAmountOwedIntermediateDataBO(expenseRecord.getUserId(),
                    expenseRecord.getContribution().subtract(expenseRecord.getExpectedContribution())));
                continue;
            }

            if (expenseRecord.getContribution().compareTo(expenseRecord.getExpectedContribution()) < 0) {
                borrowerRecords.add(new UserIdAmountOwedIntermediateDataBO(expenseRecord.getUserId(),
                    expenseRecord.getExpectedContribution().subtract(expenseRecord.getContribution())));
            }
        }
    }

    private List<ExpenseSplit> populateExpenseSplits(List<ExpenseRecord> expenseRecords,
        BigInteger expenseId) {
        log.info("Inside populateExpenseSplits");

        List<ExpenseSplit> expenseSplits = new ArrayList<>();

        List<UserIdAmountOwedIntermediateDataBO> lenderRecords = new ArrayList<>();
        List<UserIdAmountOwedIntermediateDataBO> borrowerRecords = new ArrayList<>();

        populateLenderAndBorrowerRecords(expenseRecords, lenderRecords, borrowerRecords);

        int nextLenderToSettleIndex = 0;

        for (UserIdAmountOwedIntermediateDataBO borrowerRecord: borrowerRecords) {
            nextLenderToSettleIndex = settleLenders(lenderRecords, nextLenderToSettleIndex,
                borrowerRecord, expenseId, expenseSplits);
        }

        return expenseSplits;
    }

    private int settleLenders(List<UserIdAmountOwedIntermediateDataBO> lenderRecords, int nextLenderToSettleIndex,
        UserIdAmountOwedIntermediateDataBO borrowerRecord, BigInteger expenseId,
        List<ExpenseSplit> expenseSplits) {
        BigDecimal borrowerOwedAmount = borrowerRecord.getOwedAmount();
        BigInteger borrowerUserId = borrowerRecord.getUserId();

        int i = nextLenderToSettleIndex;

        for (; i < lenderRecords.size(); i++) {
            BigDecimal lenderOwedAmount = lenderRecords.get(i).getOwedAmount();
            BigInteger lenderUserId = lenderRecords.get(i).getUserId();

            if (borrowerOwedAmount.compareTo(lenderOwedAmount) > 0) {
                borrowerOwedAmount = settleLender(expenseId, borrowerRecord, lenderRecords.get(i), expenseSplits);
                continue;
            }

            if (borrowerOwedAmount.compareTo(lenderOwedAmount) < 0) {
                partiallySettleLender(expenseId, borrowerUserId, lenderUserId, borrowerOwedAmount,
                    lenderOwedAmount, lenderRecords, i, expenseSplits);

                break;
            }

            partiallySettleLender(expenseId, borrowerUserId, lenderUserId, borrowerOwedAmount,
                lenderOwedAmount, lenderRecords, i, expenseSplits);

            i++;

            break;
        }

        return i;
    }

    private void partiallySettleLender(BigInteger expenseId, BigInteger borrowerUserId, BigInteger lenderUserId,
        BigDecimal borrowerOwedAmount, BigDecimal lenderOwedAmount,
        List<UserIdAmountOwedIntermediateDataBO> lenderRecords, int i, List<ExpenseSplit> expenseSplits) {
        ExpenseSplit expenseSplit = populateExpenseSplit(expenseId, borrowerUserId, lenderUserId,
            borrowerOwedAmount);

        expenseSplits.add(expenseSplit);

        lenderRecords.get(i).setOwedAmount(lenderOwedAmount.subtract(borrowerOwedAmount));
    }

    private BigDecimal settleLender(BigInteger expenseId, UserIdAmountOwedIntermediateDataBO borrowerRecord,
        UserIdAmountOwedIntermediateDataBO lenderRecord, List<ExpenseSplit> expenseSplits) {
        BigDecimal borrowerOwedAmount = borrowerRecord.getOwedAmount();
        BigInteger borrowerUserId = borrowerRecord.getUserId();
        BigDecimal lenderOwedAmount = lenderRecord.getOwedAmount();
        BigInteger lenderUserId = lenderRecord.getUserId();

        ExpenseSplit expenseSplit = populateExpenseSplit(expenseId, borrowerUserId, lenderUserId,
            lenderOwedAmount);

        expenseSplits.add(expenseSplit);

        borrowerRecord.setOwedAmount(borrowerOwedAmount.subtract(lenderOwedAmount));

        return borrowerRecord.getOwedAmount();
    }

    @Override
    public Long getExpenseSplitCountForUserId(BigInteger userId) {
        return expenseSplitRepository.countByOwedByUserIdOrOwedToUserId(userId, userId);
    }

    @Override
    public List<BigInteger> getExpenseIdsForUserId(BigInteger userId, Integer pageNo, Integer pageSize) {
        List<ExpenseSplit> expenseSplits = expenseSplitRepository.findAllByOwedByUserIdOrOwedToUserId(userId,
            userId, PageRequest.of(pageNo, pageSize));

        List<BigInteger> expenseIds = new ArrayList<>();

        for (ExpenseSplit expenseSplit: expenseSplits) {
            expenseIds.add(expenseSplit.getExpenseId());
        }

        return expenseIds;
    }

    @Override
    public List<ExpenseSplit> getAllExpenseSplitsForExpenseIdAndUserId(BigInteger expenseId, BigInteger userId) {
        ExpenseSplit expenseSplitForOwedByUserId = expenseSplitRepository.findFirstByExpenseIdAndOwedByUserId(expenseId,
            userId);
        ExpenseSplit expenseSplitForOwedToUserId = expenseSplitRepository.findFirstByExpenseIdAndOwedToUserId(expenseId,
            userId);

        if (expenseSplitForOwedByUserId == null && expenseSplitForOwedToUserId == null) {
            return null;
        }

        return expenseSplitRepository.findAllByExpenseId(expenseId);
    }

    private ExpenseSplit populateExpenseSplit(BigInteger expenseId, BigInteger owedByUserId, BigInteger owedToUserId,
        BigDecimal owedAmount) {
        ExpenseSplit expenseSplit = new ExpenseSplit();

        expenseSplit.setExpenseId(expenseId);
        expenseSplit.setOwedAmount(owedAmount);
        expenseSplit.setOwedByUserId(owedByUserId);
        expenseSplit.setOwedToUserId(owedToUserId);

        return expenseSplit;
    }
}
