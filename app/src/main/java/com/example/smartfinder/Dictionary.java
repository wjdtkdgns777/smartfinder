package com.example.smartfinder;

public class Dictionary {

    private String name;//내 찜리스트 정보들
    private String phone;
    private String category;
    private String url;
    private String doc;

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getName2() {
        return name;
    }//정보 사용을 위해 작성

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

    public Dictionary(String name, String phone, String category, String url,String doc) {//생성자
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.url = url;
        this.doc = doc;
    }
}
