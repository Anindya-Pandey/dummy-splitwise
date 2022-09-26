package com.anindya.dummysplitwise.service;

import com.anindya.dummysplitwise.controllers.responses.FetchTotalOwedResponse;
import com.anindya.dummysplitwise.controllers.responses.FetchUserLedgersResponse;
import com.anindya.dummysplitwise.entities.ExpenseSplit;
import com.anindya.dummysplitwise.entities.UserLedger;

import java.math.BigInteger;
import java.util.List;

public interface IUserLedgerService {
    void createOrUpdateUserLedgerForExpenseSplits(List<ExpenseSplit> expenseSplits);
    FetchUserLedgersResponse getAllUserLedgersForUserId(BigInteger userId, Integer pageNo, Integer pageSize);
    FetchTotalOwedResponse getTotalOwedByUserId(BigInteger userId);
}
