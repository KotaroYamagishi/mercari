package com.mercari.domain;

import java.util.List;

public class Item {
  private Integer id;
  private String name;
  private Integer condition;
  private Integer category;
  private String brandName;
  private Integer brand;
  private Integer price;
  private Integer shipping;
  private String description;
  private List<Category> categoryList;
  private Brand brnadInfo;

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

  public Integer getCondition() {
    return condition;
  }

  public void setCondition(Integer condition) {
    this.condition = condition;
  }

  public Integer getCategory() {
    return category;
  }

  public void setCategory(Integer category) {
    this.category = category;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public Integer getBrand() {
    return brand;
  }

  public void setBrand(Integer brand) {
    this.brand = brand;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getShipping() {
    return shipping;
  }

  public void setShipping(Integer shipping) {
    this.shipping = shipping;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Category> getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  public Brand getBrnadInfo() {
    return brnadInfo;
  }

  public void setBrnadInfo(Brand brnadInfo) {
    this.brnadInfo = brnadInfo;
  }

  


}