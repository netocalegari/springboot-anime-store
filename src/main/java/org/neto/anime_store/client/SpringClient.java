package org.neto.anime_store.client;

import lombok.extern.log4j.Log4j2;
import org.neto.anime_store.domain.Anime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(
                "http://localhost:8080/animes/{id}",
                Anime.class,
                11
        );
        log.info(entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 11);
        log.info(object);

        ResponseEntity<List<Anime>> animesList = new RestTemplate().exchange(
                "http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }
        );
        log.info(animesList.getBody());

//        Anime kingdom = Anime.builder().name("Kingdom").build();
//        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
//        log.info(kingdomSaved);

        Anime samurai = Anime.builder().name("Samurai X").build();
        ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange(
                "http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(samurai),
                new ParameterizedTypeReference<>() {
                }
        );
        log.info(samuraiSaved);

    }
}
