package com.anindya.dummysplitwise.repository;

import com.anindya.dummysplitwise.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense, BigInteger> {
    List<Expense> findAllByIdInOrderByIdAsc(List<BigInteger> ids);
}
