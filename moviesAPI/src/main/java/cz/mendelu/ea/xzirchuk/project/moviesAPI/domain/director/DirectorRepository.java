package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DirectorRepository extends CrudRepository<Director, UUID> {
    public Optional<Director> findDirectorByNameEquals(String name);

    Optional<Director> getDirectorByName(@NotEmpty String name);
}
