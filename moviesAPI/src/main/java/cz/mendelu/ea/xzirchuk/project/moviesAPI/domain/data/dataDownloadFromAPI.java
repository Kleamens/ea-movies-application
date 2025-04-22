package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.NonUniqueResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class dataDownloadFromAPI {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MovieService movieService;
    private final DirectorService directorService;
    private final WebClient webClient;

    @Autowired
    public dataDownloadFromAPI(MovieService movieService, DirectorService directorService,WebClient.Builder webClientBuilder) {
        this.movieService = movieService;
        this.webClient = webClientBuilder.baseUrl("https://www.swapi.tech/api").build();
        this.directorService = directorService;
    }

    public void downloadDataFromAPI(){
        WebClient.ResponseSpec response = null;
        Mono<FilmResponse> data = null;
        try{
            response =  webClient.get()
                    .uri("/films")
                    .retrieve();
        }catch (WebClientException ex) {
            logger.error("failed to retrieve web resource : {}", ex.getMessage());

        }
        try {
           data = response.bodyToMono(FilmResponse.class);
        }catch (WebClientException ex) {
            logger.error("Transformation to Object failed {}", ex.getMessage());

        }

        FilmResponse actualClass = data.block();

        assert actualClass != null;
        
        for (FilmResult i : actualClass.getResult()){
           Movie insertMovie = new Movie();

           Optional<Director> optionalDirector = directorService.getDirectorByName(i.getProperties().getDirector());

           Director director;
           if (optionalDirector.isPresent()) {
               director = optionalDirector.get();
           } else {
               director = new Director();
               director.setName(i.getProperties().getDirector());
               director.setNet_worth(5.0f);

               directorService.createDirector(director);
           }



           insertMovie.setCertificate("E");
           insertMovie.setGenre("Sci-Fi");
           insertMovie.setOverview(i.getDescription());
           insertMovie.setRevenue(10.0f);
           insertMovie.setRuntime(100);
           insertMovie.setTitle(i.getProperties().getTitle());
           insertMovie.setImdbRating(8.0f);
           insertMovie.setReleaseYear(LocalDate.parse(i.getProperties().getRelease_date()));
           insertMovie.setMetaScore(89);
           insertMovie.setLastModified(Instant.parse(i.getProperties().getEdited()));


           movieService.createMovie(insertMovie);
            Optional<Movie> insertedMovie;
           try{
                insertedMovie = movieService.getMovieByTitle(insertMovie.getTitle());
           }catch (NonUniqueResultException ex){
               continue;
           }

           if(insertedMovie.isPresent()){
               insertedMovie.get().setDirector(director);
               movieService.updateMovie(insertedMovie.get().getId(), insertedMovie.get());
           }
        }
    }
    @PostConstruct
    public void dow() {
        if (movieService.getMoviePage(0,10).isEmpty()){
            downloadDataFromAPI();
        }else{
            logger.info("DATABASE IS NOT EMPTY, CONTIUNING WITHOUT API DOWNLOAD");
        }
    }
}
