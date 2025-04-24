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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("directors")
@Validated
@Tag(
        name = "Directors",
        description = "Manages the CRUD opeation on the director entites with addition of other manipulation with the directors in the db"
)
@Slf4j
@RequiredArgsConstructor
public class DirectorContoller {

    private final DirectorService directorService;
    private final MovieService movieService;

    private  final String DEFAULT_NOT_FOUND="Director not found";
    private  final String DEFAULT_ALREDY_EXISTS ="Director already exists";
    private  final String DEFAULT_BAD_REQUEST ="Invalid data provided";

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
            throw new BadInputException(DEFAULT_BAD_REQUEST);
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
        Director director = directorService.getDirectorById(id).orElseThrow(
                ()-> new NotFoundException(DEFAULT_NOT_FOUND)
        );
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
            throw new AlreadyExistsException(DEFAULT_ALREDY_EXISTS);
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
                ()-> new NotFoundException(DEFAULT_NOT_FOUND)
        );
        if (directorService.findDirectorByName(directorRequest.getName()).isPresent()) {
            throw new AlreadyExistsException(DEFAULT_ALREDY_EXISTS);
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
                        .getDirectorById(id).orElseThrow(
                                ()-> new NotFoundException(DEFAULT_NOT_FOUND)
                        ).getId()
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



}
