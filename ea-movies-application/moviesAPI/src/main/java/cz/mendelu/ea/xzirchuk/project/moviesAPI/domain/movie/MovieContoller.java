package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.AlreadyExistsException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.BadInputException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.response.ArrayResponse;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("movies")
@Validated
@Tag(
        name = "Movies",
        description = "Manages the CRUD opeation on the movie entites with addition of other manipulation with the movies in the db"
)
public class MovieContoller {

    private  final MovieService movieService;
    private final DirectorService directorService;

    @Autowired
    public MovieContoller(MovieService movieService,
                          DirectorService directorService){
        this.movieService = movieService;
        this.directorService = directorService;
    }

    @GetMapping(value = "/",produces = "application/json")
    @Valid
    @Operation(
            summary = "Get movies",
            description = "Get movies with pagination with parameters representing desire page number and page size(amount of movies displayed simultaneously"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies"),
            @ApiResponse(responseCode = "400",description = "Either the value in page number or page size parametrs negative or not integers"),
            @ApiResponse(responseCode = "404",description = "The resulting list of movies is empty , most likely because the page number is too high")
    })
    public ArrayResponse<MovieResponse> getMoviesPage(@RequestParam String pageNumber,@RequestParam String pageSize) {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = movieService.getMoviePage(
                    Integer.parseInt(pageNumber)
                    , Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            throw new BadInputException();

        }

        if (movies.isEmpty()) {
            throw new NotFoundException();
        }

        return ArrayResponse.of(movies, MovieResponse::new);
    }

    @Valid
    @PostMapping(value = "/",produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new movie",
            description = "Create a movie"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the movies was created successfully"),
            @ApiResponse(responseCode = "400",description = "The payload contains invalid data,currenly only if Invalid genre"),
            @ApiResponse(responseCode = "404",description = "The director with the id does not exist"),
            @ApiResponse(responseCode = "409",description = "The same movies with the same director already exists")
    })
    public ObjectResponse<MovieResponse> createMovie(@RequestBody @Valid MovieRequest movieRequest){
        if(movieService.doesGenreContainInvalid(movieRequest.getGenre())){
            throw new BadInputException();
        }

        Movie movie = new Movie();
        movieRequest.toMovie(movie, directorService);
        doesMoviesAlreadyExist(movie);
        if(directorService.getDirectorById(movie.getDirector()
                        .getId())
                .orElseThrow(
                        NotFoundException::new
                )
                .getMovieList().stream().map(Movie::getTitle).toList().contains(movie.getTitle())){
            throw new AlreadyExistsException();
        }

        movieService.createMovie(movie);
        return  ObjectResponse.of(movie,MovieResponse::new);
    }

    @Valid
    @GetMapping(value = "/{id}",produces = "application/json")
    @Operation(
            summary = "Get movie",
            description = "Get movie by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie with the id"),
            @ApiResponse(responseCode = "404",description = "The movies with the id was not found")
    })
    public ObjectResponse<MovieResponse> getMovieById(@PathVariable UUID id){
        Movie movie = movieService.getMovieById(id).orElseThrow(NotFoundException::new);
        return  ObjectResponse.of(movie,MovieResponse::new);
    }




    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Valid
    @Transactional
    @Operation(
            summary = "Update movie",
            description = "Update movie by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The movies was updated successfully"),
            @ApiResponse(responseCode = "400",description = "The payload contains invalid data,currenly only if Invalid genre"),
            @ApiResponse(responseCode = "404",description = "The director with the id does not exist"),
            @ApiResponse(responseCode = "409",description = "The same movies with the same director already exists")
    })
    public ObjectResponse<MovieResponse> updateMovie(@PathVariable UUID id, @RequestBody @Valid MovieRequest movieRequest) {
        if(movieService.doesGenreContainInvalid(movieRequest.getGenre())){
            throw new BadInputException();
        }
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(NotFoundException::new);
        movieRequest.toMovie(movie,directorService);
        doesMoviesAlreadyExist(movie);
        movie.setLastModified(Instant.now());
        Movie return_movie =movieService.updateMovie(id, movie);

        return ObjectResponse.of(return_movie, MovieResponse::new);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete the movies",
            description = "Delete movie by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The movies was delted successfully"),
            @ApiResponse(responseCode = "404",description = "The movie with the id does not exist"),
    })
    public void deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(
                movieService
                        .getMovieById(id).orElseThrow(NotFoundException::new).getId()
        );
    }

    @GetMapping(value = "/topMoviesByRevenueWithFilter", produces = "application/json")
    @Operation(
            summary = "Get top n movies by revenue",
            description = "Get top n movies by revenue with filters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of movies sorted by revenue with filters"),
            @ApiResponse(responseCode = "404",description = "No movies satisfying the filters found"),
    })
    public ArrayResponse<MovieResponse> getMoviePageWithFilter(
            @RequestParam String top_n,
            @RequestParam(required = false) String director_name,
            @RequestParam(required = false) List<String> genre,
            @RequestParam(required = false) String year
            ){
        var movies = movieService.getAllMovies();
       String string_genres = genre.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(",", "", ""));

        if(movieService.doesGenreContainInvalid(string_genres)){
            throw new BadInputException();
        }
