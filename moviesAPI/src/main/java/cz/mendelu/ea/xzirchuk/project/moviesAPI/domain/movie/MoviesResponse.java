package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
class MoviesResponse {
    List<MovieResponse> movies;

    MoviesResponse(List<Movie> movies) {
        this.movies = movies.stream().map(MovieResponse::new).collect(Collectors.toList());

    }
}

