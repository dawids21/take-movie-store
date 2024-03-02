package xyz.stasiak.takemoviestore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByCategory(String category);
}
