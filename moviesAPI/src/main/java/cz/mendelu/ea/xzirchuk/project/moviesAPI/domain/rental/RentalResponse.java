package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user.RentalUser;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class RentalResponse {
    @Id
    private UUID id;

    @NotNull
    LocalDate rentalStartDate;

    @NotNull
    LocalDate expectedMovieReturnDate;

    LocalDate actualMovieReturnDate;

    @NotNull
    RentalUser user;

    @NotNull
    Movie movie;

    public RentalResponse(Rental rental) {
        this.id = rental.getId();
        this.rentalStartDate = rental.getRentalStartDate();
        this.expectedMovieReturnDate = rental.getExpectedReturnDate();
        this.actualMovieReturnDate = rental.getActualReturnDate();
        this.user = rental.getUser();
        this.movie = rental.getMovie();
    }
}
