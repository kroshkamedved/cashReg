create table units
(
    id   int(10)      not null
        primary key,
    unit varchar(255) not null,
    constraint unit_UNIQUE
        unique (unit)
)
    collate = utf8_bin;

