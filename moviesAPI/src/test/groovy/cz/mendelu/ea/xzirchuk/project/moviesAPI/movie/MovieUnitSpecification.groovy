package cz.mendelu.ea.xzirchuk.project.moviesAPI.movie


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.CommonSpecification
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import spock.lang.Specification

import java.time.LocalDate

class MovieUnitSpecification extends  CommonSpecification {




    def "Creating a movie should create a movie"(){
        expect:
            Movie movie = super.mock.movie
            Director director = super.mock.director

            movie.getTitle() == "Test"
            movie.getReleaseYear() == LocalDate.of(2012, 5, 23)
            movie.getMetaScore() == 12
            movie.getCertificate() == "M"
            movie.getRuntime() == 123
            movie.getRevenue() == 2.2f
            movie.getGenre() == "Action"
            movie.getImdbRating() == 9.2f
            movie.getDirector() == director

    }
}
