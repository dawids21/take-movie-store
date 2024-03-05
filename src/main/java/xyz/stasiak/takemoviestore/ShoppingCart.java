package xyz.stasiak.takemoviestore;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ElementCollection
    private Set<Long> movies;
    private int totalPrice;

    protected ShoppingCart() {
    }

    void addMovie(Movie movie) {
        movies.add(movie.getId());
        totalPrice += movie.getPrice();
    }

    void removeMovie(Movie movie) {
        movies.remove(movie.getId());
        totalPrice -= movie.getPrice();
    }
}
