package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DirectorsResponse {

    List<DirectorResponse> directors;

    DirectorsResponse(List<Director> directors) {
        this.directors = directors.stream().map(DirectorResponse::new).collect(Collectors.toList());

    }
}
