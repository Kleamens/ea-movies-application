package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.Director;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;

public class MovieUnitTests {

    @Test
    public void doesGenreContainInvalidCorrectInput(){
        MovieService movieService = new MovieService(null,null);
        String genre = "Comedy,War,Drama";

        assert !movieService.doesGenreContainInvalid(genre);
    }

    @Test
    public void doesGenreContainInvalidWrongInput(){
        MovieService movieService = new MovieService(null,null);
        String genre = "Comedy,AVC";

        assert movieService.doesGenreContainInvalid(genre);
    }

    @Test
    public void doesGenreContainInvalidWrongNoSepInput(){
        MovieService movieService = new MovieService(null,null);
        String genre = "apdgj'alkj";

        assert movieService.doesGenreContainInvalid(genre);
    }

    @Test
    public void sortMoviesByFilter(){
        MovieService movieService = new MovieService(null,null);

        Movie movie = new Movie(
                UUID.randomUUID(), // Generate a random UUID for id
                "The Shawshank Redemption",
                LocalDate.of(1994, 10, 14), // Release date
                Instant.now(), // Set last modified to current time
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption in their friendship.",
                86, // Meta score
                "R", // Certificate
                142, // Runtime in minutes
                "Drama", // Genre
                9.3f, // IMDB rating
                1.0f, // Revenue (unknown)
                null // No director assigned yet
        );
        Movie movie2 = new Movie(
                UUID.randomUUID(),
                "The Godfather",
                LocalDate.of(1972, 3, 24),
                Instant.now(),
                "The story of the Corleone family under patriarch Vito Corleone, focusing on the transformation of his youngest son, Michael, from reluctant family outsider to ruthless mafia boss.",
                99,
                "R",
                175,
                "Crime",
                9.2f,
                2.0f,
                null);

        Movie movie3 = new Movie(
                UUID.randomUUID(),
                "The Dark Knight",
                LocalDate.of(2008, 07, 18),
                Instant.now(),
                "When the mentally challenged Joker wreaks havoc on Gotham, Batman must confront him first with his inner demons and then with the full force of the law.",
                84,
                "PG-13",
                152,
                "Action,Crime,Thriller",
                9.0f,
                3.005f ,
                null);// Revenue in billions
        Movie movie4 = new Movie(
                UUID.randomUUID(), // Generate a random UUID for id
                "The Shawshank Redemption",
                LocalDate.of(1994, 10, 14), // Release year
                Instant.now(), // Set last modified to current time
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption in their friendship.",
                93, // Metascore
                "R", // Certificate
                142, // Runtime in minutes
                "Drama", // Genre
                9.3f, // IMDB rating
                4.0f, // Revenue (unknown)
                null // No director assigned yet
        );
        Director director = new Director(
                UUID.randomUUID(), // Generate a random UUID for id
                "Quentin Tarantino", // Set the director's name
                100000000.0f, // Set the net worth (assuming millions)
                List.of(movie) // Leave the movie list empty for now
        );
        Director director1 = new Director(
                UUID.randomUUID(),
                "Steven Spielberg",
                200000000.0f,
                List.of(movie2,movie3,movie4) // Add the movie to the director's list
        );
        movie.setDirector(director);
        movie3.setDirector(director1);
        movie4.setDirector(director1);
        movie2.setDirector(director1);




        var movies = List.of(movie,movie2,movie3,movie4);



       assertThat( movieService.filterMoviesByParamters(
                "Quentin Tarantino",
                null,
                null,
                movies
        ),containsInAnyOrder(movie));

       assertThat(movieService.filterMoviesByParamters(
               "Steven Spielberg",
               null,
               null,
               movies
       ),containsInAnyOrder(movie2,movie3,movie4));

        assertThat(movieService.filterMoviesByParamters(
                "Steven Spielberg",
                null,
                null,
                movies
        ),containsInAnyOrder(movie2,movie3,movie4));
        assertThat(movieService.filterMoviesByParamters(
                "Steven Spielberg",
                null,
                List.of("Crime"),
                movies
        ),containsInAnyOrder(movie3,movie2));

        assertThat(movieService.filterMoviesByParamters(
                "Steven Spielberg",
                LocalDate.of(1994, 10, 14),
                List.of("Drama"),
                movies
        ),containsInAnyOrder(movie4));

    }
}
