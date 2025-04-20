package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MoviePaginationRepository;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieRepository;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
    private final Logger logger = LoggerFactory.getLogger(RentalService.class);
    private final RentalRepository rentalRepository;
    private final RentalPaginationRepository rentalPaginationRepository;

    public RentalService(RentalRepository rentalRepository,
                        RentalPaginationRepository rentalPaginationRepository)
    {

        this.rentalRepository= rentalRepository;
        this.rentalPaginationRepository = rentalPaginationRepository;
    }


}
