package xyz.stasiak.takemoviestore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findAllByCategoryContaining(String category, Pageable pageable);
}
