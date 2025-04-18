package cz.mendelu.ea.xzirchuk.project.moviesAPI.director

import cz.mendelu.ea.xzirchuk.project.moviesAPI.MoviesApiApplication
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

class DirectorUnitSpecification extends  Specification {




    def "Creating a director should create a director"(){
        given:
            Director director = new Director(
                    name: "Jeff",
                    net_worth: 2.0,
                    movieList: []
            )
        when:
            String directorName = director.getName()
            Float directorNetWorth = director.getNet_worth()
            List<Movie> directorFilmography = director.getMovieList()
        then:
            directorName=="Jeff"
            directorNetWorth==2.0
            directorFilmography==[]
    }
}
