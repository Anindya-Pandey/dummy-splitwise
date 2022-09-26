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

@Entity(name = "expense")
@Getter
@Setter
@ToString
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String description;
    private BigDecimal amount;

    @Column(name = "created_on", updatable = false)
    @CreationTimestamp
    private Date createdOn;
}
