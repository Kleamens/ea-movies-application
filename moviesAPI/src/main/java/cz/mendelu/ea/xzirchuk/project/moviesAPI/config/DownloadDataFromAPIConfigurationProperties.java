package cz.mendelu.ea.xzirchuk.project.moviesAPI.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class DownloadDataFromAPIConfigurationProperties {
    private String downloadUrl;
}
