package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MoviePaginationRepository;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieRepository;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    public List<Rental> getRentalPage(int pageNumber,int pageSize){
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = rentalPaginationRepository.findAll(pageRequest);
        return page.get().toList();

    }

    public Optional<Rental> getRentalbyId(UUID id){
        return rentalRepository.findById(id);
    }

    public Rental createRental(Rental rental){
        if(rentalRepository.findRentalByMovieIdAndActualReturnDateIsNull(rental.getMovie().getId()).isPresent()){
            throw new IllegalArgumentException("This movie is already rented");
        }
        else if (rental.getRentalStartDate().isAfter(rental.getExpectedReturnDate())){
            throw new IllegalArgumentException("Rental start date cannot be after finished date");
        }
        else{
            rental.setActualReturnDate(null);
            return rentalRepository.save(rental);
        }
    }

    public Rental updateRental(UUID id, Rental rental) {
        rental.setId(id);
        return rentalRepository.save(rental);
    }
    public void deleteRental(UUID id) {
        rentalRepository.deleteById(id);
    }

    public Boolean isMovieRented(Movie movie,MovieService movieService){
        return rentalRepository.findRentalByMovieIdAndActualReturnDateIsNull(movie.getId()).isPresent();
    }
    public Rental finishRental(Rental rental, LocalDate returnDate){
        Rental latestUnfinishedRental =  rentalRepository.findRentalByMovieIdAndActualReturnDateIsNull(rental.getMovie().getId()).orElseThrow(NotFoundException::new);
        if (returnDate.isBefore(rental.getRentalStartDate())){
            throw new IllegalArgumentException("Rental start date cannot be after finished date");
        }
        latestUnfinishedRental.setActualReturnDate(returnDate);
        rentalRepository.save(latestUnfinishedRental);
        return latestUnfinishedRental;
    }
    public Rental getLastUnfinishedRental(UUID movieId){
        Rental latestUnfinishedRental =  rentalRepository.findRentalByMovieIdAndActualReturnDateIsNull(movieId).orElse(null);
        return latestUnfinishedRental;
    }


}
