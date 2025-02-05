package org.neto.anime_store.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.repository.AnimeRepository;
import org.neto.anime_store.repository.AnimeStoreUserRepository;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.util.AnimeCreator;
import org.neto.anime_store.util.AnimePostRequestBodyCreator;
import org.neto.anime_store.wrapper.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    private AnimeStoreUser createUser(String name, String username, String authorities) {
        return AnimeStoreUser.builder()
                .name(name) // Make names unique
                .password("$2a$10$5z5iZto9CFpI/rIB1qL8MuNUvUir1HABUNoZ91tUUrQcmBPzdWN5C")
                .username(username)
                .authorities(authorities)
                .build();
    }

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private AnimeStoreUserRepository animeStoreUserRepository;

    @Test
    @DisplayName("list returns list of animes inside each page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange(
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
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplateRoleUser.exchange(
                "/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long expectedId = savedAnime.getId();
        Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplateRoleUser.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of animes when an anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnAnimeIsNotFound() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        List<Anime> animes = testRestTemplateRoleUser.exchange(
                "/animes/find?name=dbz", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }
        ).getBody();

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns an anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity(
                "/animes",
                animePostRequestBody,
                Anime.class
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
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
    @DisplayName("replace updates an anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        savedAnime.setName("new name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
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
        AnimeStoreUser user = createUser("Administrator", "admin", "ROLE_ADMIN, ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
                "/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId()
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 when user it not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        AnimeStoreUser user = createUser("Neto Calegari", "neto", "ROLE_USER");
        animeStoreUserRepository.saveAndFlush(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
                "/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId()
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUser(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port)
                    .basicAuthentication("neto", "123456");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdmin(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "123456");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }
}
