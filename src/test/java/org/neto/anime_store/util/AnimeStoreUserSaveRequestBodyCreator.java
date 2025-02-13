package org.neto.anime_store.util;

import org.neto.anime_store.dtos.AuthorityDto;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;

public class AnimeStoreUserSaveRequestBodyCreator {
    public static AnimeStoreUserSaveRequestBody createValidAnimeStoreUserSaveRequestBody() {
        return AnimeStoreUserSaveRequestBody.builder()
                .name(AnimeStoreUserCreator.createValidAnimeStoreUser().getName())
                .username(AnimeStoreUserCreator.createValidAnimeStoreUser().getUsername())
                .password(AnimeStoreUserCreator.createValidAnimeStoreUser().getPassword())
                .authorities(AnimeStoreUserCreator.createValidAnimeStoreUser()
                                     .getAuthorities()
                                     .stream()
                                     .map(authority -> new AuthorityDto(authority.getAuthority()))
                                     .toList())
                .build();
    }
}
