package org.neto.anime_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.neto.anime_store.domain.AnimeStoreUser;
import org.neto.anime_store.dtos.AnimeStoreUserDto;
import org.neto.anime_store.exceptions.UsernameAlreadyRegisteredException;
import org.neto.anime_store.requests.AnimeStoreUserPutRequestBody;
import org.neto.anime_store.requests.AnimeStoreUserSaveRequestBody;
import org.neto.anime_store.service.AnimeStoreUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class AnimeStoreUserController {
    private final AnimeStoreUserService animeStoreUserService;

    @PostMapping
    @Operation(
            summary = "Creates an user", tags = {"users"}
    )
    public ResponseEntity<AnimeStoreUser> save(@RequestBody AnimeStoreUserSaveRequestBody animeStoreUserSaveRequestBody) {
        ResponseEntity<AnimeStoreUserDto> existingUser = findByUsername(animeStoreUserSaveRequestBody.getUsername());


        if (existingUser.getStatusCode().equals(HttpStatus.OK)) {
            throw new UsernameAlreadyRegisteredException("Username already in use");
        }

        PasswordEncoder encoder = passwordEncoder();
        animeStoreUserSaveRequestBody.setPassword(encoder.encode(animeStoreUserSaveRequestBody.getPassword()));

        return new ResponseEntity<>(animeStoreUserService.save(animeStoreUserSaveRequestBody), HttpStatus.CREATED);
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
                         ]
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
                         ]
                     }
                 ]
            
            """
    )
    )
    )
    public ResponseEntity<List<AnimeStoreUserDto>> listAll() {
        return ResponseEntity.ok(animeStoreUserService.list());
    }

    @GetMapping(path = "/find")
    @Operation(
            summary = "Finds user by username", tags = {"users"}
    )
    public ResponseEntity<AnimeStoreUserDto> findByUsername(@RequestParam String username) {
        AnimeStoreUserDto user = animeStoreUserService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(animeStoreUserService.findByUsername(username));
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Finds user by id", tags = {"users"}
    )
    public ResponseEntity<AnimeStoreUserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(animeStoreUserService.findById(id));
    }

    @PutMapping(path = "/admin")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "When User doesn't exist in the database"),
            }
    )
    @Operation(
            summary = "Updates user information", tags = {"users"}
    )
    @Transactional
    public ResponseEntity<Void> replace(@RequestBody AnimeStoreUserPutRequestBody animeStoreUserPutRequestBody) {
        animeStoreUserService.replace(animeStoreUserPutRequestBody);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/admin/{id}")
    @Operation(
            summary = "Deletes user", tags = {"users"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "When User doesn't exist in the database"),
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animeStoreUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
