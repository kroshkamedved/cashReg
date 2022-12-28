package com.elearn.fp.db.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * POJO class which represent order in the store
 */
public class Order {
    private long id;
    private long cashierId;
    private Timestamp datetime;
    private List<Item> orderItems = new ArrayList<Item>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCashierId() {
        return cashierId;
    }

    public void setCashierId(long cashierId) {
        this.cashierId = cashierId;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Item> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order#" + id +
                ", cashier ID#" + cashierId + ", datetime : " + datetime + ", order items :";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && cashierId == order.cashierId && datetime.equals(order.datetime) && orderItems.equals(order.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cashierId, datetime, orderItems);
    }
}
