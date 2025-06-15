package com.oilerrig.backend.data.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vendors", schema = "Broker")
public class VendorEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "baseurl", nullable = false, length = 100)
    private String baseurl;

    @Column(name = "apikey", nullable = false, length = 100)
    private String apikey;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VendorEntity vendor = (VendorEntity) o;
        return Objects.equals(getId(), vendor.getId()) && Objects.equals(getName(), vendor.getName()) && Objects.equals(getBaseurl(), vendor.getBaseurl()) && Objects.equals(getApikey(), vendor.getApikey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getBaseurl(), getProducts());
    }
}