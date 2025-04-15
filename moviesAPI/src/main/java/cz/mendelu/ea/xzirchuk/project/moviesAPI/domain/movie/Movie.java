package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;


import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Movie {

    @Id
    @HashCodeExclude
    @EqualsExclude
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @NotNull
    private String title;

    @NotNull
    private LocalDate releaseYear;

    @NotNull
    private Instant lastModified;

    @NotEmpty
    @NotNull
    private String overview;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer metaScore;

    @NotEmpty
    @NotNull
    private String certificate;

    @Min(5)
    @NotNull
    private Integer runtime;

    @NotEmpty
    @NotNull
    private String genre;

    @Min(0)
    @NotNull
    private Float imdbRating;

    @Min(0)

    private Float revenue;

    @ManyToOne()
    @JoinColumn(name = "director_id")
    @ToString.Exclude
    @JsonIgnore
    Director director;



}
