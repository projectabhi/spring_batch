package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "orderFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderFile {

    @XmlElement(name = "header")
    private OrderHeader header;

    @XmlElementWrapper(name = "details")
    @XmlElement(name = "detail")
    private List<OrderDetail> details = new ArrayList<>();

    @XmlElement(name = "footer")
    private OrderFooter footer;

    public OrderFile() {}

    public OrderFile(OrderHeader header, List<OrderDetail> details, OrderFooter footer) {
        this.header = header;
        this.details = details;
        this.footer = footer;
    }

    public OrderHeader getHeader() { return header; }
    public void setHeader(OrderHeader header) { this.header = header; }

    public List<OrderDetail> getDetails() { return details; }
    public void setDetails(List<OrderDetail> details) { this.details = details; }

    public OrderFooter getFooter() { return footer; }
    public void setFooter(OrderFooter footer) { this.footer = footer; }
}