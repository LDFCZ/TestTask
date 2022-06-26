package ru.nsu.ccfit.lopatkin.testTask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import ru.nsu.ccfit.lopatkin.testTask.utils.validators.IsPrice;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "banners")
public class Banner implements Serializable, Comparator<Banner> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be empty")
    @Length(max = 255, message = "Name length must be < 255 characters")
    private String name;

    @NotBlank(message = "Content must not be empty")
    private String text;

    @NotNull
    @Positive(message = "Price must be >0")
    @IsPrice(message = "price must be between 0.01 and 99999.99")
    private double price;

    private boolean deleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "banners_categories",
            joinColumns = {
                @JoinColumn(name = "banner_id", referencedColumnName = "id",
                    nullable = false, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "category_id", referencedColumnName = "id",
                nullable = false, updatable = false)})
    private Set<Category> categories = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "bannerId")
    private List<Request> requests;

    public Banner() {

    }

    public Banner(String name, double price, Set<Category> categories, String text) {
        this.name = name;
        this.text = text;
        this.price = price;
        this.deleted = false;
        this.categories=categories;
    }


    public void addCategory(Category category) {
        this.categories.add(category);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    @Override
    public int compare(Banner o1, Banner o2) {
        if (o1.getId().equals(o2.getId())) {
            return 0;
        }
        else if (o1.getPrice() > o2.getPrice()) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
