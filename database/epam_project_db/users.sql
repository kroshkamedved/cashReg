create table users
(
    id        int unsigned auto_increment
        primary key,
    role      enum ('CASHIER', 'SENIOR_CASHIER', 'COMMODITY_EXPERT') not null,
    name      text                                                   null,
    pass_hash varchar(255)                                           not null,
    constraint name
        unique (name) using hash,
    constraint unique_role_for_user
        unique (name, role) using hash,
    constraint role_constraint
        foreign key (role) references roles (role)
)
    collate = utf8_general_ci;

