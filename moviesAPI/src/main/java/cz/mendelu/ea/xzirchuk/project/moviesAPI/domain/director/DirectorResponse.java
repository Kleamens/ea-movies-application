package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DirectorResponse {
    @Id
    private UUID id;

    @NotEmpty
    @NotNull
    private String name;


    @Min(1)
    @NotNull
    private Float net_worth;


    private List<Movie> movies;

    public DirectorResponse(Director director){
        this.id = director.getId();
        this.name = director.getName();
        this.net_worth = director.getNet_worth();
        this.movies = director.getMovieList();
    }
}
