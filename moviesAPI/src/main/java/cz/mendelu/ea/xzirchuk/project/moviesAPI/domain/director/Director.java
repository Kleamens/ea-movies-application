package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;


import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.movie.Movie;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @Column(unique = true)
    private String name;


    @Min(1)
    private Float net_worth;

    @OneToMany(mappedBy = "director",cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    @JsonIgnore
    List<Movie> movieList;


    //todo find director by name
}
