package com.mercari.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ItemAddForm {
    
    @NotBlank(message = "名前を入力してください")
    private String name;
    @Pattern(regexp = "^[0-9]+$",message = "金額を入力してください")
    private String price;
    @NotBlank(message = "カテゴリーを選択してください")
    private String parentCategory;
    @NotBlank(message = "カテゴリーを選択してください")
    private String childCategory;
    @NotBlank(message = "カテゴリーを選択してください")
    private String grandChildCategory;
    private String brand;
    @NotBlank(message = "状態を選択してください")
    private String condition;
    @NotBlank(message="説明文を記載してください")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}