package cz.mendelu.ea.xzirchuk.project.moviesAPI.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class DownloadDataFromAPIConfigurationProperties {
    private String downloadUrl;
}
