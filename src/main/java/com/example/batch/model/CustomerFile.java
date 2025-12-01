package com.example.batch.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "customerFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerFile {

  @XmlElement(name = "header")
  private Header header;

  @XmlElementWrapper(name = "details")
  @XmlElement(name = "detail")
  private List<Detail> details = new ArrayList<>();

  @XmlElement(name = "footer")
  private Footer footer;

  public CustomerFile() {}

  public CustomerFile(Header header, List<Detail> details, Footer footer) {
    this.header = header;
    this.details = details;
    this.footer = footer;
  }

  public Header getHeader() {
    return header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public List<Detail> getDetails() {
    return details;
  }

  public void setDetails(List<Detail> details) {
    this.details = details;
  }

  public Footer getFooter() {
    return footer;
  }

  public void setFooter(Footer footer) {
    this.footer = footer;
  }
}
