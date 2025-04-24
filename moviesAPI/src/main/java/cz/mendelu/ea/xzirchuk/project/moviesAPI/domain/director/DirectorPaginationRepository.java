package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DirectorPaginationRepository extends PagingAndSortingRepository<Director, UUID>{

}
