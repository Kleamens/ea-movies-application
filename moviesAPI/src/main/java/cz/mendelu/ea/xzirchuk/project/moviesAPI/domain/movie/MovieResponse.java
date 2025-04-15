package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
@Data
@AllArgsConstructor
public class MovieResponse {
    @Id

    private UUID id;

    @NotEmpty

    private String title;

    @NotNull
    private LocalDate releaseYear;

    @NotNull
    private Instant lastModified;

    @NotEmpty
    private String overview;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer metaScore;

    @NotEmpty
    private String certificate;

    @Min(5)
    @NotNull
    private Integer runtime;

    @NotEmpty
    private String genre;

    @Min(0)
    @NotNull
    private Float imdbRating;

    @Min(0)
    private Float revenue;
    @NotNull
    private Director director;

    public MovieResponse(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.releaseYear = movie.getReleaseYear();
        this.lastModified = movie.getLastModified();
        this.overview = movie.getOverview();
        this.metaScore = movie.getMetaScore();
        this.certificate = movie.getCertificate();
        this.runtime = movie.getRuntime();
        this.genre = movie.getGenre();
        this.imdbRating = movie.getImdbRating();
        this.revenue = movie.getRevenue();
        this.director = movie.getDirector();

    }
}

