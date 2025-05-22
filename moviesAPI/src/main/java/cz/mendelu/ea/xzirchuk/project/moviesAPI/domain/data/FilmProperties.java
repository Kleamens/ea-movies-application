package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class FilmProperties {
    private String created;
    private String edited;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private String producer;
    private String title;
    private int episode_id;
    private String director;
    private String release_date;
    private String opening_crawl;
    private List<String> characters;
    private List<String> species;
    private String url;
}
