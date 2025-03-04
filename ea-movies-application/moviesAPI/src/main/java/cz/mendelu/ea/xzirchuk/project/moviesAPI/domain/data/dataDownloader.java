package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import com.opencsv.CSVReader;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;


@Service
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class dataDownloader {


    private final DirectorService directorService;

    private final MovieService movieService;

    @Autowired
    public dataDownloader(MovieService movieService,DirectorService directorService){
        this.directorService = directorService;
        this.movieService = movieService;
    }



    public void downloadData(){
        String filename ="C:\\Users\\Person\\Desktop\\ea_git\\ea_ls2023-2024_xzirchuk\\moviesAPI\\src\\main\\java\\cz\\mendelu\\ea\\xzirchuk\\project\\moviesAPI\\domain\\data\\imdb_top_1000.csv";

        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(filename);

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            boolean isFirstRecord = true;
            Random generator = new Random();
            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {

                if(isFirstRecord){
                    isFirstRecord=false;
                    continue;
                }
                Movie insertMovie = new Movie();
                Director director = new Director();
                List<String> params = new ArrayList<String>();
                for (int i = 1; i<nextRecord.length; i++) {
                    params.add(nextRecord[i]);
                }
                System.out.println(params);
                insertMovie.setTitle(params.get(0));
                insertMovie.setReleaseYear(
                        LocalDate.ofYearDay(
                                Integer.parseInt(
                                        params.get(1)
                                ),
                                1)
                );
                insertMovie.setCertificate(
                        ((params.get(2).isEmpty())? "Unknown" : params.get(2))
                );
                insertMovie.setRuntime(Integer.parseInt(
                    params.get(3).split(" ")[0]
                 )
                );
                insertMovie.setGenre(params.get(4)
                        .replace(" ",""));
                insertMovie.setImdbRating(Float.parseFloat(
                        params.get(5)
                )
                );
                insertMovie.setOverview(params.get(6));
                insertMovie.setMetaScore(
                        (!params.get(7).isEmpty()? Integer.parseInt(params.get(7)): generator.nextInt(0,100)));
                if (directorService.findDirectorByName(params.get(8)).isPresent()){
                    director = directorService.findDirectorByName(params.get(8)).get();
                    System.out.println("##########"+directorService.findDirectorByName(params.get(8)).get());
                }else{
                    director.setName(params.get(8));
                    director.setNet_worth(generator.nextFloat(1f,10f));
                    directorService.createDirector(director);
                }

                String lastItem = params.get(params.size()-1);
                insertMovie.setRevenue(
                        ((!lastItem.isEmpty()) ? Float.parseFloat(params.get(params.size()-1).replace(",","")) : null
                        )
                );
                insertMovie.setLastModified(Instant.now());
                insertMovie.setDirector(director);

                movieService.createMovie(insertMovie);

                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void dow(){

//            downloadData();
    }


}
