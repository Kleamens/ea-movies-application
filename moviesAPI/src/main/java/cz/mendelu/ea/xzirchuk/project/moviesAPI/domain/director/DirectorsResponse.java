package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.director;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DirectorsResponse {

    List<DirectorResponse> directors;

    DirectorsResponse(List<Director> directors) {
        this.directors = directors.stream().map(DirectorResponse::new).collect(Collectors.toList());

    }
}
