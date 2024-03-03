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
}
