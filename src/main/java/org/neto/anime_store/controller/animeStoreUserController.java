package org.neto.anime_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.mapper.AnimeStoreUserMapper;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.requests.AnimePutRequestBody;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;
import org.neto.anime_store.service.AnimeService;
import org.neto.anime_store.service.AnimeStoreUserDetailsService;
import org.neto.anime_store.utils.DateUtil;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class animeStoreUserController {
    private final AnimeStoreUserDetailsService animeStoreUserDetailsService;

    @PostMapping
    @Operation(
            summary = "Creates an user", tags = {"users"}
    )
    public ResponseEntity<AnimeStoreUser> save(@RequestBody AnimeStoreUserSaveRequestBody animeStoreUserSaveRequestBody) {
        PasswordEncoder encoder = passwordEncoder();
        animeStoreUserSaveRequestBody.setPassword(encoder.encode(animeStoreUserSaveRequestBody.getPassword()));

        return new ResponseEntity<>(
                animeStoreUserDetailsService.save(animeStoreUserSaveRequestBody),
                                    HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(
            summary = "Lists all users", tags = {"users"}
    )
    @ApiResponse(
            responseCode = "200", description = "Successful retrieval of users list", content = @Content(
            mediaType = "application/json", examples = @ExampleObject(
            name = "Users List Example", value = """
                [
                     {
                         "id": 1,
                         "name": "Giorno Giovanna",
                         "username": "jojo",
                         "authorities": [
                             {
                                 "authority": "ROLE_USER"
                             }
                         ],
                         "enabled": true,
                         "credentialsNonExpired": true,
                         "accountNonExpired": true,
                         "accountNonLocked": true
                     },
                     {
                         "id": 2,
                         "name": "Gon Freecs",
                         "username": "gon",
                         "authorities": [
                             {
                                 "authority": "ROLE_USER"
                             },
                             {
                                 "authority": "ROLE_ADMIN"
                             }
                         ],
                         "enabled": true,
                         "credentialsNonExpired": true,
                         "accountNonExpired": true,
                         "accountNonLocked": true
                     }
                 ]
            
            """
    )
    )
    )
    public ResponseEntity<List<AnimeStoreUser>> listAll() {
        return ResponseEntity.ok(animeStoreUserDetailsService.list());
    }

    @GetMapping(path = "/find")
    public ResponseEntity<AnimeStoreUserDto> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(animeStoreUserDetailsService.findByUsername(username));
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
