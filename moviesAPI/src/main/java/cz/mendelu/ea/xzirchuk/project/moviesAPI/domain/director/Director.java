package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @Column(unique = true)
    private String name;


    @Min(1)
    private Float net_worth;

    @OneToMany(mappedBy = "director",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @JsonIgnore

    List<Movie> movieList;

//todo find director by name
}
