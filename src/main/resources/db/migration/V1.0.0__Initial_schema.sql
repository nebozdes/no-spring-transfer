create sequence hibernate_sequence;

create TABLE accounts
(
  id bigint primary key,
  balance decimal(19,2)
);

create TABLE transactions
(
  id bigint primary key,
  from_account_id bigint NOT NULL,
  to_account_id bigint NOT NULL,
  amount decimal(19,2),
  PRIMARY KEY(id),
 FOREIGN KEY (from_account_id) REFERENCES accounts(id),
 FOREIGN KEY (to_account_id) REFERENCES accounts(id),
);

insert into accounts(id, balance) values (1, 100);
insert into accounts(id, balance) values (2, 200);
