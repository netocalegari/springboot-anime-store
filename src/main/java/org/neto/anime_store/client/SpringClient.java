package org.neto.anime_store.client;

import lombok.extern.log4j.Log4j2;
import org.neto.anime_store.domain.Anime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
//        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(
//                "http://localhost:8080/animes/{id}",
//                Anime.class,
//                1
//        );
//        log.info(entity);
//
//        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 1);
//        log.info(object);
//
//        ResponseEntity<List<Anime>> animesList = new RestTemplate().exchange(
//                "http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
//                }
//        );
//        log.info(animesList.getBody());

//        Anime kingdom = Anime.builder().name("Kingdom").build();
//        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
//        log.info(kingdomSaved);

        Anime samurai = Anime.builder().name("Samurai X").build();
        ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange(
                "http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(samurai, createJsonHeader()),
                new ParameterizedTypeReference<>() {
                }
        );
        log.info(samuraiSaved);

        Anime animeToUpdate = samuraiSaved.getBody();
        animeToUpdate.setName("Samurai X2");

        ResponseEntity<Void> samuraiUpdated = new RestTemplate().exchange(
                "http://localhost:8080/animes",
                HttpMethod.PUT,
                new HttpEntity<>(animeToUpdate, createJsonHeader()),
                Void.class
        );
        log.info(samuraiUpdated);

        ResponseEntity<Void> samuraiDelete = new RestTemplate().exchange(
                "http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeToUpdate.getId()
        );
        log.info(samuraiDelete);


    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }
}
