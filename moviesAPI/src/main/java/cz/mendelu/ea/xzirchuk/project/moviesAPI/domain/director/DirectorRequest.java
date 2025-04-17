package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.MovieService;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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
