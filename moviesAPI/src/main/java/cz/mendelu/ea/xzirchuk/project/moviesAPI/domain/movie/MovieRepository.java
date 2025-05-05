package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface MovieRepository extends CrudRepository<Movie, UUID> {
    Optional<Movie> getMovieByTitle(String title);
    List<Movie> getMoviesByDirector_Name(String name);
}
