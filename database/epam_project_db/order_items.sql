create table order_items
(
    order_id     int unsigned not null,
    product_id   int unsigned not null,
    product_name varchar(255) not null,
    quantity     int          not null,
    constraint order_items_order_id_foreign
        foreign key (order_id) references orders (id),
    constraint fk_order_items
        foreign key (order_id) references orders (id)
            on delete cascade,
    constraint order_items_product_id_foreign
        foreign key (product_id) references goods (id)
)
    collate = utf8_general_ci;

