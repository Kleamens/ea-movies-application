package cz.mendelu.ea.xzirchuk.project.moviesAPI.movie


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import spock.lang.Specification

import java.time.LocalDate

class MovieUnitSpecification extends  Specification {




    def "Creating a movie should create a movie"(){
        given:
            Movie movie = Mocks.movie
            Director director = Mocks.director
        when:
            String title = movie.getTitle()
            LocalDate releaseYear = movie.getReleaseYear()
            String overview = movie.getOverview()
            int metaScore = movie.getMetaScore()
            String certificate = movie.getCertificate()
            int runtime = movie.getRuntime()
            Float revenue = movie.getRevenue()
            String genre = movie.getGenre()
            Float imdbRating = movie.getImdbRating()
            Director director_test = movie.getDirector()
        then:

            title == "Test"
            releaseYear == LocalDate.of(2012, 5, 23)
            overview == "Test Overview"
            metaScore == 12
            certificate == "M"
            runtime == 123
            revenue == 2.2f
            genre == "Action"
            imdbRating == 9.2f
            director_test == director

    }
}
