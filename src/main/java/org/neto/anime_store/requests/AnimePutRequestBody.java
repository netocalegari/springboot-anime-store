package org.neto.anime_store.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequestBody {
    @Schema(
            description = "This is the Anime's id", example = "6452"
    )
    private Long id;
    @Schema(
            description = "This is the Anime's name", example = "Hajime no Ippo"
    )
    private String name;
}
