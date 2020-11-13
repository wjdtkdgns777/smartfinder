package com.example.smartfinder;

public class Dictionary {

    private String name;
    private String phone;
    private String category;
    private String url;

    public String getName2() {
        return name;
    }

    public void setName2(String name) {
        this.name = name;
    }

    public String getPhone2() {
        return phone;
    }

    public void setPhone2(String phone) {
        this.phone = phone;
    }

    public String getCategory2() {
        return category;
    }

    public void setCategory2(String category) {
        this.category = category;
    }

    public String getUrl2() {
        return url;
    }

    public void setUrl2(String url) {
        this.url = url;
    }

    public Dictionary(String name, String phone, String category, String url) {
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.url = url;
    }
}
