package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Header {
    @XmlElement(name = "fileSeq")
    private String fileSeq;

    @XmlElement(name = "date") // ddMMyyyy as-is
    private String date;

    @XmlElement(name = "fileName")
    private String fileName;

    public Header() {}

    public Header(String fileSeq, String date, String fileName) {
        this.fileSeq = fileSeq;
        this.date = date;
        this.fileName = fileName;
    }

    public String getFileSeq() { return fileSeq; }
    public void setFileSeq(String fileSeq) { this.fileSeq = fileSeq; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}