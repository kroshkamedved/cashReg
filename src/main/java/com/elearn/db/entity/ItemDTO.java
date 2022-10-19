package com.elearn.db.entity;

import java.util.Objects;

public class ItemDTO extends AbstractEntity {

    private long productID;
    private String productName;
    private String productDescription;
    private int productQuantity;
    private int productUnitId;
    private int productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDTO)) return false;
        ItemDTO item = (ItemDTO) o;
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

    public ItemDTO(String productName, String productDescription, int productQuantity, int productUnitId, int productPrice) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productUnitId = productUnitId;
        this.productPrice = productPrice;
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}