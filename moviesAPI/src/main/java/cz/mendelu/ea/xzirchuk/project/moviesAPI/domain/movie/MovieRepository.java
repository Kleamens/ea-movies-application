package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;
import java.util.Optional;
public interface MovieRepository extends CrudRepository<Movie, UUID> {
    Optional<Movie> getMovieByTitle(String title);
    List<Movie> getMoviesByDirector_Name(String name);
}
