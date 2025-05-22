package cz.mendelu.ea.xzirchuk.project.moviesAPI.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class DownloadConfiguration {
    private final DownloadDataFromAPIConfigurationProperties downloadDataFromAPIConfigurationProperties;


    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(downloadDataFromAPIConfigurationProperties.getDownloadUrl()).build();
    }

}
