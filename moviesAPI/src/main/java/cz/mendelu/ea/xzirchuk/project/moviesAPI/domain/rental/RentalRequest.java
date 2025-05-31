package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user.UserService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
public class RentalRequest {


        private LocalDate rentalStartDate;

        private LocalDate expectedMovieReturnDate;

        private LocalDate actualMovieReturnDate;

        private UUID userId;

        private UUID movieId;

        public void toRental(Rental rental, MovieService movieService, UserService userService){
            rental.setRentalStartDate(rentalStartDate);
            rental.setExpectedReturnDate(expectedMovieReturnDate);
            rental.setActualReturnDate(actualMovieReturnDate);
            rental.setUser(userService.getUserById(userId).orElseThrow(NotFoundException::new));
            rental.setMovie(movieService.getMovieById(movieId).orElseThrow(NotFoundException::new));


        }


    }

