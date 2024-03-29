package xyz.stasiak.takemoviestore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String category;
    private int yearOfRelease;
    private String plotDescription;
    private int price;

    protected Movie() {
    }

    List<String> getCategories() {
        return Arrays.asList(category.split(", "));
    }
}
