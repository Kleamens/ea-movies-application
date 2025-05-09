package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;


import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @NotNull
    private String title;

    @NotNull
    private LocalDate releaseYear;

    @NotNull
    @EqualsAndHashCode.Exclude
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "director_id")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    Director director;



}
