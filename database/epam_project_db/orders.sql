create table orders
(
    id         int unsigned auto_increment
        primary key,
    date       datetime     not null,
    created_by int unsigned not null,
    constraint orders_created_by_foreign
        foreign key (created_by) references users (id)
)
    collate = utf8_general_ci;

