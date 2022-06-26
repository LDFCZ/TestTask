package ru.nsu.ccfit.lopatkin.testTask.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private String userAgent;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "banner_id")
    private Banner bannerId;

    private String result;

    @Column(name = "DATE")
    private LocalDateTime dateTime;

    public Request(Banner banner, String userAgent, String ipAddress, LocalDateTime dateTime, String result) {
        this.bannerId = banner;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.dateTime = dateTime;
        this.result = result;
    }

    public Request() {

    }

    public Banner getBannerId() {
        return bannerId;
    }

    public void setBannerId(Banner banner) {
        this.bannerId = banner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getCreated() {
        return dateTime;
    }

    public void setCreated(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String condition) {
        this.result = condition;
    }
}
