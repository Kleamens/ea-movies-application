package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RentalsResponse {
    List<RentalResponse> rentals;

    RentalsResponse(List<Rental> rental) {
        this.rentals = rental.stream().map(RentalResponse::new).collect(Collectors.toList());

    }
}
