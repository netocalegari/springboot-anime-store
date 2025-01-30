package org.neto.anime_store.repository;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.neto.anime_store.domain.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
@Log4j2
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persistis anime when successful")
    void save_PersistAnime_WhenSuccesfull() {
        Anime animeToSave = createAnime();
        Anime savedAnime = this.animeRepository.save(animeToSave);

        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeToSave.getName());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    void save_UpdatesAnime_WhenSuccesfull() {
        Anime animeToSave = createAnime();
        Anime savedAnime = this.animeRepository.save(animeToSave);
        savedAnime.setName("Overlord");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(updatedAnime).isNotNull();
        Assertions.assertThat(updatedAnime.getId()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isEqualTo(savedAnime.getName());
    }


    @Test
    @DisplayName("Save throw ConstraintViolationException anime when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Anime anime = new Anime();

//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The name cannot be empty");
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccesfull() {
        Anime animeToSave = createAnime();
        Anime savedAnime = this.animeRepository.save(animeToSave);

        this.animeRepository.delete(savedAnime);
        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of anime when successful")
    void finByName_ReturnListOfAnime_WhenSuccesfull() {
        Anime animeToSave = createAnime();
        Anime savedAnime = this.animeRepository.save(animeToSave);
        String name = savedAnime.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty().contains(savedAnime);
    }

    @Test
    @DisplayName("find by name returns empty list anime when no anime is found")
    void finByName_ReturnEmptyList_WhenSuccesfull() {
        List<Anime> animes = this.animeRepository.findByName("AAAAAAA");

        Assertions.assertThat(animes).isEmpty();
    }


    private Anime createAnime() {
        return Anime.builder().name("Hajime no Ippo").build();
    }
}