//        filtering by req params
        var filtered_movies= movieService.filterMoviesByParamters(
                director_name,
                year!=null?LocalDate.parse(year):null,
                genre,
                movies
        );
        if (filtered_movies.isEmpty()){throw new NotFoundException();};
//sorting by revenue
        movieService.sortMoviesByRevenue(filtered_movies);
//getting first n results
        try{
            filtered_movies=filtered_movies.subList(0,Integer.parseInt(top_n));
        }catch (IndexOutOfBoundsException e){
            System.out.println("### LIMIT TOO HIGH, RETURNING ALL OF THE AVAILABLE ITEMS ");
        }catch (NumberFormatException e){
            throw  new BadInputException();
        }
        return ArrayResponse.of(filtered_movies,MovieResponse::new);
    }
    @GetMapping(value = "/topMoviesByImdbWithFilter", produces = "application/json")
    @Operation(
            summary = "Get top n movies by imdb rating",
            description = "Get top n movies by imdb rating with filters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of movies sorted by imdb rating with filters"),
            @ApiResponse(responseCode = "404",description = "No movies satisfying the filters found"),
    })
    public ArrayResponse<MovieResponse> getMoviePageByImdbWithFilter(
            @RequestParam String top_n,
            @RequestParam(required = false) String director_name,
            @RequestParam(required = false) List<String> genre,
            @RequestParam(required = false) String year
    ){
        String string_genres = genre.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(",", "", ""));
        if(movieService.doesGenreContainInvalid(string_genres)){
            throw new BadInputException();
        }
        var movies = movieService.getAllMovies();
//        filtering by req params
        var filtered_movies= movieService.filterMoviesByParamters(
                director_name,
                year!=null?LocalDate.parse(year):null,
                genre,
                movies
        );
        if (filtered_movies.isEmpty()){throw new NotFoundException();};
//sorting by revenue
        movieService.sortMoviesByImdbRating(filtered_movies);
//getting first n results
        try{
            filtered_movies=filtered_movies.subList(0,Integer.parseInt(top_n));
        }catch (IndexOutOfBoundsException e){
            System.out.println("### LIMIT TOO HIGH, RETURNING ALL OF THE AVAILABLE ITEMS ");
        }catch (NumberFormatException e){
            throw  new BadInputException();
        }
        return ArrayResponse.of(filtered_movies,MovieResponse::new);
    }
    //suporting methods
    public void doesMoviesAlreadyExist(Movie movie){
        if(directorService.getDirectorById(movie.getDirector()
                        .getId())
                .orElseThrow(
                        NotFoundException::new
                )
                .getMovieList().stream().map(Movie::getTitle).toList().contains(movie.getTitle())){
            throw new AlreadyExistsException();
        }
    }
}