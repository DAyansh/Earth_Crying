package com.earthcrying.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * WebClient-based aggregator for external environmental data APIs.
 * Each method builds a complete URI (base URL is a full URL from config),
 * performs a blocking call with a timeout, and returns raw JSON as a String.
 */
@Service
@Slf4j
public class ExternalDataService {

    @Value("${app.external-apis.global-forest-watch.base-url}")
    private String gfwBaseUrl;

    @Value("${app.external-apis.global-forest-watch.api-key}")
    private String gfwApiKey;

    @Value("${app.external-apis.noaa.base-url}")
    private String noaaBaseUrl;

    @Value("${app.external-apis.openaq.base-url}")
    private String openaqBaseUrl;

    @Value("${app.external-apis.world-bank.base-url}")
    private String worldBankBaseUrl;

    @Value("${app.external-apis.nasa.base-url}")
    private String nasaBaseUrl;

    @Value("${app.external-apis.nasa.api-key}")
    private String nasaApiKey;

    private final WebClient webClient;

    public ExternalDataService() {
        var httpClient = HttpClient.create()
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(30));

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public String fetchGlobalForestWatchData() {
        try {
            return webClient.get()
                    .uri(gfwBaseUrl + "/api/v2/area?api-key=" + gfwApiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Error fetching from Global Forest Watch API", e);
            return null;
        }
    }

    public String fetchNoaaData() {
        try {
            return webClient.get()
                    .uri(noaaBaseUrl + "/collection?dataset=global-marine-surface-temperature")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Error fetching from NOAA API", e);
            return null;
        }
    }

    public String fetchOpenAQData(String parameter) {
        try {
            return webClient.get()
                    .uri(openaqBaseUrl + "/measurements?parameter=" + parameter + "&limit=10")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Error fetching from OpenAQ API", e);
            return null;
        }
    }

    public String fetchWorldBankData(String indicator) {
        try {
            return webClient.get()
                    .uri(worldBankBaseUrl + "/country/all/indicator/" + indicator + "?format=json&per_page=10")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Error fetching from World Bank API", e);
            return null;
        }
    }

    public String fetchNasaApodData() {
        try {
            return webClient.get()
                    .uri(nasaBaseUrl + "/planetary/apod?api_key=" + nasaApiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Error fetching from NASA API", e);
            return null;
        }
    }
}
