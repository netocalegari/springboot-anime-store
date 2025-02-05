package org.neto.anime_store.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePostRequestBody {
    @NotEmpty(message = "The name cannot be empty")
    @Schema(
            description = "This is the Anime's name", example = "Hajime no Ippo"
    )
    private String name;
}
