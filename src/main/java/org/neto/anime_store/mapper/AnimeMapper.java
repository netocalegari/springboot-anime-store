package org.neto.anime_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.requests.AnimePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
