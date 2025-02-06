package org.neto.anime_store.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimeStoreUserSaveRequestBody {
    @NotEmpty(message = "The name cannot be empty")
    @Schema(
            description = "This is the User's name", example = "John Doe"
    )
    private String name;

    @NotEmpty(message = "The username cannot be empty")
    @Schema(
            description = "This is the User's username", example = "johnnydoe"
    )
    private String username;

    @NotEmpty(message = "The authorities cannot be empty")
    @Schema(
            description = "This is the User's authorities", example = "ROLE_ADMIN, ROLE_USER"
    )
    private String authorities;

    @NotEmpty(message = "The password cannot be empty")
    @Schema(
            description = "This is the User's password hash",
            example = "$2a$10$5z5iZto9CFpI/rIB1qL8MuNv46tir1HABUNoZ91tUUrQcmBPzdWN5C"
    )
    private String password;
}
