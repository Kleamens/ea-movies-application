package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MovieRepository extends CrudRepository<Movie, UUID> {
    List<Movie> getMoviesByDirector_Name(String name);
}
