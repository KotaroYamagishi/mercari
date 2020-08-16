package com.mercari.domain;

public class Relation {
  private Integer id;
  private Integer ancestorId;
  private Integer descendantId;
  private Integer depth;

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAncestorId() {
    return this.ancestorId;
  }

  public void setAncestorId(Integer ancestorId) {
    this.ancestorId = ancestorId;
  }

  public Integer getDescendantId() {
    return this.descendantId;
  }

  public void setDescendantId(Integer descendantId) {
    this.descendantId = descendantId;
  }

  public Integer getDepth() {
    return this.depth;
  }

  public void setDepth(Integer depth) {
    this.depth = depth;
  }

}