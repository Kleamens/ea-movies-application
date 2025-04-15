package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DirectorPaginationRepository extends PagingAndSortingRepository<Director, UUID>{

}
