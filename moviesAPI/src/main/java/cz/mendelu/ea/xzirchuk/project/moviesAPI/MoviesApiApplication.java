package cz.mendelu.ea.xzirchuk.project.moviesAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoviesApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(MoviesApiApplication.class, args);


	}

}
