package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderHeader {
    @XmlElement(name = "seq")
    private String seq;

    @XmlElement(name = "date")
    private String date;

    @XmlElement(name = "type")
    private String type;

    public OrderHeader() {}

    public OrderHeader(String seq, String date, String type) {
        this.seq = seq;
        this.date = date;
        this.type = type;
    }

    public String getSeq() { return seq; }
    public void setSeq(String seq) { this.seq = seq; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}