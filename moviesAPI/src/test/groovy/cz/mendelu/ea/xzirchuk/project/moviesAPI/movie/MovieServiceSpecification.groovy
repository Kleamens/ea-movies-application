package cz.mendelu.ea.xzirchuk.project.moviesAPI.movie

import cz.mendelu.ea.xzirchuk.project.moviesAPI.MoviesApiApplication
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorContoller
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Mocks
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utility.Utils
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import spock.lang.Specification
import spock.lang.Subject

import javax.sql.DataSource

@SpringBootTest(classes = [MoviesApiApplication])
@ActiveProfiles("test")
@Slf4j
class MovieServiceSpecification extends Specification {
    @Autowired
    @Subject
    MovieService movieService;

    @Value('${spring.datasource.url}')
    String databaseUrl
    @Value('${spring.datasource.username}')
    String userName
    @Value('${spring.datasource.password}')
    String password

    String setupDataSQLScript ="/test-data/basedata.sql"
    String cleanupDataSQLScript = "/test-data/cleanup.sql"

    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create()


    ResourceDatabasePopulator databaseSetup = new ResourceDatabasePopulator()
    ResourceDatabasePopulator databaseCleanup = new ResourceDatabasePopulator()

    void setup(){
        databaseSetup.addScript(new ClassPathResource(setupDataSQLScript))
        databaseCleanup.addScript(new ClassPathResource(cleanupDataSQLScript))

        Utils.execPopulator(
                dataSourceBuilder,
                databaseUrl,
                userName,
                password,
                databaseSetup,
                "Setup failed"
        )
    }
//
    void cleanup(){
        Utils.execPopulator(
                dataSourceBuilder,
                databaseUrl,
                userName,
                password,
                databaseCleanup,
                "Cleanup failed"
        )
    }


    def "Should give a list of movies"(){
        given:
            List<Movie> movies= movieService.getAllMovies();
        when:
        int moviesListSize = movies.size()
        then:
            moviesListSize == 2
    }

    def "Should create a new movie"(){
        given:
            List<Movie> originalMovies = movieService.getAllMovies()
            int originalMoviesListSize = originalMovies.size()
            Movie movie = Mocks.movie

            movieService.createMovie(movie)
        when:
            List<Movie> newMovieList = movieService.getAllMovies()
            int moviesListSize = newMovieList.size()
        then:
        (moviesListSize == originalMoviesListSize+1)&&(newMovieList.contains(movie))
    }
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
}
