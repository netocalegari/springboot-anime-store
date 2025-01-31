package org.neto.anime_store.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.repository.AnimeRepository;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.util.AnimeCreator;
import org.neto.anime_store.util.AnimePostRequestBodyCreator;
import org.neto.anime_store.wrapper.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("list returns list of animes inside each page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange(
                "/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of animes when successful")
    void list_ReturnsListOfAnimes_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange(
                "/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long expectedId = savedAnime.getId();
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }
//    @Test
//    @DisplayName("findById throws BadRequestException when no anime is found")
//    void findById_ThrowsBadRequestException_WhenNoAnimeIsFound() {
//        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
//                .thenThrow(new BadRequestException("Anime not found"));
//
//        Assertions.assertThatThrownBy(() -> animeController.findById(1L))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Anime not found");
//    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of animes when an anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnAnimeIsNotFound() {
        List<Anime> animes = testRestTemplate.exchange(
                "/animes/find?name=dbz", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns an anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity(
                "/animes",
                animePostRequestBody,
                Anime.class
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates an anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        savedAnime.setName("new name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes an anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId()
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
