package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rental {

    @Id
    @HashCodeExclude
    @EqualsExclude
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    LocalDate rentalStartDate;

    @NotNull
    LocalDate expectedMovieReturnDate;

    @NotNull
    LocalDate actualMovieReturnDate;

    @ManyToOne()
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    Movie movie;

}
