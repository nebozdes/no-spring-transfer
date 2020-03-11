package ru.matveev.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "from_account_id", referencedColumnName = "id", nullable = false)
    public Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id", referencedColumnName = "id", nullable = false)
    public Account toAccount;

    public BigDecimal amount;
}
