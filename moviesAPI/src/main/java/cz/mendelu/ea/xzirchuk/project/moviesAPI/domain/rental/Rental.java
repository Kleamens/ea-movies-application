package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user.RentalUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.time.LocalDate;
import java.util.UUID;
//This a basic implementation of the rental system, for greter robustnes obviously the better solution is creating a User entity
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rental {

    @Id
    @EqualsExclude
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    LocalDate rentalStartDate;

    @NotNull
    LocalDate expectedReturnDate;


    LocalDate actualReturnDate = null;


    @ManyToOne()
    @JoinColumn(name = "movie_id")
    Movie movie;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    RentalUser user;

}
