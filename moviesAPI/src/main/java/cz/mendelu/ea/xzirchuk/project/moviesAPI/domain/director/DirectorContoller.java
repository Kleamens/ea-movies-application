package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.AlreadyExistsException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.BadInputException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.response.ArrayResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            @ApiResponse(responseCode = "404",description = "No directors found , most likely page number too high"),
            @ApiResponse(responseCode = "400",description = "Wrong input found in page number or size parameters"),
    })
    public ArrayResponse<DirectorResponse> getDirectorsPage(@RequestParam String pageNumber,
                                                            @RequestParam String pageSize){
        List<Director> directors = new ArrayList<>();
        try {
            directors = directorService.getDirectorPage(
                    Integer.parseInt(pageNumber)
                    , Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            throw new BadInputException();
        }

        if (directors.isEmpty()) {
            throw new NotFoundException();
        }

        return ArrayResponse.of(directors, DirectorResponse::new);
    }

    @Valid
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
    @Valid
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
    public ArrayResponse<DirectorResponse> getProducersByNetWorth(
            @RequestParam String top_n
    ){
        var directors =directorService.getAllDirectors();
        directorService.sortDirectorsByNetWorth(directors);
        try{
            directors.subList(0,Integer.parseInt(top_n));
        }catch (IndexOutOfBoundsException e){
            logger.debug("### LIMIT TOO HIGH, RETURNING ALL OF THE AVAILABLE ITEMS ");
        }catch (NumberFormatException e){
            throw new BadInputException();
        }
        return ArrayResponse.of(directors,DirectorResponse::new);
    }

}
