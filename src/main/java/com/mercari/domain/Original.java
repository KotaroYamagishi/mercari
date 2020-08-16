package com.mercari.domain;

public class Original {
  private Integer id;
  private String name;
  private Integer condition;
  private String categoryName;
  private String brand;
  private Integer price;
  private Integer shipping;
  private String description;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCondition() {
    return this.condition;
  }

  public void setCondition(Integer condition) {
    this.condition = condition;
  }

  public String getBrand() {
    return this.brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Integer getPrice() {
    return this.price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getShipping() {
    return this.shipping;
  }

  public void setShipping(Integer shipping) {
    this.shipping = shipping;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCategoryName() {
    return this.categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

}
