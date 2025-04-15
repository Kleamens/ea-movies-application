package cz.mendelu.ea.xzirchuk.project.moviesAPI;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data.dataDownloader;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviesApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(MoviesApiApplication.class, args);


	}

}
