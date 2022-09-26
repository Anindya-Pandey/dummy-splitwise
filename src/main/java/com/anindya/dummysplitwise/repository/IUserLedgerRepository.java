package com.anindya.dummysplitwise.repository;

import com.anindya.dummysplitwise.entities.UserLedger;
import com.anindya.dummysplitwise.repository.customresults.ITotalOwedAmount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface IUserLedgerRepository extends JpaRepository<UserLedger, BigInteger> {
    List<UserLedger> findAllByUserId(BigInteger userId, Pageable pageable);
    Long countByUserId(BigInteger userId);
    UserLedger findByUserIdAndOwedToUserId(BigInteger userId, BigInteger owedToUserId);


    @Modifying
    @Query("update user_ledger u set u.owedAmount = :owedAmount where u.id = :id")
    void updateOwedAmountById(@Param(value = "id") BigInteger id, @Param(value = "owedAmount") BigDecimal owedAmount);

    @Query("select u.userId AS userId, SUM(u.owedAmount) AS totalOwed "
            + "FROM user_ledger AS u where u.userId = :userId GROUP BY u.userId")
    List<ITotalOwedAmount> getTotalOwedByUserId(BigInteger userId);
}
