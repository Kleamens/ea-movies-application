package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MovieRequest {


    @NotEmpty
    @Schema(description = "Title name of the movie.", example = "The Alien")
    private String title;

    @NotNull
    @Schema(description = "The year movie was released in YYYY-MM-DD", example = "1993-01-01")

    private String releaseYear;


    @NotEmpty
    @NotNull
    @Schema(description = "Short synopsis of what happens in the movie.", example = "An extraterrestrial lifeform finds its way onto a colonist spaceship, hijinks ensue idk")

    private String overview;

    @NotNull
    @Min(0)
    @Max(100)
    @Schema(description = "The score the movie got on the metacritic 0-100 int", example = "95")

    private Integer metaScore;

    @NotEmpty
    @Schema(description = "The certificate the movie got", example = "U")

    private String certificate;

    @Min(5)
    @NotNull
    @Schema(description = "The chronological length of the movie in minutes", example = "120")

    private Integer runtime;

    @NotEmpty
    @NotNull
    @Schema(description = "The genres applicable to the movie in form of:GENRE,GENRE,GENRE...", example = "Thriller,Sci-fi")

    private String genre;

    @Min(0)
    @NotNull
    @Schema(description = "The rating the movie got on IMDB 0-10 float", example ="9.3")

    private Float imdbRating;

    @Min(0)
    @NotNull
    @Schema(description = "The box office revenue of the movie in the cinemas", example ="9.3")

    private Float revenue;
    @NotNull
    @Schema(description = "The id of the director who made the movie, director must firstly be created before", example ="idk")

    private UUID director_id;

    public void toMovie(Movie movie, DirectorService directorService){
        movie.setTitle(title);
        movie.setReleaseYear(LocalDate.parse(releaseYear));
        movie.setLastModified(Instant.now());
        movie.setOverview(overview);
        movie.setMetaScore(metaScore);
        movie.setCertificate(certificate);
        movie.setRuntime(runtime);
        movie.setGenre(genre);
        movie.setImdbRating(imdbRating);
        movie.setRevenue(revenue);
        movie.setDirector(directorService.getDirectorById(director_id).orElseThrow(NotFoundException::new));
    }


}
