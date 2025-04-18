package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MovieRepository extends CrudRepository<Movie, UUID> {
}
