package com.oilerrig.backend.data.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.oilerrig.backend.data.entity.ProductEntity}
 */
public class ProductEntityDto implements Serializable {
    private Integer id;
    private String vendorName;
    private String name;
    private Double price;
    private Integer stock;

    public ProductEntityDto() {
    }

    public ProductEntityDto(Integer id, String vendorName, String name, Double price, Integer stock) {
        this.id = id;
        this.vendorName = vendorName;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public ProductEntityDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getVendorName() {
        return vendorName;
    }

    public ProductEntityDto setVendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductEntityDto setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public ProductEntityDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public ProductEntityDto setStock(Integer stock) {
        this.stock = stock;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntityDto entity = (ProductEntityDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.vendorName, entity.vendorName) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.price, entity.price) &&
                Objects.equals(this.stock, entity.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendorName, name, price, stock);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "vendorName = " + vendorName + ", " +
                "name = " + name + ", " +
                "price = " + price + ", " +
                "stock = " + stock + ")";
    }
}