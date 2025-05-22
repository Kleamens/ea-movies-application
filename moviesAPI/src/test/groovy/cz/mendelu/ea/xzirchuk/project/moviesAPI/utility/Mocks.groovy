package cz.mendelu.ea.xzirchuk.project.moviesAPI.utility

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie

import java.time.Instant
import java.time.LocalDate


class Mocks {
    private final Director director = new Director(
            name: "Jeff",
            net_worth: 2.0,
    )

    private final Movie movie = new Movie(
            title: "Test",
            releaseYear: LocalDate.of(2012,5,23),
            overview: "Test Overview",
            metaScore: 12,
            lastModified: Instant.now(),
            certificate: "M",
            runtime: 123,
            genre: "Action",
            imdbRating: 9.2,
            revenue: 2.2,
            director:director,
    )

    Director getDirector() {
        return director
    }

    Movie getMovie() {
        return movie
    }
}
