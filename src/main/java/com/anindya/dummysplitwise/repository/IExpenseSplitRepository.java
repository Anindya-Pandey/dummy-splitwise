package com.anindya.dummysplitwise.repository;

import com.anindya.dummysplitwise.entities.ExpenseSplit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IExpenseSplitRepository extends JpaRepository<ExpenseSplit, BigInteger> {
    Long countByOwedByUserIdOrOwedToUserId(BigInteger owedByUserId, BigInteger owedToUserId);
    List<ExpenseSplit> findAllByOwedByUserIdOrOwedToUserId(BigInteger owedByUserId, BigInteger owedToUserId,
        Pageable pageable);
    List<ExpenseSplit> findAllByExpenseId(BigInteger expenseId);
    ExpenseSplit findFirstByExpenseIdAndOwedByUserId(BigInteger expenseId, BigInteger owedByUserId);
    ExpenseSplit findFirstByExpenseIdAndOwedToUserId(BigInteger expenseId, BigInteger owedToUserId);
}
