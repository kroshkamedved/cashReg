create table roles
(
    role enum ('CASHIER', 'SENIOR_CASHIER', 'COMMODITY_EXPERT') not null
        primary key
)
    collate = utf8_general_ci;

