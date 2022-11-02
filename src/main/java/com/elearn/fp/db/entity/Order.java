package com.elearn.fp.db.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long id;
    private long cashierId;
    private Timestamp datetime;
    private List<ItemDTO> orderItems = new ArrayList<ItemDTO>();

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

    public List<ItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
