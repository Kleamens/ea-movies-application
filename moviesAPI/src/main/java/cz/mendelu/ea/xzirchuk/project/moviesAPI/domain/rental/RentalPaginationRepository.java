package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface RentalPaginationRepository extends PagingAndSortingRepository<Rental, UUID> {

}

