package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnchorFollowerForm {

    @NotNull(message = "anchor_id can be not empty!")
    private String anchorId;
    @NotNull(message = "follower_id can be not empty!")
    private String followerId;
}
