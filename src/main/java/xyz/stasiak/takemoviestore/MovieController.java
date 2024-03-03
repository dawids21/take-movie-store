package xyz.stasiak.takemoviestore;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
class MovieController {
    private static final int PAGE_SIZE = 25;
    private final MovieService movieService;

    @GetMapping
    String index(Model model) {
        model.addAttribute("movies", movieService.getMovies(PageRequest.of(0, PAGE_SIZE)).getContent());
        model.addAttribute("page", 0);
        model.addAttribute("categories", movieService.getAllCategories());
        return "index";
    }

    @GetMapping("/movies")
    @HxRequest
    String moviesList(Model model, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            model.addAttribute("category", category);
            model.addAttribute("movies", movieService.getMoviesByCategory(category, PageRequest.of(page, PAGE_SIZE)).getContent());
        } else {
            model.addAttribute("movies", movieService.getMovies(PageRequest.of(page, PAGE_SIZE)).getContent());
        }
        model.addAttribute("page", page);
        return "movies :: rows";
    }
}
