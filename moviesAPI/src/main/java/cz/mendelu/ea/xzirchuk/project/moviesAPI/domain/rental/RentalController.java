package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.rental;

import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director.DirectorService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.*;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user.UserService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.AlreadyExistsException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.BadInputException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("rental")
@Tag(
        name = "Rental",
        description = "Manages the CRUD opeation on the rental entites with addition of other manipulation with the rental in the db"
)
public class RentalController {
    private final Logger logger = LoggerFactory.getLogger(MovieContoller.class);
    private  final RentalService rentalService;
    private  final MovieService movieService;
    private  final UserService userService;

    @Autowired
    public RentalController(RentalService rentalService,MovieService movieService,UserService userService) {
        this.rentalService = rentalService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping(value = "/",produces = "application/json")
    @Operation(
            summary = "Get rentals",
            description = "Get rentals with pagination with parameters representing desire page number and page size(amount of rentals displayed simultaneously"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rentals"),
            @ApiResponse(responseCode = "400",description = "Either the value in page number or page size parametrs negative or not integers"),
    })
    public RentalsResponse getRentalPage(@RequestParam int pageNumber, @RequestParam int pageSize) {
        List<Rental> rentals = new ArrayList<>();
        try {
            rentals = rentalService.getRentalPage(
                    pageNumber
                    , pageSize);
        } catch (NumberFormatException e) {
            throw new BadInputException();
        }

        return new RentalsResponse(rentals);
    }


    @PostMapping(value = "/",produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new rental",
            description = "Create a rental"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the rental was created successfully"),
            @ApiResponse(responseCode = "400",description = "The payload contains invalid data"),
            @ApiResponse(responseCode = "404",description = "The movie with the id does not exist"),
            @ApiResponse(responseCode = "409",description = "The same rental with the same movie already exists")
    })
    public ObjectResponse<RentalResponse> createRental(@RequestBody @Valid RentalRequest rentalRequest){

        Rental rental = new Rental();
        rentalRequest.toRental(rental, movieService,userService);

        rentalService.createRental(rental);
        return  ObjectResponse.of(rental,RentalResponse::new);
    }

    @PutMapping(value = "/finishRental",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Finish a  rental",
            description = "Finish a rental"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Rental was finished successfully"),
            @ApiResponse(responseCode = "400",description = "The payload contains invalid data"),
            @ApiResponse(responseCode = "404",description = "The movie with the id does not exist"),
    })
    public ObjectResponse<RentalResponse> finishRental(@RequestParam UUID movieId){

        Rental rental = rentalService.getLastUnfinishedRental(movieId);
        if(rental != null){
            rentalService.finishRental(rental, LocalDate.now());
            return  ObjectResponse.of(rental,RentalResponse::new);
        }
        else{
            throw new NotFoundException();
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete the rental",
            description = "Delete rental by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The rental was deleted successfully"),
            @ApiResponse(responseCode = "404",description = "The rental with the id does not exist"),
    })
    public void deleteRental(@PathVariable UUID id) {
        rentalService.deleteRental(
                        rentalService.getRentalbyId(id).orElseThrow(NotFoundException::new).getId()
        );
    }
}
