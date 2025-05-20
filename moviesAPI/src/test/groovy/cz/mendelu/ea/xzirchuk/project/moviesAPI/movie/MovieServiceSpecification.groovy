package cz.mendelu.ea.xzirchuk.project.moviesAPI.movie

import cz.mendelu.ea.xzirchuk.project.moviesAPI.MoviesApiApplication
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import groovy.util.logging.Slf4j
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject


@SpringBootTest(classes = [MoviesApiApplication])
@ActiveProfiles("test")
@Slf4j
class MovieServiceSpecification extends Specification {
    @Autowired
    @Subject
    private MovieService movieService;

    private Mocks mock

    def setup(){
        mock = new Mocks();
    }

    @Transactional
    def "Should delete a  movie"(){
        given:
        List<Movie> originalMovies = movieService.getAllMovies()
        int originalMoviesListSize = originalMovies.size()
        UUID idToDelete = UUID.fromString("07ab22d5-67c7-45ec-953a-17ca0e1a8bd6")
        movieService.deleteMovie(idToDelete)
        when:
        List<Movie> newMovieList = movieService.getAllMovies()
        int moviesListSize = newMovieList.size()
        then:
        (moviesListSize == originalMoviesListSize-1)&&(movieService.getMovieById(idToDelete).isEmpty())
    }
    @Transactional
    def "Should create a new movie"(){
        given:
            List<Movie> originalMovies = movieService.getAllMovies()
            int originalMoviesListSize = originalMovies.size()
            Movie movie = mock.getMovie()


            movieService.createMovie(movie)
        when:
            List<Movie> newMovieList = movieService.getAllMovies()
            int moviesListSize = newMovieList.size()
        then:
        (moviesListSize == originalMoviesListSize+1)&&(newMovieList.contains(movie))
    }
    def "Should give a list of movies"(){
        given:
        List<Movie> movies= movieService.getAllMovies();
        when:
        int moviesListSize = movies.size()
        then:
        moviesListSize == 2
    }

}
