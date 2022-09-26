package com.anindya.dummysplitwise.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "expense_split")
@Getter
@Setter
@ToString
public class ExpenseSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "expense_id")
    private BigInteger expenseId;

    @Column(name = "owed_by_user_id")
    private BigInteger owedByUserId;

    @Column(name = "owed_to_user_id")
    private BigInteger owedToUserId;

    @Column(name = "owed_amount")
    private BigDecimal owedAmount;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private Date createdOn;
}
