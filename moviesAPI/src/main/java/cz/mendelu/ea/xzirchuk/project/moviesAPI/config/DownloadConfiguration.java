package cz.mendelu.ea.xzirchuk.project.moviesAPI.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class DownloadConfiguration {
    @ConfigurationProperties(prefix = "app")
    @Bean
    public DownloadDataFromAPIConfigurationProperties getconfigurationProperties(){
        return new DownloadDataFromAPIConfigurationProperties();
    }


    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(getconfigurationProperties().getDownloadUrl()).build();
    }

}
