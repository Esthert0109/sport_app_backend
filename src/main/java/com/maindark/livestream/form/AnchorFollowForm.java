package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnchorFollowForm {

    @NotNull(message = "anchor_id should not be empty")
    private Long anchorId;
}
