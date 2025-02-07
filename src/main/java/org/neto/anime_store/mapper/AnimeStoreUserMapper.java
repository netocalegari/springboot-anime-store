package org.neto.anime_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.dtos.AuthorityDto;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AnimeStoreUserMapper {
    AnimeStoreUser toAnimeStoreUser(AnimeStoreUserSaveRequestBody animeStoreUserSaveRequestBody);

    @Mapping(source = "password", target = "password")
    AnimeStoreUser dtoToAnimeStoreUser(AnimeStoreUserDto animeStoreUserDto);

    default String mapAuthorities(List<AuthorityDto> authorities) {
        if (authorities == null) {
            return null;
        }

        return authorities.stream()
                .map(AuthorityDto::getAuthority)
                .collect(Collectors.joining(", "));
    }

    default String mapPassword(String password) {
        // Lógica de hashing aqui, se necessário
        return password;  // Exemplo de retorno, não altere a senha no momento
    }
}
