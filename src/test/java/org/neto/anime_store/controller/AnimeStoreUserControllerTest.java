package org.neto.anime_store.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.dtos.AuthorityDto;
import org.neto.anime_store.exceptions.NotFoundException;
import org.neto.anime_store.requests.AnimeStoreUserPutRequestBody;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;
import org.neto.anime_store.service.AnimeStoreUserService;
import org.neto.anime_store.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeStoreUserControllerTest {
    @InjectMocks
    private AnimeStoreUserController animeStoreUserController;
    @Mock
    private AnimeStoreUserService animeStoreUserServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(animeStoreUserServiceMock.save(ArgumentMatchers.any(AnimeStoreUserSaveRequestBody.class)))
                .thenReturn(AnimeStoreUserCreator.createValidAnimeStoreUser());

        BDDMockito.when(animeStoreUserServiceMock.list())
                .thenReturn(List.of(AnimeStoreUserDto.builder()
                                            .id(AnimeStoreUserCreator.createValidAnimeStoreUser().getId())
                                            .name(AnimeStoreUserCreator.createValidAnimeStoreUser().getName())
                                            .username(AnimeStoreUserCreator.createValidAnimeStoreUser().getUsername())
                                            .password(AnimeStoreUserCreator.createValidAnimeStoreUser().getPassword())
                                            .authorities(AnimeStoreUserCreator.createValidAnimeStoreUser()
                                                                 .getAuthorities()
                                                                 .stream()
                                                                 .map(authority -> new AuthorityDto(authority.getAuthority()))
                                                                 .toList())
                                            .build()));

        BDDMockito.when(animeStoreUserServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeStoreUserToDto.animeStoreUserToDto(AnimeStoreUserCreator.createValidAnimeStoreUser()));

        BDDMockito.when(animeStoreUserServiceMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(AnimeStoreUserToDto.animeStoreUserToDto(AnimeStoreUserCreator.createValidAnimeStoreUser()));


        BDDMockito.doNothing()
                .when(animeStoreUserServiceMock)
                .replace(ArgumentMatchers.any(AnimeStoreUserPutRequestBody.class));

        BDDMockito.doNothing().when(animeStoreUserServiceMock).delete(ArgumentMatchers.anyLong());
    }


    @Test
    @DisplayName("save returns an anime when successful")
    void save_AnimeStoreUser_WhenSuccessful() {
        BDDMockito.when(animeStoreUserServiceMock.findByUsername(ArgumentMatchers.anyString())).thenReturn(null);

        AnimeStoreUserSaveRequestBody validAnimeStoreUserSaveRequestBody =
                AnimeStoreUserSaveRequestBodyCreator.createValidAnimeStoreUserSaveRequestBody();

        ResponseEntity<AnimeStoreUser> response = animeStoreUserController.save(validAnimeStoreUserSaveRequestBody);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(AnimeStoreUserCreator.createValidAnimeStoreUser());
    }

    @Test
    @DisplayName("listAll returns list of users when successful")
    void listAll_ReturnsListOfAnimeStoreUsers_WhenSuccessful() {
        String expectedUsername = AnimeStoreUserCreator.createValidAnimeStoreUser().getUsername();
        List<AnimeStoreUserDto> users = animeStoreUserController.listAll().getBody();

        Assertions.assertThat(users).isNotEmpty().isNotNull().hasSize(1);
        Assertions.assertThat(users.getFirst().getUsername()).isEqualTo(expectedUsername);
    }

    @Test
    @DisplayName("findByUsername returns null when user not found")
    void findByUsername_ReturnsNull_WhenNotFound() {
        BDDMockito.when(animeStoreUserServiceMock.findByUsername(ArgumentMatchers.anyString())).thenReturn(null);
        ResponseEntity<AnimeStoreUserDto> user = animeStoreUserController.findByUsername("Ainz");

        Assertions.assertThat(user.getBody()).isNull();
        Assertions.assertThat(user.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("findByUsername returns user when successful")
    void findByUsername_ReturnsUser_WhenSuccessful() {
        String expectedUsername = AnimeStoreUserCreator.createValidAnimeStoreUser().getUsername();
        ResponseEntity<AnimeStoreUserDto> userFound = animeStoreUserController.findByUsername(expectedUsername);

        Assertions.assertThat(userFound.getBody()).isNotNull();
        Assertions.assertThat(userFound.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(userFound.getBody().getUsername()).isEqualTo(expectedUsername);
    }

    @Test
    @DisplayName("findById returns user when successful")
    void findById_ReturnsUser_WhenSuccessful() {
        Long expectedId = AnimeStoreUserCreator.createValidAnimeStoreUser().getId();
        ResponseEntity<AnimeStoreUserDto> userFound = animeStoreUserController.findById(expectedId);

        Assertions.assertThat(userFound).isNotNull();
        Assertions.assertThat(userFound.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(userFound.getBody()).isNotNull();
        Assertions.assertThat(userFound.getBody().getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById throws user when successful")
    void findById_ThrowsNotFoundException_WhenNotFound() {
        BDDMockito.when(animeStoreUserServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new NotFoundException("User not found"));

        Assertions.assertThatThrownBy(() -> animeStoreUserController.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("replace updates user data when successful")
    void replace_UpdatesUserData_WhenSuccessful() {
        ResponseEntity<Void> entity =
                animeStoreUserController.replace(AnimeStoreUserPutRequestBodyCreator.createAnimeStoreUserPutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes user when successful")
    void delete_RemovesUser_WhenSuccessful() {
        ResponseEntity<Void> entity = animeStoreUserController.delete(1L);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}