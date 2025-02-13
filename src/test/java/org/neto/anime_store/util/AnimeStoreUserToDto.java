package org.neto.anime_store.util;

import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.dtos.AuthorityDto;

public class AnimeStoreUserToDto {
    public static AnimeStoreUserDto animeStoreUserToDto(AnimeStoreUser animeStoreUser) {
        return AnimeStoreUserDto.builder()
                .id(animeStoreUser.getId())
                .name(animeStoreUser.getName())
                .username(animeStoreUser.getUsername())
                .password(animeStoreUser.getPassword())
                .authorities(animeStoreUser.getAuthorities()
                                     .stream()
                                     .map(authority -> new AuthorityDto(authority.getAuthority()))
                                     .toList())
                .build();
    }
}
