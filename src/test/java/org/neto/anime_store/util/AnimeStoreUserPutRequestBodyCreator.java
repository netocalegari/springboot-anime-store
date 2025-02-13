package org.neto.anime_store.util;

import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AuthorityDto;
import org.neto.anime_store.requests.AnimeStoreUserPutRequestBody;

public class AnimeStoreUserPutRequestBodyCreator {
    public static AnimeStoreUserPutRequestBody createAnimeStoreUserPutRequestBody() {
        AnimeStoreUser validAnimeStoreUser = AnimeStoreUserCreator.createValidAnimeStoreUser();

        return AnimeStoreUserPutRequestBody.builder()
                .id(validAnimeStoreUser.getId())
                .name(validAnimeStoreUser.getName())
                .username(validAnimeStoreUser.getUsername())
                .authorities(validAnimeStoreUser.getAuthorities()
                                     .stream()
                                     .map(authority -> new AuthorityDto(authority.getAuthority()))
                                     .toList())
                .build();
    }
}
