package com.anindya.dummysplitwise.service.impl;

import com.anindya.dummysplitwise.controllers.responses.FetchTotalOwedResponse;
import com.anindya.dummysplitwise.controllers.responses.FetchUserLedgersResponse;
import com.anindya.dummysplitwise.entities.ExpenseSplit;
import com.anindya.dummysplitwise.entities.UserLedger;
import com.anindya.dummysplitwise.repository.IUserLedgerRepository;
import com.anindya.dummysplitwise.repository.customresults.ITotalOwedAmount;
import com.anindya.dummysplitwise.service.IUserLedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class UserLedgerServiceImpl implements IUserLedgerService {
    @Autowired private IUserLedgerRepository userLedgerRepository;

    @Override
    public void createOrUpdateUserLedgerForExpenseSplits(List<ExpenseSplit> expenseSplits) {
        log.info("Inside createOrUpdateUserLedgerForExpenseSplits");

        for (ExpenseSplit expenseSplit: expenseSplits) {
            createOrUpdateUserLedgerForExpenseSplit(expenseSplit);
        }
    }

    private void createOrUpdateUserLedgerForExpenseSplit(ExpenseSplit expenseSplit) {
        log.info("Inside createOrUpdateUserLedgerForExpenseSplit: {}", expenseSplit);

        createOrUpdateUserLedger(expenseSplit.getOwedByUserId(), expenseSplit.getOwedToUserId(),
            expenseSplit.getOwedAmount());
        createOrUpdateUserLedger(expenseSplit.getOwedToUserId(), expenseSplit.getOwedByUserId(),
            expenseSplit.getOwedAmount().negate());
    }

    private void createOrUpdateUserLedger(BigInteger owedByUserId, BigInteger owedToUserId, BigDecimal owedAmount) {
        UserLedger userLedger = userLedgerRepository.findByUserIdAndOwedToUserId(owedByUserId,
            owedToUserId);

        if (userLedger == null) {
            createUserLedger(owedByUserId, owedToUserId, owedAmount);
            return;
        }

        userLedgerRepository.updateOwedAmountById(userLedger.getId(), userLedger.getOwedAmount().add(owedAmount));
    }

    private void createUserLedger(BigInteger owedByUserId, BigInteger owedToUserId, BigDecimal owedAmount) {
        UserLedger userLedger = new UserLedger();
        userLedger.setUserId(owedByUserId);
        userLedger.setOwedToUserId(owedToUserId);
        userLedger.setOwedAmount(owedAmount);

        userLedgerRepository.save(userLedger);
    }

    @Override
    public FetchUserLedgersResponse getAllUserLedgersForUserId(BigInteger userId, Integer pageNo, Integer pageSize) {
        Long count = userLedgerRepository.countByUserId(userId);
        List<UserLedger> userLedgers = userLedgerRepository.findAllByUserId(userId, PageRequest.of(pageNo, pageSize));

        return new FetchUserLedgersResponse(count, userLedgers);
    }

    @Override
    public FetchTotalOwedResponse getTotalOwedByUserId(BigInteger userId) {
        List<ITotalOwedAmount> totalOwedAmountList = userLedgerRepository.getTotalOwedByUserId(userId);

        if (CollectionUtils.isEmpty(totalOwedAmountList)) {
            return new FetchTotalOwedResponse(BigDecimal.ZERO);
        }

        return new FetchTotalOwedResponse(totalOwedAmountList.get(0).getTotalOwed());
    }
}
