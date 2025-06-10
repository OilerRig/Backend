package com.oilerrig.backend.domain;

public class Vendor {
    private int id;
    private String name;
    private String baseurl;
    private String apikey;


    // business logic and helpers
    public boolean isValid() {
        return id >= 0 && name != null && baseurl != null;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
