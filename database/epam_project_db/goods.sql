create table goods
(
    id          int unsigned auto_increment
        primary key,
    name        varchar(255)   not null,
    description varchar(255)   null,
    price       decimal(10, 2) null,
    units_id    int(10)        not null,
    constraint name
        unique (name, description),
    constraint units_fk
        foreign key (units_id) references units (id)
            on update cascade
)
    collate = utf8_general_ci;

create index units_fk_idx
    on goods (units_id);

