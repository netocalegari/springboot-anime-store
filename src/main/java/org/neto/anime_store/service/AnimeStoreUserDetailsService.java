package org.neto.anime_store.service;

import lombok.RequiredArgsConstructor;
import org.neto.anime_store.repository.AnimeStoreUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
