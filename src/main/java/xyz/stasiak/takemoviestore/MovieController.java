package xyz.stasiak.takemoviestore;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movies")
class MovieController {
    private static final int PAGE_SIZE = 25;
    private final MovieService movieService;

    @GetMapping
    String index(Model model) {
        model.addAttribute("page", 0);
        model.addAttribute("categories", movieService.getAllCategories());
        ShoppingCart shoppingCart = movieService.getShoppingCart();
        List<Movie> movies = movieService.getMovies(PageRequest.of(0, PAGE_SIZE)).getContent();
        model.addAttribute("movies", toMovieDtos(movies, shoppingCart));
        model.addAttribute("total", shoppingCart.getTotalPrice());
        return "index";
    }

    @GetMapping("/rows")
    @HxRequest
    String moviesList(Model model, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false) String category) {
        ShoppingCart shoppingCart = movieService.getShoppingCart();
        if (category != null && !category.isBlank()) {
            model.addAttribute("category", category);
            List<Movie> movies = movieService.getMoviesByCategory(category, PageRequest.of(page, PAGE_SIZE)).getContent();
            model.addAttribute("movies", toMovieDtos(movies, shoppingCart));
        } else {
            List<Movie> movies = movieService.getMovies(PageRequest.of(page, PAGE_SIZE)).getContent();
            model.addAttribute("movies", toMovieDtos(movies, shoppingCart));
        }
        model.addAttribute("page", page);
        return "fragments/movies :: rows";
    }

    @GetMapping("/{id}")
    String movie(Model model, @PathVariable long id) {
        Optional<Movie> movie = movieService.getMovie(id);
        if (movie.isEmpty()) {
            return "redirect:/movies";
        }
        ShoppingCart shoppingCart = movieService.getShoppingCart();
        model.addAttribute("movie", toMovieDto(movie.get(), shoppingCart));
        model.addAttribute("total", shoppingCart.getTotalPrice());
        return "movie-details";
    }

    @PostMapping(value = "/cart/add/{id}")
    @HxRequest
    HtmxResponse addToCart(Model model, @PathVariable long id) {
        ShoppingCart shoppingCart = movieService.addMovieToShoppingCart(id);
        Optional<Movie> movie = movieService.getMovie(id);
        if (movie.isEmpty()) {
            throw new IllegalArgumentException("Movie not found");
        }
        model.addAttribute("movie", toMovieDto(movie.get(), shoppingCart));
        model.addAttribute("total", shoppingCart.getTotalPrice());
        return HtmxResponse.builder()
                .view("fragments/movies :: movieCartButton")
                .view("fragments/cart :: cartButton")
                .build();
    }

    @PostMapping(value = "/cart/remove/{id}")
    @HxRequest
    HtmxResponse removeFromCart(Model model, @PathVariable long id) {
        ShoppingCart shoppingCart = movieService.removeMovieFromShoppingCart(id);
        Optional<Movie> movie = movieService.getMovie(id);
        if (movie.isEmpty()) {
            throw new IllegalArgumentException("Movie not found");
        }
        model.addAttribute("movie", toMovieDto(movie.get(), shoppingCart));
        model.addAttribute("total", shoppingCart.getTotalPrice());
        return HtmxResponse.builder()
                .view("fragments/movies :: movieCartButton")
                .view("fragments/cart :: cartButton")
                .build();
    }

    @GetMapping("/cart")
    @HxRequest
    String cart(Model model) {
        ShoppingCart shoppingCart = movieService.getShoppingCart();
        model.addAttribute("movies", toMovieDtos(movieService.getMoviesInShoppingCart(), shoppingCart));
        return "fragments/cart :: cartModalContent";
    }

    @GetMapping("/cart/value")
    @HxRequest
    String cartValue(Model model) {
        ShoppingCart shoppingCart = movieService.getShoppingCart();
        model.addAttribute("total", shoppingCart.getTotalPrice());
        return "fragments/cart :: cartButton";
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
