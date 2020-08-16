package com.mercari.domain;

public class Category {
  private Integer id;
  private Integer parent;
  private String name;
  private String nameAll;
  private Integer depth;

  public Integer getDepth() {
    return this.depth;
  }

  public void setDepth(Integer depth) {
    this.depth = depth;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getParent() {
    return this.parent;
  }

  public void setParent(Integer parent) {
    this.parent = parent;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameAll() {
    return this.nameAll;
  }

  public void setNameAll(String nameAll) {
    this.nameAll = nameAll;
  }

}