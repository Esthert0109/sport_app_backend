package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PopularSearchForm {
    @NotNull(message = "popular_keywords can be not empty!")
    private String popularKeywords;
}
