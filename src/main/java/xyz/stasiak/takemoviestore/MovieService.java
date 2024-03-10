package xyz.stasiak.takemoviestore;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class MovieService {
    private final MovieRepository movieRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    Page<Movie> getMovies(PageRequest pageRequest) {
        return movieRepository.findAll(pageRequest);
    }

    Page<Movie> getMoviesByCategory(String category, PageRequest pageRequest) {
        return movieRepository.findAllByCategoryContaining(category, pageRequest);
    }

    Optional<Movie> getMovie(long id) {
        return movieRepository.findById(id);
    }

    List<String> getAllCategories() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .flatMap(movie -> movie.getCategories().stream())
                .distinct()
                .toList();
    }

    ShoppingCart getShoppingCart() {
        return shoppingCartRepository.find();
    }

    ShoppingCart addMovieToShoppingCart(long movieId) {
        ShoppingCart shoppingCart = shoppingCartRepository.find();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        shoppingCart.addMovie(movie);
        return shoppingCartRepository.save(shoppingCart);
    }

    ShoppingCart removeMovieFromShoppingCart(long movieId) {
        ShoppingCart shoppingCart = shoppingCartRepository.find();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        shoppingCart.removeMovie(movie);
        return shoppingCartRepository.save(shoppingCart);
    }

    List<Movie> getMoviesInShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartRepository.find();
        return movieRepository.findAllById(shoppingCart.getMovies());
    }
}
