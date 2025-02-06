package org.neto.anime_store.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AnimeStoreUserDto {
    private Long id;
    private String username;
    private String name;
    private List<String> authorities;
}
