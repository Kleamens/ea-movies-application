package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilmResponse {
    private String message;
    private List<FilmResult> result;

}

