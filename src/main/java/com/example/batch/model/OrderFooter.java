package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderFooter {
    @XmlElement(name = "seq")
    private String seq;

    @XmlElement(name = "count")
    private Integer count;

    public OrderFooter() {}

    public OrderFooter(String seq, Integer count) {
        this.seq = seq;
        this.count = count;
    }

    public String getSeq() { return seq; }
    public void setSeq(String seq) { this.seq = seq; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}