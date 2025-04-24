package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface MoviePaginationRepository extends PagingAndSortingRepository<Movie, UUID>{

}
