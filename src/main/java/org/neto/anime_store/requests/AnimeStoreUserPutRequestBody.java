package org.neto.anime_store.requests;

import lombok.Builder;
import lombok.Data;
import org.neto.anime_store.dtos.AuthorityDto;

import java.util.List;

@Data
@Builder
public class AnimeStoreUserPutRequestBody {
    private Long id;
    private String name;
    private String username;
    private List<AuthorityDto> authorities;
}
