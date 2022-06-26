package ru.nsu.ccfit.lopatkin.testTask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name must not be empty")
    @Length(max = 255, message = "Name length must be < 255 characters")
    private String name;

    @NotBlank(message = "Request name must not be empty")
    @Length(max = 255, message = "Request name length must be < 255 characters")
    private String requestId;

    private boolean deleted;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Banner> banners = new HashSet<>();

    public Category() {

    }

    public Category(String name, String requestId) {
        this.name = name;
        this.requestId = requestId;
        this.deleted = false;
    }

    public void addBanner(Banner banner) {
        this.banners.add(banner);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Banner> getBanners() {
        return banners;
    }

    public void setBanners(Set<Banner> banners) {
        this.banners = banners;
    }
}
