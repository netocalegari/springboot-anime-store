package org.neto.anime_store.repository;

import org.neto.anime_store.domain.AnimeStoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeStoreUserRepository extends JpaRepository<AnimeStoreUser, Long> {
    AnimeStoreUser findByUsername(String username);
}
