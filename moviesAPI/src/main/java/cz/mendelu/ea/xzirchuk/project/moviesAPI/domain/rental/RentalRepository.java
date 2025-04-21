package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RentalRepository extends CrudRepository<Rental, UUID> {
    public Optional<Rental> findRentalByMovieIdAndActualReturnDateIsNull(UUID movieId);
}
