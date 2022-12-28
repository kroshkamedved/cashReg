create table warehouse
(
    product_id int unsigned not null,
    quantity   int          not null,
    constraint product_id
        unique (product_id),
    constraint warehouse
        foreign key (product_id) references goods (id)
            on delete cascade,
    constraint warehouse_product_id_foreign
        foreign key (product_id) references goods (id)
)
    collate = utf8_general_ci;

