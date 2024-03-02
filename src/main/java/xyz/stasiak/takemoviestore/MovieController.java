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
    String index(Model model, @RequestParam(required = false, defaultValue = "0") int page) {
        model.addAttribute("movies", movieService.getMovies(PageRequest.of(0, PAGE_SIZE)).getContent());
        model.addAttribute("page", 0);
        return "index";
    }

    @GetMapping
    @HxRequest
    String moviesList(Model model, @RequestParam(required = false, defaultValue = "0") int page) {
        model.addAttribute("movies", movieService.getMovies(PageRequest.of(page, PAGE_SIZE)).getContent());
        model.addAttribute("page", page);
        return "movies :: list";
    }
}
