package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class dataDownloadFromAPI {

    private final MovieService movieService;
    private final DirectorService directorService;
    private final WebClient webClient;

    public void downloadDataFromAPI(){
        WebClient.ResponseSpec response = null;
        Mono<FilmResponse> data = null;
        try{
            data =  webClient.get()
                    .uri("/films")
                    .retrieve()
                    .bodyToMono(FilmResponse.class);
        }catch (WebClientException ex) {
            log.error("failed to retrieve web resource : {}", ex.getMessage());
        }


        FilmResponse actualClass = data.block();
        if (actualClass == null) {
            log.error("failed to retrieve data from web resource");
            return;
        }

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

               director = directorService.createDirector(director);
           }

            insertMovie.setDirector(director);

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

           try{
                movieService.createMovie(insertMovie);
            }catch (NonUniqueResultException ex){
               log.error("Could not create Movie: Unique constraint prevent creation");
           }


        }
    }
    @Scheduled(cron = "0 0 * * * ?")
    public void downloadData() {
        if (movieService.getMoviePage(0,10).isEmpty()){
            downloadDataFromAPI();
        }else{
            log.info("DATABASE IS NOT EMPTY, CONTIUNING WITHOUT API DOWNLOAD");
        }
    }
}
