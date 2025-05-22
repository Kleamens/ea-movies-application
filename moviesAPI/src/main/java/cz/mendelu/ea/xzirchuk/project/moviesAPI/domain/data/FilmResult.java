package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmResult {
    private String _id;
    private String uid;
    private String description;
    private int __v;
    private FilmProperties properties;
}
