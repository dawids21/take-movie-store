package xyz.stasiak.takemoviestore;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("movies", movieService.getMovies(PageRequest.of(0, PAGE_SIZE)));
        model.addAttribute("total", movieService.getShoppingCartTotal());
        return "index";
    }

    @GetMapping("/rows")
    @HxRequest
    HtmxResponse moviesList(Model model, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            model.addAttribute("category", category);
            model.addAttribute("movies", movieService.getMoviesByCategory(category, PageRequest.of(page, PAGE_SIZE)));
        } else {
            model.addAttribute("movies", movieService.getMovies(PageRequest.of(page, PAGE_SIZE)));
        }
        model.addAttribute("page", page);
        return HtmxResponse.builder()
                .view("fragments/movies :: rows")
                .build();
    }

    @GetMapping("/{id}")
    String movie(Model model, @PathVariable long id) {
        Optional<MovieDto> movie = movieService.getMovie(id);
        if (movie.isEmpty()) {
            return "redirect:/movies";
        }
        model.addAttribute("movie", movie.get());
        model.addAttribute("total", movieService.getShoppingCartTotal());
        return "movie-details";
    }

    @PostMapping(value = "/cart/add/{id}")
    @HxRequest
    HtmxResponse addToCart(Model model, @PathVariable long id) {
        movieService.addMovieToShoppingCart(id);
        Optional<MovieDto> movie = movieService.getMovie(id);
        if (movie.isEmpty()) {
            throw new IllegalArgumentException("Movie not found");
        }
        model.addAttribute("movie", movie.get());
        model.addAttribute("total", movieService.getShoppingCartTotal());
        return HtmxResponse.builder()
                .view("fragments/movies :: movieCartButton")
                .view("fragments/cart :: cartButton")
                .build();
    }

    @PostMapping(value = "/cart/remove/{id}")
    @HxRequest
    HtmxResponse removeFromCart(Model model, @PathVariable long id) {
        movieService.removeMovieFromShoppingCart(id);
        Optional<MovieDto> movie = movieService.getMovie(id);
        if (movie.isEmpty()) {
            throw new IllegalArgumentException("Movie not found");
        }
        model.addAttribute("movie", movie.get());
        model.addAttribute("total", movieService.getShoppingCartTotal());
        return HtmxResponse.builder()
                .view("fragments/movies :: movieCartButton")
                .view("fragments/cart :: cartButton")
                .view("fragments/cart :: cartTotal")
                .build();
    }

    @GetMapping("/cart")
    @HxRequest
    HtmxResponse cart(Model model) {
        model.addAttribute("movies", movieService.getMoviesInShoppingCart());
        model.addAttribute("total", movieService.getShoppingCartTotal());
        return HtmxResponse.builder()
                .view("fragments/cart :: cartModalContent")
                .build();
    }

    @GetMapping("/cart/value")
    @HxRequest
    HtmxResponse cartValue(Model model) {
        model.addAttribute("total", movieService.getShoppingCartTotal());
        return HtmxResponse.builder()
                .view("fragments/cart :: cartButton")
                .build();
    }
}
