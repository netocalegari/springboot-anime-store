package org.neto.anime_store.util;

import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AuthorityDto;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;

import java.util.List;

public class AnimeStoreUserCreator {
    public static AnimeStoreUser createValidAnimeStoreUser() {
        return AnimeStoreUser.builder()
                .id(1L)
                .name("Giorno Giovanna")
                .username("jojo")
                .password("123456")
                .authorities("ROLE_ADMIN")
                .build();
    }
}
