package com.elearn.fp.db.entity;

import javax.naming.Context;
import javax.servlet.ServletContext;
import java.util.Objects;

/**
 * POJO class which represent product in the store
 */

public class Item {

    private long productID;
    private String productName;
    private String productDescription;
    private int productQuantity;
    private int productUnitId;
    private double productPrice;
    private String productUnit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return productUnitId == item.productUnitId && Objects.equals(productName, item.productName) && Objects.equals(productDescription, item.productDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, productDescription, productQuantity, productUnitId, productPrice);
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public Item(String productName, String productDescription, int productQuantity, int productUnitId, double productPrice) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productUnitId = productUnitId;
        this.productPrice = productPrice;
    }

    public Item() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(int productUnitId) {
        this.productUnitId = productUnitId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    @Override
    public String toString() {
        return productName +
                " : " + productQuantity + productUnit +
                ", product price : " + productPrice + "$.";
    }
}
