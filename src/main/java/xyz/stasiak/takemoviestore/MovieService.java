package xyz.stasiak.takemoviestore;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class MovieService {
    private final MovieRepository movieRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    List<String> getAllCategories() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .flatMap(movie -> movie.getCategories().stream())
                .distinct()
                .toList();
    }

    List<MovieDto> getMovies(PageRequest pageRequest) {
        ShoppingCart shoppingCart = getShoppingCart();
        return toMovieDtos(movieRepository.findAll(pageRequest).getContent(), shoppingCart);
    }

    int getShoppingCartTotal() {
        ShoppingCart shoppingCart = getShoppingCart();
        return shoppingCart.getTotalPrice();
    }

    List<MovieDto> getMoviesByCategory(String category, PageRequest pageRequest) {
        ShoppingCart shoppingCart = getShoppingCart();
        return toMovieDtos(movieRepository.findAllByCategoryContaining(category, pageRequest).getContent(), shoppingCart);
    }

    Optional<MovieDto> getMovie(long id) {
        ShoppingCart shoppingCart = getShoppingCart();
        return movieRepository.findById(id)
                .map(movie -> toMovieDto(movie, shoppingCart));
    }

    void addMovieToShoppingCart(long movieId) {
        ShoppingCart shoppingCart = shoppingCartRepository.find();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        shoppingCart.addMovie(movie);
        shoppingCartRepository.save(shoppingCart);
    }

    void removeMovieFromShoppingCart(long movieId) {
        ShoppingCart shoppingCart = shoppingCartRepository.find();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        shoppingCart.removeMovie(movie);
        shoppingCartRepository.save(shoppingCart);
    }

    List<MovieDto> getMoviesInShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartRepository.find();
        return toMovieDtos(movieRepository.findAllById(shoppingCart.getMovies()), shoppingCart);
    }

    private ShoppingCart getShoppingCart() {
        return shoppingCartRepository.find();
    }

    private List<MovieDto> toMovieDtos(List<Movie> movies, ShoppingCart shoppingCart) {
        return movies.stream()
                .map(movie -> toMovieDto(movie, shoppingCart))
                .toList();
    }

    private MovieDto toMovieDto(Movie movie, ShoppingCart shoppingCart) {
        return MovieDto.from(movie, shoppingCart.getMovies().contains(movie.getId()));

    }
}
