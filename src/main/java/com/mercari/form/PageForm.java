package com.mercari.form;

import javax.validation.constraints.Pattern;

public class PageForm {
    @Pattern(regexp = "^[0-9]+$",message = "数字を入力してください")
    private String page;
    private String name;
    private String parentCategory;
    private String childCategory;
    private String grandChildCategory;
    private String brandName;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    

    
}