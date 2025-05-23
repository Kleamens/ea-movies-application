package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import com.opencsv.CSVReader;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
public class DataDownloader {

    private final DirectorService directorService;

    private final MovieService movieService;

    private final int MOVIE_TITLE = 0;
    private final int MOVIE_YEAR = 1;
    private final int MOVIE_CERTIFICATE = 2;
    private final int MOVIE_RUNTIME = 3;
    private final int MOVIE_GENRE = 4;
    private final int MOVIE_IMDB_RATING = 5;
    private final int MOVIE_OVERVIEW = 6;
    private final int MOVIE_META_SCORE = 7;
    private final int DIRECTOR_NAME = 8;


    public void downloadData() {
        URL resourceRelativePath = DataDownloader.class.getResource("/data/imdb_top_1000.csv");

        if (resourceRelativePath == null) {
            log.error("CSV file was not found on path: {}", resourceRelativePath);
            return;
        }

        File CSVFile = new File(resourceRelativePath.getFile());



        // Create an object of filereader
        // class with CSV file as a parameter.
        FileReader filereader = null;
        try {
            filereader = new FileReader(CSVFile);
        } catch (FileNotFoundException f) {
            log.error("CSV file not found");
            return;
        }

        // create csvReader object passing
        // file reader as a parameter
        CSVReader csvReader = new CSVReader(filereader);
        String[] nextRecord;
        boolean isFirstRecord = true;
        Random generator = new Random();
        // we are going to read data line by line
        try {

            while ((nextRecord = csvReader.readNext()) != null) {

                if (isFirstRecord) {
                    isFirstRecord = false;
                    continue;
                }
                Movie insertMovie = new Movie();
                Director director = new Director();
                List<String> params = new ArrayList<String>();
                for (int i = 1; i < nextRecord.length; i++) {
                    params.add(nextRecord[i]);
                }
                log.debug("### Parameters ### {}", params);
                insertMovie.setTitle(params.get(MOVIE_TITLE));
                insertMovie.setReleaseYear(
                        LocalDate.ofYearDay(
                                Integer.parseInt(
                                        params.get(MOVIE_YEAR)
                                ),
                                1)
                );
                insertMovie.setCertificate(
                        ((params.get(MOVIE_CERTIFICATE).isEmpty()) ? "Unknown" : params.get(MOVIE_CERTIFICATE))
                );
                insertMovie.setRuntime(Integer.parseInt(
                                params.get(MOVIE_RUNTIME).split(" ")[0]
                        )
                );
                insertMovie.setGenre(params.get(MOVIE_GENRE)
                        .replace(" ", ""));
                insertMovie.setImdbRating(Float.parseFloat(
                                params.get(MOVIE_IMDB_RATING)
                        )
                );
                insertMovie.setOverview(params.get(MOVIE_OVERVIEW));
                insertMovie.setMetaScore(
                        (!params.get(MOVIE_META_SCORE).isEmpty() ? Integer.parseInt(params.get(MOVIE_META_SCORE)) : generator.nextInt(0, 100)));
                if (directorService.findDirectorByName(params.get(DIRECTOR_NAME)).isPresent()) {
                    director = directorService.findDirectorByName(params.get(DIRECTOR_NAME)).get();
                    log.info("### Director ### {}", director);
                } else {
                    director.setName(params.get(DIRECTOR_NAME));
                    director.setNet_worth(generator.nextFloat(1f, 10f));
                    directorService.createDirector(director);
                }

                String lastItem = params.get(params.size() - 1);
                insertMovie.setRevenue(
                        ((!lastItem.isEmpty()) ? Float.parseFloat(params.get(params.size() - 1).replace(",", "")) : null
                        )
                );
                insertMovie.setLastModified(Instant.now());
                insertMovie.setDirector(director);

                movieService.createMovie(insertMovie);

            }
        }catch (IOException i){
            log.error("Unable to read CSV file row");
        }


    }

    @PostConstruct
    public void dow() {

//        downloadData();
    }


}
