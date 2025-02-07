package org.neto.anime_store.service;

import lombok.RequiredArgsConstructor;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.dtos.AuthorityDto;
import org.neto.anime_store.exceptions.NotFoundException;
import org.neto.anime_store.mapper.AnimeMapper;
import org.neto.anime_store.mapper.AnimeStoreUserMapper;
import org.neto.anime_store.repository.AnimeStoreUserRepository;
import org.neto.anime_store.requests.AnimeStoreUserPutRequestBody;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeStoreUserService {
    private final AnimeStoreUserRepository animeStoreUserRepository;
    private final AnimeStoreUserMapper animeStoreUserMapper;

    @Transactional(rollbackFor = Exception.class)
    public AnimeStoreUser save(AnimeStoreUserSaveRequestBody animeStoreUserSaveRequestBody) {
        return animeStoreUserRepository.save(animeStoreUserMapper.toAnimeStoreUser(animeStoreUserSaveRequestBody));
    }

    public List<AnimeStoreUserDto> list() {
        return animeStoreUserRepository.findAll()
                .stream()
                .map(user -> AnimeStoreUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getAuthorities()
                                             .stream()
                                             .map(auth -> new AuthorityDto(auth.getAuthority())) // Converte para
                                             // AuthorityDto
                                             .toList())
                        .build())
                .toList();
    }


    public AnimeStoreUserDto findByUsername(String username) {
        AnimeStoreUser user = animeStoreUserRepository.findByUsername(username);

        if (user == null) {
            return null;
        }

        return AnimeStoreUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities().stream().map(a -> new AuthorityDto(a.getAuthority())).toList())
                .build();
    }

    public AnimeStoreUserDto findById(Long id) {
        AnimeStoreUser user = animeStoreUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return AnimeStoreUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .authorities(user.getAuthorities().stream().map(a -> new AuthorityDto(a.getAuthority())).toList())
                .password(user.getPassword())
                .build();
    }

    public void replace(AnimeStoreUserPutRequestBody animeStoreUserPutRequestBody) {
        AnimeStoreUserDto userFound = findById(animeStoreUserPutRequestBody.getId());

        AnimeStoreUserDto updatedUserDto = AnimeStoreUserDto.builder()
                .id(userFound.getId())
                .name(Optional.ofNullable(animeStoreUserPutRequestBody.getName()).orElse(userFound.getName()))
                .username(Optional.ofNullable(animeStoreUserPutRequestBody.getUsername())
                                  .orElse(userFound.getUsername()))
                .authorities(Optional.ofNullable(animeStoreUserPutRequestBody.getAuthorities())
                                     .orElse(userFound.getAuthorities()))
                .build();

        AnimeStoreUser animeStoreUser = animeStoreUserMapper.dtoToAnimeStoreUser(updatedUserDto);

        animeStoreUserRepository.save(animeStoreUser);
    }

    public void delete(Long id) {
        AnimeStoreUserDto userDto = findById(id);
        AnimeStoreUser animeStoreUser = animeStoreUserMapper.dtoToAnimeStoreUser(userDto);

        animeStoreUserRepository.delete(animeStoreUser);
    }
}

