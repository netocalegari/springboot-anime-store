package org.neto.anime_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.neto.anime_store.domain.Anime;
import org.neto.anime_store.requests.AnimePostRequestBody;
import org.neto.anime_store.requests.AnimePutRequestBody;
import org.neto.anime_store.service.AnimeService;
import org.neto.anime_store.utils.DateUtil;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    @Operation(
            summary = "Lists all animes paginated",
            description = "The default size is 20, use the parameter 'size' to change the default value",
            tags = {"anime"}
    )
    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    @Operation(
            summary = "Lists all animes",
            description = "Returns a list with all animes in the database",
            tags = {"anime"}
    )
    public ResponseEntity<List<Anime>> listAll() {
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Finds anime by id", description = "Returns anime matching informed id", tags = {"anime"}
    )
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "by-id/{id}")
    @Operation(
            tags = {"anime"}
    )
    public ResponseEntity<Anime> findByIdAuthenticated(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    @Operation(
            summary = "Finds anime by name", description = "Returns anime matching name", tags = {"anime"}
    )
    @ApiResponse(
            responseCode = "200", description = "Successful retrieval of anime list", content = @Content(
            mediaType = "application/json", examples = @ExampleObject(
            name = "Anime List Example", value = """
                [
                  {
                    "id": 1,
                    "name": "Naruto"
                  },
                  {
                    "id": 2,
                    "name": "Hunter x Hunter"
                  }
                ]
            """
    )
    )
    )
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @PostMapping
    @Operation(
            summary = "Saves anime on database", description = "Saves anime on database", tags = {"anime"}
    )
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/{id}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "When Anime doesn't exist in the database"),
            }
    )
    @Operation(
            summary = "Deletes anime from database", description = "Deletes anime from database", tags = {"anime"}
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "When Anime doesn't exist in the database"),
            }
    )
    @Operation(
            summary = "Updates found anime", description = "Updates found anime", tags = {"anime"}
    )
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody) {
        animeService.replace(animePutRequestBody);
        return ResponseEntity.noContent().build();
    }
}
