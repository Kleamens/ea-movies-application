package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.*;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("directors")
@Validated
@Tag(
        name = "Directors",
        description = "Manages the CRUD opeation on the director entites with addition of other manipulation with the directors in the db"
)
public class DirectorContoller {
    private final Logger logger = LoggerFactory.getLogger(DirectorContoller.class);

    private final DirectorService directorService;
    private final MovieService movieService;

    @Autowired
    public DirectorContoller(
            DirectorService directorService, MovieService movieService
    ){
        this.directorService = directorService;
        this.movieService = movieService;
    }

    @GetMapping(value = "/",produces = "application/json")
    @Operation(
            summary = "Get directors",
            description = "Get directors page with provided page number and size"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of directors"),
            @ApiResponse(responseCode = "400",description = "Wrong input found in page number or size parameters"),
    })
    public DirectorsResponse getDirectorsPage(@RequestParam int pageNumber,
                                                            @RequestParam int pageSize){
        List<Director> directors = new ArrayList<>();
        try {
            directors = directorService.getDirectorPage(
                    pageNumber
                    , pageSize);
        } catch (NumberFormatException e) {
            throw new BadInputException();
        }


        return new  DirectorsResponse(directors);
    }

    @GetMapping(value = "/{id}",produces = "application/json")
    @Operation(
            summary = "Get director by id",
            description = "Get directors by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A director entity"),
            @ApiResponse(responseCode = "404",description = "Director with id doesnt exist"),
    })
    public ObjectResponse<DirectorResponse> getDirectorById(@PathVariable UUID id){
        Director director = directorService.getDirectorById(id).orElseThrow(NotFoundException::new);
        return  ObjectResponse.of(director,DirectorResponse::new);
    }

    @PostMapping(value = "/",produces = "application/json")
    @Valid
    @Operation(
            summary = "Create director",
            description = "Create director with request"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The created director"),
            @ApiResponse(responseCode = "409",description = "Director Already exists"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectResponse<DirectorResponse> createDirector(@Valid @RequestBody DirectorRequest directorRequest){
        Director director = new Director();
        if (directorService.findDirectorByName(directorRequest.getName()).isPresent()){
            throw new AlreadyExistsException();
        }else{
            directorRequest.toDirector(director,movieService);
            directorService.createDirector(director);
            return ObjectResponse.of(director,DirectorResponse::new);
        }
    }

    @PutMapping(value = "/{id}",produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    @Operation(
            summary = "Update existing director",
            description = "Update director by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "209", description = "Updated Succesfully"),
            @ApiResponse(responseCode = "409",description = "Director Already exists"),
            @ApiResponse(responseCode = "404",description = "Director with id was not found"),
    })
    public ObjectResponse<DirectorResponse> updateDirector(
            @PathVariable UUID id ,
            @RequestBody @Valid DirectorRequest directorRequest){
        Director director = directorService.getDirectorById(id).orElseThrow(
                NotFoundException::new
        );
        if (directorService.findDirectorByName(directorRequest.getName()).isPresent()) {
            throw new AlreadyExistsException();
        }
        directorRequest.toDirector(director,movieService);
        var return_director=directorService.updateDirector(id,director);
        return ObjectResponse.of(return_director,DirectorResponse::new);

    }
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(
            summary = "Delete director",
            description = "Delete director by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Director Deleted Succesfully"),
            @ApiResponse(responseCode = "404",description = "Director with id was not found"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDirector(@PathVariable UUID id) {
        directorService.deleteDirector(
                directorService
                        .getDirectorById(id).orElseThrow(NotFoundException::new).getId()
        );
    }

    @GetMapping(value = "/directorsByNetWorth",produces = "application/json")
    @Operation(
            summary = "get directors sorted by networth",
            description = "Get directors by networth with filters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of n directors sorted by networth"),
            @ApiResponse(responseCode = "400",description = "Wrong input in paramters"),
    })
    public DirectorsResponse getProducersByNetWorth(
            @RequestParam int top_n
    ){
        var directors =directorService.getAllDirectors();
        directorService.sortDirectorsByNetWorth(directors);
        directors = directorService.getTopNDirectors(top_n,directors);
        return new DirectorsResponse(directors);
    }

    @ExceptionHandler({BadInputException.class})
    public ResponseEntity BadInputExceptionDirector(final BadInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Erroneous input when searching for director",BAD_REQUEST);
        return new ResponseEntity<>(errorResponse.getMessage(),errorResponse.getHttpStatus());
    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity exceptionDirectorExists(AlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Director already exists",CONFLICT);
        return new ResponseEntity<>(errorResponse.getMessage(),errorResponse.getHttpStatus());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity exceptionDirectorNotFound(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Director not found",NOT_FOUND);
        return new ResponseEntity<>(errorResponse.getMessage(),errorResponse.getHttpStatus());
    }

}
