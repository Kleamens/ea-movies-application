package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.AlreadyExistsException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.BadInputException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


@RestController()
@RequestMapping("movies")
@Validated
@Tag(
        name = "Movies",
        description = "Manages the CRUD opeation on the movie entites with addition of other manipulation with the movies in the db"
)
@Slf4j
@RequiredArgsConstructor
public class MovieContoller {
    private  final MovieService movieService;
    private final DirectorService directorService;

    private  final String DEFAULT_NOT_FOUND="Movie not found";
    private  final String DEFAULT_ALREDY_EXISTS ="Movie already exists";
    private  final String DEFAULT_BAD_REQUEST ="Invalid data provided";


    @GetMapping(value = "/",produces = "application/json")
    @Operation(
            summary = "Get movies",
            description = "Get movies with pagination with parameters representing desire page number and page size(amount of movies displayed simultaneously"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies"),
            @ApiResponse(responseCode = "400",description = "Either the value in page number or page size parametrs negative or not integers"),
            @ApiResponse(responseCode = "404",description = "The resulting list of movies is empty , most likely because the page number is too high")
    })
    public MoviesResponse getMoviesPage(@RequestParam int pageNumber,@RequestParam int pageSize) {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = movieService.getMoviePage(
                    pageNumber
                    , pageSize);
        } catch (NumberFormatException e) {
            throw new BadInputException("AAAAAAAAAAAA");

        }

        if (movies.isEmpty()) {
            throw new NotFoundException(DEFAULT_NOT_FOUND);
        }

        return new MoviesResponse(movies);
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
            throw new BadInputException("AAAAAAAAAAAA");
        }

        Movie movie = new Movie();
        movieRequest.toMovie(movie, directorService);
        doesMoviesAlreadyExist(movie);
        if(directorService.getDirectorById(movie.getDirector()
                        .getId())
                .orElseThrow(
                        ()-> new NotFoundException(DEFAULT_NOT_FOUND)
                )
                .getMovieList().stream().map(Movie::getTitle).toList().contains(movie.getTitle())){
            throw new AlreadyExistsException(DEFAULT_ALREDY_EXISTS);
        }

        movieService.createMovie(movie);
        return  ObjectResponse.of(movie,MovieResponse::new);
    }

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
        Movie movie = movieService.getMovieById(id).orElseThrow(
                ()-> new NotFoundException(DEFAULT_NOT_FOUND)
        );
        return  ObjectResponse.of(movie,MovieResponse::new);
    }




    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
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
            throw new BadInputException("AAAAAAA");
        }
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(
                        ()-> new NotFoundException(DEFAULT_NOT_FOUND)
                );
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
                        .getMovieById(id).orElseThrow(
                                ()-> new NotFoundException(DEFAULT_NOT_FOUND)
                        ).getId()
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
    public MoviesResponse getMoviePageWithFilter(
            @RequestParam int top_n,
            @RequestParam(required = false) String director_name,
            @RequestParam(required = false) List<String> genre,
            @RequestParam(required = false) String year
            ){
        var filtered_movies = movieService.filterMoviesByParamters(
                director_name,
                year!=null?LocalDate.parse(year):null,
                genre
        );
//sorting by revenue
        movieService.sortMoviesByRevenue(filtered_movies);
//getting first n results
        filtered_movies = movieService.getTopNMovies(top_n,filtered_movies);
        return new MoviesResponse(filtered_movies);
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
    public MoviesResponse getMoviePageByImdbWithFilter(
            @RequestParam int top_n,
            @RequestParam(required = false) String director_name,
            @RequestParam(required = false) List<String> genre,
            @RequestParam(required = false) String year
    ){
//        filtering by req params
        var filtered_movies = movieService.filterMoviesByParamters(
                director_name,
                year!=null?LocalDate.parse(year):null,
                genre
        );
//sorting by revenue
        movieService.sortMoviesByImdbRating(filtered_movies);
//getting first n results
        filtered_movies = movieService.getTopNMovies(top_n,filtered_movies);
        return new MoviesResponse(filtered_movies);
    }
    //suporting methods
    public void doesMoviesAlreadyExist(Movie movie){
        if(directorService.getDirectorById(movie.getDirector()
                        .getId())
                .orElseThrow(
                        ()-> new NotFoundException(DEFAULT_NOT_FOUND)
                )
                .getMovieList().stream().map(Movie::getTitle).toList().contains(movie.getTitle())){
            throw new AlreadyExistsException(DEFAULT_ALREDY_EXISTS);
        }
    }



}