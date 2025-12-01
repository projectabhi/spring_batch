package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "detail")
public class OrderDetail {
  @XmlElement(name = "seq")
  private String seq;

  @XmlElement(name = "item")
  private String item;

  @XmlElement(name = "amount")
  private Integer amount;

  @XmlElement(name = "precision")
  private Integer precision;

  @XmlElement(name = "quantity")
  private Integer quantity;

  public OrderDetail() {}

  public OrderDetail(String seq, String item, Integer amount, Integer precision, Integer quantity) {
    this.seq = seq;
    this.item = item;
    this.amount = amount;
    this.precision = precision;
    this.quantity = quantity;
  }

  public String getSeq() {
    return seq;
  }

  public void setSeq(String seq) {
    this.seq = seq;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public Integer getPrecision() {
    return precision;
  }

  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
