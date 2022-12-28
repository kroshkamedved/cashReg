package com.elearn.fp.db.query;

public class Query {
    private Query() {
    }

    public static final String DELETE_ITEM_FROM_ORDER = "delete from order_items where order_id = ? and product_id = ?";
    public static final String COUNT_ORDER_ITEMS = "select count(*) as oiCount from order_items where order_id =";
    public static final String DELETE_ORDER_WITH_ID = "DELETE FROM orders where id =";
    public static final String UDPATE_WAREHOUSE_AFTER_ITEM_DELETED_FROM_ORDER = "UPDATE warehouse SET quantity = (quantity + ?) WHERE product_id  = ?";
    public static final String INSERT_INTO_ORDER_ITEMS = "insert into order_items " +
            "(order_id,product_id, product_name,quantity)" +
            " values (?,?,?,?)";
    public static final String SELECT_CHECKS_FOR_CASHIER = "select * from order_items oi " +
            "                                                       join orders o on o.id = oi.order_id" +
            "                                                        join goods g on oi.product_id = g.id " +
            "                                                           and o.created_by = ? " +
            "                                                           and cast(o.date as  date) = current_date()" +
            "                                                       order by o.id asc";
    public static final String SELECT_CHECKS_FOR_SENIOR_CASHIER = "select o.id, o.date, o.created_by, oi.product_id, oi.product_name, g.description, oi.quantity, g.price, g.units_id" +
            " from order_items oi" +
            "                                                                join orders o on o.id = oi.order_id " +
            "                                                                join goods g on oi.product_id = g.id" +
            "                                                                and cast(o.date as  date) = curdate()" +
            "                                                                order by o.id asc";/*+
            "                                                                LIMIT ? offset ?";*/
    public static final String SELECT_CHECKS_FOR_SENIOR_CASHIER_WITH_DATE = "select o.id, o.date, o.created_by, oi.product_id, oi.product_name, g.description, oi.quantity, g.price, g.units_id" +
            "                                                                   from order_items oi" +
            "                                                                       join orders o on o.id = oi.order_id " +
            "                                                                       join goods g on oi.product_id = g.id" +
            "                                                                   and cast(o.date as  date) like ?" +
            "                                                                   order by o.id asc";/*+
            "                                                                LIMIT ? offset ?";*/
    public static final String INSERT_INTO_ORDERS = "Insert into orders (date,created_by) VALUES (?,?)";

    public static final String COUNT_ALL_ORDERS = "SELECT COUNT(*) as quantity, curdate() as currentDate" +
            "  FROM orders o " +
            "where cast(o.date as DATE) = curdate();";
    public static final String COUNT_ALL_ORDERS_FROM_DATE = "SELECT COUNT(*) as quantity" +
            "  FROM orders o " +
            "where cast(o.date as DATE) LIKE ";
    public static final String COUNT_ALL_ORDERS_FROM_DATE_FOR_CASHIER = "SELECT COUNT(*) as quantity " +
            " FROM orders o " +
            "where cast(o.date as DATE) = curdate() AND created_by = ?";

    public static final String INSERT_STATEMENT_SQL = "insert into goods(name,description,price, units_id) VALUES (?, ?, ?,?)";
    public static final String INSERT_TO_WAREHOUSE_SQL = "insert into warehouse(product_id,quantity) VALUES (?, ?)";
    public static final String UPDATE_ITEM_STOCK_SQL = "UPDATE warehouse SET QUANTITY = ? where product_id = ? ";
    public static final String UPDATE_ITEM_STOCK_AFTER_PURCHASE = "UPDATE warehouse SET QUANTITY = (QUANTITY - ?) where product_id = ? ";
    public static final String DELETE_ITEM_FROM_STOCK = "DELETE FROM goods WHERE ID = ? ";
    public static final String SELECT_ALL_GOODS = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id";
    public static final String SELECT_ITEM = "SELECT * FROM epam_project_db.goods gd join warehouse wh on gd.id = wh.product_id where gd.id = ? or gd.name = ?";
    public static final String COUNT_ALL_GOODS = "SELECT COUNT(*) as quantity FROM goods";
    public static final String SELECT_GOODS_WITH_LIMIT = "SELECT * FROM goods gd join warehouse wh on gd.id = wh.product_id ORDER BY name ASC LIMIT ? offset ?";

    public static final String FIND_USER_BY_NAME = "select * from users where name=? AND pass_hash=?";
}
