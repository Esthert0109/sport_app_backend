package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CollectionForm {
    @NotNull(message = "match_id can be not empty!")
    private Integer matchId;
    @NotNull(message = "category can be not empty!")
    private String category;
}
