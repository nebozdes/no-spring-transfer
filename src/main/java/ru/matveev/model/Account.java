package ru.matveev.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends PanacheEntity {

    public BigDecimal balance;
}
