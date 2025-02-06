package org.neto.anime_store.service;

import lombok.RequiredArgsConstructor;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.exceptions.NotFoundException;
import org.neto.anime_store.mapper.AnimeMapper;
import org.neto.anime_store.mapper.AnimeStoreUserMapper;
import org.neto.anime_store.repository.AnimeStoreUserRepository;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeStoreUserDetailsService implements UserDetailsService {
    private final AnimeStoreUserRepository animeStoreUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(animeStoreUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    public AnimeStoreUser save(AnimeStoreUserSaveRequestBody animeStoreUserSaveRequestBody) {
        return animeStoreUserRepository.save(AnimeStoreUserMapper.INSTANCE.toAnimeStoreUser(
                animeStoreUserSaveRequestBody));
    }

    public List<AnimeStoreUser> list() {
        return animeStoreUserRepository.findAll();
    }

    public AnimeStoreUserDto findByUsername(String username) {
        AnimeStoreUser user = animeStoreUserRepository.findByUsername(username);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return AnimeStoreUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .authorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
