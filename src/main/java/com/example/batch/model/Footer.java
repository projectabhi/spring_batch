package com.example.batch.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Footer {
    @XmlElement(name = "fileSeq")
    private String fileSeq;

    @XmlElement(name = "recordCount")
    private Integer recordCount;

    public Footer() {}

    public Footer(String fileSeq, Integer recordCount) {
        this.fileSeq = fileSeq;
        this.recordCount = recordCount;
    }

    public String getFileSeq() { return fileSeq; }
    public void setFileSeq(String fileSeq) { this.fileSeq = fileSeq; }

    public Integer getRecordCount() { return recordCount; }
    public void setRecordCount(Integer recordCount) { this.recordCount = recordCount; }
}