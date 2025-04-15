package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions.NotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DirectorRequest {


    @NotEmpty
    @NotNull
    @Column(unique = true)
    private String name;


    @Min(1)
    @NotNull
    private Float net_worth;



    public void toDirector(Director director, MovieService movieService){
        director.setName(name);
        director.setNet_worth(net_worth);
        if(movieService.getMoviesByDirectorName(name).isEmpty()){
            director.setMovieList(List.of());
        }
    }


}
