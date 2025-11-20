package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "detail")
public class Detail {
    @XmlElement(name = "seq")
    private String seq;

    @XmlElement(name = "firstName")
    private String firstName;

    @XmlElement(name = "lastName")
    private String lastName;

    @XmlElement(name = "department")
    private String department;

    public Detail() { }

    public Detail(String seq, String firstName, String lastName, String department) {
        this.seq = seq;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    public String getSeq() { return seq; }
    public void setSeq(String seq) { this.seq = seq; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}