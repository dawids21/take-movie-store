package xyz.stasiak.takemoviestore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

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

    public Movie(String title, String category, int yearOfRelease, String plotDescription, int price) {
        this.title = title;
        this.category = category;
        this.yearOfRelease = yearOfRelease;
        this.plotDescription = plotDescription;
        this.price = price;
    }
}
