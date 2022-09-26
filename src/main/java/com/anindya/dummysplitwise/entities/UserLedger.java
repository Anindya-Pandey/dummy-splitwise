package com.anindya.dummysplitwise.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "user_ledger")
@Getter
@Setter
@ToString
public class UserLedger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "owed_to_user_id")
    private BigInteger owedToUserId;

    @Column(name = "owed_amount")
    private BigDecimal owedAmount;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_on", updatable = false)
    @CreationTimestamp
    private Date updatedOn;
}
