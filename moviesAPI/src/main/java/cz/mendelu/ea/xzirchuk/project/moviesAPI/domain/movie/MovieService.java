package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.BadInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.HashSet;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final Logger logger = LoggerFactory.getLogger(MovieContoller.class);
    private final MovieRepository movieRepository;
    private final MoviePaginationRepository moviePaginationRepository;


    public MovieService(MovieRepository movieRepository,
                        MoviePaginationRepository moviePaginationRepository)
    {

        this.movieRepository= movieRepository;
        this.moviePaginationRepository = moviePaginationRepository;
    }

    public List<Movie> getAllMovies(){
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }
    public List<Movie> getMoviePage(int pageNumber,int pageSize){
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = moviePaginationRepository.findAll(pageRequest);
        return page.get().toList();
    }

    public Optional<Movie> getMovieById(UUID id){
        return movieRepository.findById(id);
    }

    public Movie createMovie(Movie movie){
        return movieRepository.save(movie);
    }


    public Movie updateMovie(UUID id, Movie movie) {
        movie.setId(id);
        return movieRepository.save(movie);
    }

    public void deleteMovie(UUID id) {
        movieRepository.deleteById(id);
    }

    public void sortMoviesByRevenue(List<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>(){

            public int compare(Movie o1, Movie o2)
            {
                return o1.getRevenue().compareTo(o2.getRevenue());
            }
        });
        Collections.reverse(movies);
    }

    public void sortMoviesByImdbRating(List<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>(){

            public int compare(Movie o1, Movie o2)
            {
                return o1.getImdbRating().compareTo(o2.getImdbRating());
            }
        });
        Collections.reverse(movies);
    }

    public List<Movie> filterMoviesByParamters(
            String director_name,
            LocalDate releaseYear,
            List<String> genre
    ){
        var movies = getAllMovies();
        String string_genres = genre.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(",", "", ""));

        if(doesGenreContainInvalid(string_genres)){
            throw new BadInputException("AAAAAAAAA");
        }
//        filtering by req params

        List<Movie> movies_copy = List.copyOf(movies);
        var filtered_movies= new ArrayList<>(movies_copy.stream().filter(movie -> {
            return ( //filtering by presence of revenue data
                    movie.getRevenue() != null)
                    //filtering by presence director name if present
                    && (director_name != null ?
                    movie.getDirector().getName().equals(director_name) : true)
                    && (genre!=null&&!genre.isEmpty() ? new HashSet<>(Arrays.stream(movie
                            .getGenre()
                            .split(","))
                    .toList())
                    .containsAll(genre) : true)
                    // filtering by year if present
                    && (releaseYear != null ? movie.getReleaseYear().equals(releaseYear) : true);
        }).toList());
        return filtered_movies;
    }
    public List<Movie> getMoviesByDirectorName(String name){
        List<Movie> movies = new ArrayList<>();

        movieRepository.findAll().forEach(movies::add);
        movies = movies.stream().filter(
                movie -> movie.getDirector().getName().equals(name)
        ).toList();
        return  movies;
    }

    public boolean doesGenreContainInvalid(String genres){
        List<String> genres_split= List.of(
                genres.replace(
                        " ",
                        "")
                        .split(","));
        for (String genre:genres_split){
            if(!genre.matches(
                    "Action|Drama|Mystery|Adventure|Family|Fantasy|Crime|Thriller|Music|Biography|Animation|Sci-Fi|War|Romance|Western|Comedy|History"
            )){
                return true;
            }
        }
        return false;
    }


    public List<Movie> getTopNMovies(int top_n,List<Movie> movies){

        List<Movie> filtered_movies=List.of();
        try{
            filtered_movies=movies.subList(0,top_n);
        }catch (IndexOutOfBoundsException e){
            logger.debug("### LIMIT TOO HIGH, RETURNING ALL OF THE AVAILABLE ITEMS ");
        }catch (NumberFormatException e){
            throw  new BadInputException("AAAAAAAAAAA");
        }
        return filtered_movies;

    }


}
