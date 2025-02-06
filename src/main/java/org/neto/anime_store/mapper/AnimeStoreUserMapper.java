package org.neto.anime_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeStoreUserMapper {
    public static final AnimeStoreUserMapper INSTANCE = Mappers.getMapper(AnimeStoreUserMapper.class);

    public abstract AnimeStoreUser toAnimeStoreUser(AnimeStoreUserSaveRequestBody animeStoreUserSaveRequestBody);
}
