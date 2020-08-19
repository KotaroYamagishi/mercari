package com.mercari.form;

public class ItemSearchForm {
    private String name;
    private String parentCategory;
    private String childCategory;
    private String grandChildCategory;
    private String brand;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(String childCategory) {
        this.childCategory = childCategory;
    }

    public String getGrandChildCategory() {
        return grandChildCategory;
    }

    public void setGrandChildCategory(String grandChildCategory) {
        this.grandChildCategory = grandChildCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    
}