package org.neto.anime_store.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.neto.anime_store.dtos.AuthorityDto;

import java.util.List;

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
    private List<AuthorityDto> authorities;

    @NotEmpty(message = "The password cannot be empty")
    @Schema(
            description = "This is the User's password hash",
            example = "159753"
    )
    private String password;
}
